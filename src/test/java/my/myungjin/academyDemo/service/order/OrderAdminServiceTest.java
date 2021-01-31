package my.myungjin.academyDemo.service.order;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.CartItem;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderAdminServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    private Id<Member, String> memberId;

    private Id<my.myungjin.academyDemo.domain.order.Order, String> orderId;

    private Id<Delivery, String> deliveryId;

    @BeforeAll
    void setup(){
        memberId = Id.of(Member.class, "3a18e633a5db4dbd8aaee218fe447fa4");
    }

    @Test
    @Order(1)
    void 주문_생성하기(){
        my.myungjin.academyDemo.domain.order.Order order = my.myungjin.academyDemo.domain.order.Order.builder()
                .orderName("김명진")
                .orderTel("010-1234-5678")
                .orderAddr1("서울시 노원구 공릉로59길 28")
                .orderAddr2("1-1111")
                .usedPoints(0)
                .build();
        Delivery delivery = Delivery.builder()
                .receiverName("김명진")
                .receiverTel("010-1234-5678")
                .receiverAddr1("서울시 노원구 공릉로59길 28")
                .receiverAddr2("1-1111")
                .status(DeliveryStatus.PROCESSING)
                .build();

        List<Id<CartItem, String>> itemIds = Arrays.asList(
                Id.of(CartItem.class, "f4597dfc1ae649a58edcb7921002aca5")
                //Id.of(CartItem.class, "0a25d9eea6d94a3897e06b33e4bf5b69")
        );
        my.myungjin.academyDemo.domain.order.Order saved = orderService.ordering(memberId, order, delivery, itemIds);
        orderId = Id.of(my.myungjin.academyDemo.domain.order.Order.class, saved.getId());
        deliveryId = Id.of(Delivery.class, saved.getDeliveries().get(0).getId());
        assertThat(saved, is(notNullValue()));
        log.info("Saved Order: {}", saved);
        log.info("Saved Order Item: {}", saved.getItems());
        log.info("Saved Delivery: {}", saved.getDeliveries());
    }

    @Test
    @Order(3)
    void 주문_조회하기_회원별_전체(){
        PageRequest pageRequest = new PageRequest();
        pageRequest.setPage(0);
        pageRequest.setSize(5);
        pageRequest.setDirection(Sort.Direction.DESC);

        Page<my.myungjin.academyDemo.domain.order.Order> orders = orderService.findAllMyMemberWithPage(memberId, pageRequest.of());
        assertThat(orders, is(notNullValue()));
        log.info("Found Order: {}", orders.getContent());
        for(my.myungjin.academyDemo.domain.order.Order o : orders.getContent()){
            log.info("Found Order Item: {}", o.getItems());
        }
    }
/*
    @Test
    @Order(4)
    void 주문_조회하기_회원별_단건(){
        my.myungjin.academyDemo.domain.order.Order order = orderService.findById(memberId, orderId);
        assertThat(order, is(notNullValue()));
        log.info("Found Order: {}", order);
        //log.info("Found Delivery: {}", order.getDeliveries());
        log.info("Found Order Item: {}", order.getItems());
        log.info("Found Order Item Delivery: {}", order.getItems().stream().map(OrderItem::getDeliveryId));
    }*/

    @Test
    @Order(5)
    void 주문_수정하기_회원별_단건(){
        my.myungjin.academyDemo.domain.order.Order o = my.myungjin.academyDemo.domain.order.Order.builder()
                .id(orderId.value())
                .orderName("김명진")
                .orderTel("010-1234-5678")
                .orderAddr1("서울시 노원구 공릉로59길 28")
                .orderAddr2("99999999999")
                .build();

        my.myungjin.academyDemo.domain.order.Order order = orderService.modify(memberId, orderId, o);

        assertThat(order, is(notNullValue()));
        log.info("Updated Order: {}", order);
    }

    /*@Test
    @Order(4)
    void 배송상태_수정(){

        Delivery delivery = orderService.modifyStatus(deliveryId,DeliveryStatus.SHIPPED);
        assertThat(delivery, is(notNullValue()));
        log.info("Updated Delivery: {}", delivery);
    }*/


    @Test
    @Order(6)
    void 배송정보_수정하기_회원별_단건(){

        Delivery d = Delivery.builder()
                .id(deliveryId.value())
                .receiverName("김명진")
                .receiverTel("010-1234-5678")
                .receiverAddr1("서울시 XX구 OOO로 12345678")
                .receiverAddr2("1-1111")
                .status(DeliveryStatus.PROCESSING)
                .build();

        Delivery delivery = orderService.modify(memberId, orderId, deliveryId, d);
        assertThat(delivery, is(notNullValue()));
        log.info("Updated Delivery: {}", delivery);
    }
}
