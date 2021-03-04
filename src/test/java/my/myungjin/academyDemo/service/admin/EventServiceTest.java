package my.myungjin.academyDemo.service.admin;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.Event;
import my.myungjin.academyDemo.domain.event.EventStatus;
import my.myungjin.academyDemo.domain.event.EventType;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static java.time.LocalDateTime.now;
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

    @Test
    @Order(1)
    void 이벤트를_등록한다() {
        Event newEvent = Event.builder()
                .name("아우터 20% 할인")
                .amount(0)
                .ratio(20)
                .type(EventType.DISCOUNT_RATIO)
                .startAt(LocalDate.of(2021, 3, 3))
                .endAt(LocalDate.of(2021, 3, 20))
                .createAt(now())
                .status(EventStatus.OFF)
                .build();
        List<Id<ItemDisplay, String>> ids = List.of(
                Id.of(ItemDisplay.class, "6bdbf6eea40b425caae4410895ca4809")
        );
        Event saved = eventService.save(newEvent, ids);
        assertThat(saved, is(notNullValue()));
        log.info("Saved Event: {}", saved);
        log.info("Saved Event Items: {}", saved.getItems());
    }
}
