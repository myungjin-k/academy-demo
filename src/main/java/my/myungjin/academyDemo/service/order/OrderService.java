package my.myungjin.academyDemo.service.order;

import com.siot.IamportRestClient.exception.IamportResponseException;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.Coupon;
import my.myungjin.academyDemo.domain.event.CouponRepository;
import my.myungjin.academyDemo.domain.member.*;
import my.myungjin.academyDemo.domain.order.*;
import my.myungjin.academyDemo.domain.review.ReviewRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.error.StatusNotSatisfiedException;
import my.myungjin.academyDemo.service.mail.MailService;
import my.myungjin.academyDemo.util.Util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final OrderItemRepository orderItemRepository;

    private final DeliveryRepository deliveryRepository;

    private final DeliveryItemRepository deliveryItemRepository;

    private final CartRepository cartRepository;

    private final MemberRepository memberRepository;

    private final ReviewRepository reviewRepository;

    private final ReservesHistoryRepository reservesHistoryRepository;

    private final CouponRepository couponRepository;

    private final MailService mailService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Transactional(readOnly = true)
    public Order getOrderDetail(@Valid Id<Member, String> memberId, @Valid Id<Order, String> orderId) {
        Order o = findById(memberId, orderId);
        o.setDeliveries(deliveryRepository.getAllByOrderOrderByCreateAtDesc(o));
        return o;
    }

    @Transactional(readOnly = true)
    public Page<Order> findAllMyMemberWithPage(@Valid Id<Member, String> memberId, Pageable pageable){
        return orderRepository.findAllByMemberId(memberId.value(), pageable)
                .map(order -> {
                    order.setDeliveries(deliveryRepository.getAllByOrderOrderByCreateAtDesc(order));
                    return getItems(memberId, order);
                });
    }

    private Order findById(@Valid Id<Member, String> memberId, @Valid Id<Order, String> orderId){
        return orderRepository.findByMemberIdAndId(memberId.value(), orderId.value())
                .map(order -> {
                    order.setDeliveries(deliveryRepository.getAllByOrderOrderByCreateAtDesc(order));
                    return getItems(memberId, order);
                }).orElseThrow(() -> new NotFoundException(Order.class, memberId, orderId));
    }

    private Order getItems(@Valid Id<Member, String> memberId, @Valid Order o){
        for(OrderItem item : o.getItems()){
            List<DeliveryItem> deliveryItems = deliveryItemRepository
                    .findAllByDeliveryOrderAndItemOptionIdAndDeliveryStatusNotOrderByCreateAtDesc(item.getOrder(), item.getItemOption().getId(), DeliveryStatus.DELETED);
            if(deliveryItems.isEmpty())
                continue;

            DeliveryItem dItem = deliveryItems.get(0);
            item.setDeliveryItem(dItem);
            if(!dItem.getDelivery().getStatus().equals(DeliveryStatus.DELIVERED))
                continue;
            item.setReview(reviewRepository.findByOrderItemIdAndMemberId(item.getId(), memberId.value()).orElse(null));
        }
        return o;
    }

    public Optional<Member> findMemberInfo(@Valid Id<Member, String> memberId){
        return memberRepository.findById(memberId.value())
                .map(member -> {
                    member.setCoupons(couponRepository.findByMember(member));
                    return member;
                });
    }

    @Transactional
    public Order ordering(@Valid Id<Member, String> memberId, @Valid Order newOrder,
                          @Valid Delivery delivery, List<Id<CartItem, String>> itemIds,  Optional<Id<Coupon, String>> usedCouponId) {
        // 주문
        Order saved = memberRepository.findById(memberId.value())
                .map(member -> {
                    newOrder.setMember(member);
                    return save(newOrder);
                }).orElseThrow(() ->  new NotFoundException(Member.class, memberId));
        // 주문상품
        Order updated = saveOrderItems(itemIds, saved);
        // 적립금 업데이트 + 등급 업데이트
        Member m = updated.getMember();

        if(!reservesHistoryRepository.existsByTypeAndRefId(ReservesType.ORDER, updated.getId())){
            int usedPoint = updated.getUsedPoints();
            if(updated.getUsedPoints() == 0){
                int reserve = (int) (updated.getTotalAmount() * m.getRating().getReserveRatio());
                m.addReserves(reserve);
                m.addReservesHistory(new ReservesHistory(reserve, ReservesType.ORDER, updated.getId()));
            } else {
                m.flushReserves(updated.getUsedPoints());
                m.addReservesHistory(new ReservesHistory(-usedPoint, ReservesType.ORDER_USED, updated.getId()));
            }

            m.addOrderAmountAndUpdateRating(updated.getTotalAmount() - updated.getUsedPoints());
        }

        // 배송정보
        delivery.setOrder(updated);
        delivery.setExtDeliveryId("EXT" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH24mmssSSS")));
        Delivery d = save(delivery);
        // 배송상품
        saveDeliveryItems(updated.getItems(), d);
        updated.addDelivery(d);

        // 쿠폰
        usedCouponId.ifPresentOrElse
                (couponStringId ->
                    couponRepository.findById(usedCouponId.get().value())
                        .map(coupon -> {
                            coupon.use();
                            return couponRepository.save(coupon);
                        }).orElseThrow(() -> new NotFoundException(Coupon.class, usedCouponId)),
                () -> log.info("Coupon didn't used"));

        // 메일 발송
        updated.getOrderEmail().ifPresent(s -> this.sendMail(s, updated));

        return updated;
    }
    private void sendMail(String email, Order order){
        /*Mail mail = Mail.builder()
                .to(email)
                .title("[mesmerizin'] 주문하신 상품 내역입니다.")
                .htmlBody(order.toString()).build();*/
        String title = "[mesmerizin'] 주문하신 상품 내역입니다.";

        Map<String, Object> emailTemplateModel = new HashMap<>();
        emailTemplateModel.put("order", order);

        try {
            //mailService.sendMail(mail);
            mailService.sendMessageUsingThymeleafTemplate(
                    email,
                    title,
                    "orderCompleted",
                    emailTemplateModel);
        } catch (MessagingException | UnsupportedEncodingException e){
            log.warn("Messaging Error : {}", e.getMessage(), e);
        } catch (MailException me){
            log.warn("Mailing Error : {}", me.getMessage(), me);
        }
    }
    @Transactional
    public Order modify(@Valid Id<Member, String> memberId, @Valid Id<Order, String> orderId, @Valid Order order) {
        Order o = findById(memberId, orderId);
        Delivery d = deliveryRepository.getByOrderMemberIdAndOrderId(memberId.value(), orderId.value());
        if(d.getStatus().getValue() > 1){
            throw new StatusNotSatisfiedException(Order.class, orderId, d.getStatus());
        }
        o.modify(order.getOrderName(), order.getOrderEmail().orElse(""), order.getOrderTel(), order.getOrderAddr1(), order.getOrderAddr2());
        return save(o);
    }

    @Transactional
    public Delivery modify(@Valid Id<Member, String> memberId, @Valid Id<Order, String> orderId,
                           @Valid Id<Delivery, String> deliveryId, @Valid Delivery delivery) {
        Delivery d = deliveryRepository.findByOrderMemberIdAndOrderIdAndId(memberId.value(), orderId.value(), deliveryId.value())
                .orElseThrow(() -> new NotFoundException(Delivery.class, memberId, orderId, deliveryId));
        if(d.getStatus().getValue() > 1){
            throw new StatusNotSatisfiedException(Delivery.class, deliveryId, d.getStatus());
        }
        d.modify(delivery.getReceiverName(), delivery.getReceiverTel(),
                delivery.getReceiverAddr1(), delivery.getReceiverAddr2(), delivery.getMessage());
        return save(d);
    }

    public Order cancel(@Valid Id<Member, String> memberId, @Valid Id<Order, String> orderId) throws IOException, IamportResponseException {
        Order order = orderRepository.findByMemberIdAndId(memberId.value(), orderId.value())
                .map(o -> {
                    o.setDeliveries(deliveryRepository.getAllByOrderOrderByCreateAtDesc(o));
                    return o;
                }).orElseThrow(() -> new NotFoundException(Order.class, memberId.value(), orderId.value()));

        List<Delivery> deliveries = order.getDeliveries();
        for(Delivery d : deliveries){
            if(!DeliveryStatus.REQUESTED.equals(d.getStatus())){
                throw new IllegalArgumentException("배송정보가 존재합니다. 관리자에게 문의 바랍니다. orderId=" + orderId + "deliveryId=" + d.getId());
            }
            d.updateStatus(DeliveryStatus.DELETED);
            save(d);
        }
        order.cancel();
        if(!reservesHistoryRepository.existsByTypeAndRefId(ReservesType.ORDER_CANCEL, order.getId())){
            int reserves = 0;
            Member member = order.getMember();
            if(order.getUsedPoints() > 0){
                reserves = order.getUsedPoints();
                member.addReserves(reserves);
            } else {
                reserves = -reservesHistoryRepository
                        .getByTypeAndRefId(ReservesType.ORDER, order.getId())
                        .getAmount();
                member.flushReserves(reserves);
            }

            ReservesHistory newHistory = ReservesHistory.builder()
                    .amount(reserves)
                    .type(ReservesType.ORDER_CANCEL)
                    .refId(order.getId())
                    .build();
            newHistory.setMember(member);
            reservesHistoryRepository.save(newHistory);
        }
        return save(order);
    }

    private Order saveOrderItems(List<Id<CartItem, String>> itemIds, Order order){
        List<CartItem> items = itemIds.stream()
                .map(itemId -> cartRepository.findById(itemId.value())
                                .orElseThrow(() -> new NotFoundException(CartItem.class, itemId)))
                .collect(Collectors.toList());

        int totalAmount = 0;
        for(CartItem item : items){
            OrderItem oItem = new OrderItem(Util.getUUID(), item.getCount());
            oItem.setItemOption(item.getItemOption());
            oItem.setOrder(order);
            order.addItem(save(oItem));
            totalAmount += oItem.getItemOption().getItemDisplay().getItemMaster().getPrice() * item.getCount();
        }
        order.setTotalAmount(totalAmount);
        StringBuilder abbrOrderItems = new StringBuilder();
        abbrOrderItems.append( items.get(0).getItemOption().getItemDisplay().getItemDisplayName());
        if(items.size() > 1)
            abbrOrderItems.append("外 ").append(items.size() - 1).append("건");
        order.setAbbrOrderItems(abbrOrderItems.toString());
        deleteCartItems(items);
        return order;
    }

    private void saveDeliveryItems(List<OrderItem> orderItems, Delivery delivery){
        for(OrderItem item : orderItems){
            DeliveryItem dItem = new DeliveryItem(item.getCount());
            dItem.setItemOption(item.getItemOption());
            dItem.setOrderItem(item);
            delivery.addItem(dItem);
            item.setDeliveryItem(save(dItem));
        }
    }

    private DeliveryItem save(DeliveryItem deliveryItem){
        return deliveryItemRepository.save(deliveryItem);
    }

    private Delivery save(Delivery delivery){
        return deliveryRepository.save(delivery);
    }

    private OrderItem save(OrderItem orderItem){
       return orderItemRepository.save(orderItem);
    }

    private Order save(Order order){
        return orderRepository.save(order);
    }

    private void deleteCartItems(List<CartItem> items){
        cartRepository.deleteAll(items);
    }
}
