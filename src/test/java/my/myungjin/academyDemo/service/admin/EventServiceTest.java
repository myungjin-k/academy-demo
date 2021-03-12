package my.myungjin.academyDemo.service.admin;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Rating;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static java.util.Collections.emptySet;
import static org.assertj.core.util.Lists.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventServiceTest {

    @Autowired
    private EventService eventService;

    @Autowired
    private CouponRepository couponRepository;

    @Test
    @Order(1)
    void 상품_할인_이벤트를_등록한다() {
        Event newEvent = Event.builder()
                .name("아우터 20% 할인")
                .amount(0)
                .ratio(20)
                .type(EventType.DISCOUNT_PRODUCT)
                .startAt(LocalDate.of(2021, 3, 3))
                .endAt(LocalDate.of(2021, 3, 20))
                .status(EventStatus.OFF)
                .build();
        List<Id<ItemDisplay, String>> ids = List.of(
                Id.of(ItemDisplay.class, "6bdbf6eea40b425caae4410895ca4809")
        );
        Event saved = eventService.save(newEvent, ids, emptySet());
        assertThat(saved, is(notNullValue()));
        log.info("Saved Event: {}", saved);
        log.info("Saved Event Items: {}", saved.getItems());
    }


    @Test
    @Order(2)
    void 쿠폰_지급_이벤트를_등록한다() {
        Event newEvent = Event.builder()
                .name("BRONZE, SILVER 대상 금액 쿠폰")
                .amount(0)
                .ratio(20)
                .type(EventType.COUPON)
                .startAt(LocalDate.of(2021, 3, 3))
                .endAt(LocalDate.of(2021, 3, 20))
                .status(EventStatus.OFF)
                .build();
        Set<Rating> targets = Set.of(Rating.BRONZE, Rating.SILVER);
        Event saved = eventService.save(newEvent, emptyList(), targets);
        assertThat(saved, is(notNullValue()));
        log.info("Saved Event: {}", saved);
        log.info("Saved Event Targets: {}", saved.getTargets());
    }

    @Test
    void 테스트() {
        List<CouponProject> results = couponRepository.test(EventType.COUPON, EventStatus.ON, LocalDate.now());
        for(CouponProject c : results){
            log.info("Result: eventSeq={}, userId={}", c.getEvent().getEvent().getSeq(), c.getMember().getUserId());
        }
    }

    @Test
    void 테스트2() {
        Set<Coupon> results = couponRepository.findExpireTargets(LocalDate.now().plusDays(1));
        for(Coupon c : results){
            log.info("Result: eventSeq={}, userId={}", c.getEvent().getEvent().getSeq(), c.getMember().getUserId());
        }
    }
}
