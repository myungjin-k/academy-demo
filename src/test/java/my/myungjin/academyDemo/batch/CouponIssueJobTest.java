package my.myungjin.academyDemo.batch;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.order.ReceivedDeliveryStatusRepository;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.Assert.assertEquals;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class CouponIssueJobTest {

    @Qualifier("getJobLauncherTestUtil3")
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Test
    public void 쿠폰을_지급한다() throws Exception{
        LocalDateTime dateTime = LocalDateTime.now();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("createAt", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addString("today", dateTime.toLocalDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .toJobParameters();
        log.info("# Job parameter: (createAt={})", jobParameters.getString("createAt"));
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

    }

}
