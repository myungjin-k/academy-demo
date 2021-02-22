package my.myungjin.academyDemo.batch;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.order.TopSeller;
import my.myungjin.academyDemo.domain.order.TopSellerRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBootTest
public class TopSellerJobTest {

    @Qualifier("getJobLauncherTestUtil2")
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private TopSellerRepository topSellerRepository;

    @Test
    public void 상위_판매_상품을_저장한다() throws Exception{
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();
        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        List<TopSeller> result =  topSellerRepository.findByCreateAtAfter(dateTime);
        assertNotEquals(0, result.size());
        for(TopSeller item : result){
            log.info("Top Seller Item: {}", item.getItem().getItemDisplayName());
        }
    }

}
