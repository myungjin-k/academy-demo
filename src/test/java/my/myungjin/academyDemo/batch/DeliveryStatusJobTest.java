package my.myungjin.academyDemo.batch;

import my.myungjin.academyDemo.domain.order.DeliveryRepository;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DeliveryStatusJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private DeliveryRepository deliveryRepository;

    @Test
    public void 배송_상태를_업데이트한다() throws Exception{
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(0,
                deliveryRepository.findByCreateAtBeforeAndStatusIs(
                            LocalDateTime.now().minusHours(1), DeliveryStatus.REQUESTED).size()
        );

    }

}
