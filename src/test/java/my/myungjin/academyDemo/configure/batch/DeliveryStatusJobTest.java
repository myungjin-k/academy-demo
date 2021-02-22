package my.myungjin.academyDemo.configure.batch;

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

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class DeliveryStatusJobTest {

    @Qualifier("getJobLauncherTestUtil1")
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ReceivedDeliveryStatusRepository repository;

    @Test
    public void 배송_상태를_업데이트한다() throws Exception{
        LocalDateTime dateTime = LocalDateTime.now().minusHours(1);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("createAt", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .toJobParameters();
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(0, repository.findByApplyYnEquals('N').size());

    }

}
