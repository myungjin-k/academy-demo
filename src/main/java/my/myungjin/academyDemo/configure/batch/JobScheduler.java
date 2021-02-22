package my.myungjin.academyDemo.configure.batch;

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

@Slf4j
@RequiredArgsConstructor
@Component
public class JobScheduler {

    private final JobLauncher jobLauncher;

    private final DeliveryStatusJobConfigure deliveryStatusJobConfigure;

    private final TobSellerJobConfigure tobSellerJobConfigure;

    @Scheduled(initialDelay = 10000, fixedDelay = 1800000)
    public void runDeliveryJob() {

        LocalDateTime dateTime = LocalDateTime.now().minusHours(1);
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("createAt", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .toJobParameters();
        try {

            jobLauncher.run(deliveryStatusJobConfigure.deliveryStatusJob(), jobParameters);

        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        }
    }

    @Scheduled(initialDelay = 3000, fixedDelay = 86400000)
    public void runTopSellerJob() {

        LocalDateTime dateTime = LocalDate.now().atStartOfDay();
        JobParameters jobParameters = new JobParametersBuilder()
                .addString("createAt", dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .toJobParameters();

        try {

            jobLauncher.run(tobSellerJobConfigure.topSellerJob(), jobParameters);

        } catch (JobExecutionAlreadyRunningException | JobInstanceAlreadyCompleteException
                | JobParametersInvalidException | org.springframework.batch.core.repository.JobRestartException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
