package my.myungjin.academyDemo.service.order;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryItem;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

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

    private Id<ItemDisplay.ItemDisplayOption, String> itemId;

    private Id<ItemDisplay.ItemDisplayOption, String> newItemId;

    @BeforeAll
    void setup(){
        deliveryId = Id.of(Delivery.class, "cd2940ee2dfc418384eedc450be832a2");
        itemId = Id.of(ItemDisplay.ItemDisplayOption.class, "c9402883dbe540e898a417e4884845bf");
        newItemId = Id.of(ItemDisplay.ItemDisplayOption.class, "8130cede06c04fa2bbd8bc09a29787c8");
    }

    @Test
    @Order(1)
    void 배송정보_조회하기_배송아이디로(){
        Delivery d = deliveryService.findById(deliveryId);
        assertThat(d, is(notNullValue()));
        log.info("Found Delivery: {}", d);
    }

    @Test
    @Order(2)
    void 배송상품_조회하기(){
        DeliveryItem deliveryItem = deliveryService.findItemByDeliveryAndItem(deliveryId, itemId).orElse(null);
        assertThat(deliveryItem, is(notNullValue()));
        log.info("Found Delivery Item: {}", deliveryItem);
    }


    @Test
    @Order(3)
    void 배송상품_추가하기(){
        DeliveryItem delivery = deliveryService.addDeliveryItem(deliveryId, newItemId).orElse(null);
        assertThat(delivery, is(notNullValue()));
        log.info("Added Item: {}", delivery);
    }


    @Test
    @Order(4)
    void 배송상품_삭제하기(){
        DeliveryItem deleted = deliveryService.deleteDeliveryItem(deliveryId, newItemId);
        assertThat(deleted, is(notNullValue()));
        log.info("Deleted Item: {}", deleted);
    }

    @Test
    @Order(5)
    void 송장_업데이트하기(){
        Delivery updated = deliveryService.updateInvoice(deliveryId, "012345678");
        assertThat(updated, is(notNullValue()));
        log.info("Updated Delivery: {}", updated);
    }

    @Test
    @Order(6)
    void 배송상태_업데이트하기(){
        Delivery updated = deliveryService.modifyStatus(deliveryId, DeliveryStatus.SHIPPED);
        assertThat(updated, is(notNullValue()));
        log.info("Updated Delivery: {}", updated);
    }
}
