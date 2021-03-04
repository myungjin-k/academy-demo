package my.myungjin.academyDemo.batch;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.Event;
import my.myungjin.academyDemo.domain.event.EventStatus;
import my.myungjin.academyDemo.domain.event.EventType;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.service.admin.EventService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static java.time.LocalDateTime.now;
import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class EventJobTest {

    @Qualifier("getJobLauncherTestUtil3")
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private EventService eventService;

    @Before
    public void setup() {
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
        eventService.save(newEvent, ids);
    }

    @Test
    public void 이벤트_가격을_적용한다() throws Exception{
        LocalDate today = LocalDate.now();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("createAt", now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addString("today", today.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .toJobParameters();
        log.info("# Job parameter: (today={})", jobParameters.getString("today"));
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        List<Event> events = eventService.findOnEvents();
        //assertThat(events.size(), greaterThan(0));
        for(Event e : events){
            log.info("## On Event Items: {}" , e.getItems());
        }

    }

}
