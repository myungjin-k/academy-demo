package my.myungjin.academyDemo.service.order;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.Payment;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.commons.mail.Mail;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import my.myungjin.academyDemo.domain.order.*;
import my.myungjin.academyDemo.domain.review.ReviewRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.error.StatusNotSatisfiedException;
import my.myungjin.academyDemo.iamport.IamportClient;
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
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

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

    private final IamportClient iamportClient;

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
        return memberRepository.findById(memberId.value());
    }

    public Payment pay(@NotBlank String impUid) throws IOException, IamportResponseException {
        return iamportClient.paymentByImpUid(impUid).getResponse();
    }

    // TODO 주문 확인 이메일 발송
    @Transactional
    public Order ordering(@Valid Id<Member, String> memberId, @Valid Order newOrder,
                          @Valid Delivery delivery, List<Id<CartItem, String>> itemIds) throws IOException, IamportResponseException {
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
        m.flushReserves(updated.getUsedPoints());
        if(updated.getUsedPoints() == 0){
            int reserve = (int) (updated.getTotalAmount() * m.getRating().getReserveRatio());
            m.addReserves(reserve);
        }
        m.addOrderAmountAndUpdateRating(updated.getTotalAmount() - updated.getUsedPoints());

        // 배송정보
        delivery.setOrder(updated);
        Delivery d = save(delivery);
        // 배송상품
        saveDeliveryItems(updated.getItems(), d);
        updated.addDelivery(d);
        // 메일 발송
        updated.getOrderEmail().ifPresent(s -> this.sendMail(s, updated));

        return updated;
    }
    private void sendMail(String email, Order order){
        Mail mail = Mail.builder()
                .to(email)
                .title("[mesmerizin'] 주문하신 상품 내역입니다.")
                .content(order.toString()).build();
        try {
            mailService.sendMail(mail);
        } catch (MessagingException | UnsupportedEncodingException e){
            log.warn("Messaging Error ({}) : {}", mail, e.getMessage(), e);
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
