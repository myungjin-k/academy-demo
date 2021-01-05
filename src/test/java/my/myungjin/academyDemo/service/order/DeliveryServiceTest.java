package my.myungjin.academyDemo.service.order;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryItem;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.domain.order.OrderItem;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DeliveryServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DeliveryService deliveryService;

    private Id<Delivery, String> deliveryId;

    private Id<ItemDisplay.ItemDisplayOption, String> newItemId;

    private Id<my.myungjin.academyDemo.domain.order.Order, String> orderId;

    private Id<Delivery, String> newDeliveryId;

    private Id<DeliveryItem, String> deliveryItemId;

    private Id<DeliveryItem, String> newDeliveryItemId;

    @BeforeAll
    void setup(){
        deliveryId = Id.of(Delivery.class, "cd2940ee2dfc418384eedc450be832a2");
        deliveryItemId = Id.of(DeliveryItem.class, "d14b36612cd047a0b1e4e71d993dc9b2");
        newItemId = Id.of(ItemDisplay.ItemDisplayOption.class, "8130cede06c04fa2bbd8bc09a29787c8");
        orderId = Id.of(my.myungjin.academyDemo.domain.order.Order.class, "03039b4535404247bfee52cfd934c779");
    }

    @Test
    @Sql("/db/order-data-setup.sql")
    @Order(1)
    void 배송정보_조회하기_배송아이디로(){
        Delivery d = deliveryService.findById(deliveryId);
        assertThat(d, is(notNullValue()));
        log.info("Found Delivery: {}", d);
    }

    @Test
    @Order(2)
    void 배송상품_조회하기(){
        DeliveryItem deliveryItem = deliveryService.findItem(deliveryId, deliveryItemId).orElse(null);
        assertThat(deliveryItem, is(notNullValue()));
        log.info("Found Delivery Item: {}", deliveryItem);
    }


    @Test
    @Order(3)
    void 배송상품_추가하기(){
        DeliveryItem deliveryItem = deliveryService.addDeliveryItem(deliveryId, newItemId, 1).orElse(null);
        assertThat(deliveryItem, is(notNullValue()));
        newDeliveryItemId = Id.of(DeliveryItem.class, deliveryItem.getId());
        log.info("Added Item: {}", deliveryItem);
    }

    @Test
    @Order(4)
    void 배송상품_수량_수정하기(){
        DeliveryItem modified = deliveryService.modifyDeliveryItemCount(deliveryId, deliveryItemId, 1);
        assertThat(modified, is(notNullValue()));
        log.info("Modified Item: {}", modified);
    }

    @Test
    @Order(5)
    void 배송상품_삭제하기(){
        DeliveryItem deleted = deliveryService.deleteDeliveryItem(deliveryId, newDeliveryItemId);
        assertThat(deleted, is(notNullValue()));
        log.info("Deleted Item: {}", deleted);
    }

    @Test
    @Order(6)
    void 송장_업데이트하기(){
        Delivery updated = deliveryService.updateInvoice(deliveryId, "012345678");
        assertThat(updated, is(notNullValue()));
        log.info("Updated Delivery: {}", updated);
    }

    @Test
    @Order(7)
    void 배송상태_업데이트하기(){
        Delivery updated = deliveryService.modifyStatus(deliveryId, DeliveryStatus.SHIPPED);
        assertThat(updated, is(notNullValue()));
        log.info("Updated Delivery: {}", updated);
    }

    @Test
    @Order(8)
    void 배송정보_추가하기(){
        Delivery newDelivery = Delivery.builder()
                .receiverName("명진")
                .receiverTel("010-1234-5678")
                .receiverAddr1("XX시 XX구 XX로")
                .receiverAddr2("1-1111")
                .status(DeliveryStatus.PROCESSING)
                .build();

        List<Id<OrderItem, String>> items = new ArrayList<>();
        // 테스트 용
        List<OrderItem> orderItems = deliveryService.findAllOrderItems(deliveryId);
        items.add(Id.of(OrderItem.class, orderItems.get(0).getId()));

        Delivery d = deliveryService.addDelivery(orderId, newDelivery, items);
        newDeliveryId = Id.of(Delivery.class, d.getId());
        assertThat(d, is(notNullValue()));
        log.info("Added Delivery: {}", d);
        log.info("Added Delivery Item: {}", d.getItems());
    }

    @Test
    @Order(9)
    void 배송_취소하기(){
        Delivery deleted = deliveryService.deleteDelivery(newDeliveryId);
        assertThat(deleted, is(notNullValue()));
        log.info("Deleted Delivery: {}", deleted);
    }

}
