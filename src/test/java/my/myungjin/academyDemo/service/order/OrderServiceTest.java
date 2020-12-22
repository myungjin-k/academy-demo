package my.myungjin.academyDemo.service.order;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.CartItem;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.util.Util;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import static java.time.LocalTime.now;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class OrderServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private OrderService orderService;

    private Id<Member, String> memberId;

    private Id<CartItem, String> cartItemId;

    @BeforeAll
    void setup(){
        memberId = Id.of(Member.class, "3a18e633a5db4dbd8aaee218fe447fa4");
    }

    @Test
    @Order(1)
    void 주문_생성하기(){
        String id = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHH24mmss")) + randomAlphabetic(4).toUpperCase(Locale.ROOT);
        log.info("order id: {}", id);
        my.myungjin.academyDemo.domain.order.Order order = my.myungjin.academyDemo.domain.order.Order.builder()
                .id(id)
                .orderName("김명진")
                .orderTel("010-1234-5678")
                .orderAddr1("서울시 노원구 공릉로59길 28")
                .orderAddr2("1-1111")
                .build();
        Delivery delivery = Delivery.builder()
                .id(Util.getUUID())
                .receiverName("김명진")
                .receiverTel("010-1234-5678")
                .receiverAddr1("서울시 노원구 공릉로59길 28")
                .receiverAddr2("1-1111")
                .status(DeliveryStatus.PROCESSING)
                .build();

        List<Id<CartItem, String>> itemIds = Arrays.asList(
                Id.of(CartItem.class, "f4597dfc1ae649a58edcb7921002aca5"),
                Id.of(CartItem.class, "0a25d9eea6d94a3897e06b33e4bf5b69")
        );
        my.myungjin.academyDemo.domain.order.Order saved = orderService.ordering(memberId, order, delivery, itemIds);
        assertThat(saved, is(notNullValue()));
        log.info("Saved Order: {}", saved);
        log.info("Saved Order Item: {}", saved.getItems());
        log.info("Saved Delivery: {}", saved.getDeliveries());
    }

}
