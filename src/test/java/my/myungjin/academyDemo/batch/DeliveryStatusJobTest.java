package my.myungjin.academyDemo.batch;

import my.myungjin.academyDemo.domain.order.DeliveryRepository;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.domain.order.ReceivedDeliveryStatusRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class DeliveryStatusJobTest {

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private ReceivedDeliveryStatusRepository repository;

    @Test
    public void 배송_상태를_업데이트한다() throws Exception{
        //TODO 스케줄러
        //TODO 물류 배송정보 엔티티 -> 새로운 정보 read -> 쇼핑몰 배송정보에서 해당 정보 조회 -> 업데이트 -> 업데이트 완료된 물류 배송정보 체크
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        assertEquals(0, repository.findByApplyYnEquals('N').size());

    }

}
