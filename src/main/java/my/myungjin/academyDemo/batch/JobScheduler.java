package my.myungjin.academyDemo.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;

@Slf4j
@RequiredArgsConstructor
@Component
public class JobScheduler {

    private final JobLauncher jobLauncher;

    private final DeliveryStatusJobConfigure deliveryStatusJobConfigure;

    private final TopSellerJobConfigure tobSellerJobConfigure;

    private final CouponIssueJobConfigure couponIssueJobConfigure;

    private final CouponExpireJobConfigure couponExpireJobConfigure;

    // TODO 배송상태 수신 로직 구현(ex 파일과 같이 외부에서 입력)
    @Scheduled(initialDelay = 10000, fixedDelay = 1800000)
    public void runDeliveryJob() {
        try {
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("createAt", now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                    .addString("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                    .toJobParameters();
            jobLauncher.run(deliveryStatusJobConfigure.deliveryStatusJob(), jobParameters);

        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(initialDelay = 30000, fixedDelay = 86400000)
    public void runTopSellerJob() {

        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("createAt", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addString("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .toJobParameters();

        try {
            log.info("# Job parameter: (createAt={})", jobParameters.getString("createAt"));
            jobLauncher.run(tobSellerJobConfigure.topSellerJob(), jobParameters);

        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Scheduled(cron = "0 0 0 * * ?")
    public void runCouponIssueJob() {

        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("createAt", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addString("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .toJobParameters();

        try {
            log.info("# Job parameter: (today={})", jobParameters.getString("today"));
            jobLauncher.run(couponIssueJobConfigure.couponIssueJob(), jobParameters);

        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void runCouponExpireJob() {

        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("createAt", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .addString("today", LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
                .toJobParameters();

        try {
            log.info("# Job parameter: (today={})", jobParameters.getString("today"));
            jobLauncher.run(couponExpireJobConfigure.couponExpireJob(), jobParameters);

        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
