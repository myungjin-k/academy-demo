package my.myungjin.academyDemo.batch;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.AcademyDemoApplication;
import my.myungjin.academyDemo.domain.order.TopSeller;
import my.myungjin.academyDemo.domain.order.TopSellerRepository;
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
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = AcademyDemoApplication.class)
@SpringBootTest
public class TopSellerJobTest {

    @Qualifier("getJobLauncherTestUtil2")
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    private TopSellerRepository topSellerRepository;

    @Test
    public void 상위_판매_상품을_저장한다() throws Exception{

        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        JobParameters jobParameters = new JobParametersBuilder()
            .addString("createAt", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
            .addString("today", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
            .toJobParameters();

        log.info("# Job parameter: (createAt={})", jobParameters.getString("createAt"));
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);
        assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
        List<TopSeller> result =  topSellerRepository.findByCreateAtAfter(dateTime);
        assertNotEquals(0, result.size());
        for(TopSeller item : result){
            log.info("Top Seller Item: {}", item.getItem().getItemDisplayName());
        }
    }

}
