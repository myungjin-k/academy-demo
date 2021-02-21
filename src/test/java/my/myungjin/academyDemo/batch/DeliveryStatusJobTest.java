package my.myungjin.academyDemo.batch;

import my.myungjin.academyDemo.domain.order.ReceivedDeliveryStatusRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

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
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(0, repository.findByApplyYnEquals('N').size());

    }

}
