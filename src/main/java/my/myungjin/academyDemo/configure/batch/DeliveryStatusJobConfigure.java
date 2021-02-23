package my.myungjin.academyDemo.configure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.ReceivedDeliveryStatus;
import my.myungjin.academyDemo.util.Util;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class DeliveryStatusJobConfigure{

    private final String JOB_NAME = "deliveryStatus";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 4;

    @Bean
    public JobLauncherTestUtils getJobLauncherTestUtil1() {
        return new JobLauncherTestUtils() {
            @Override
            @Autowired
            public void setJob(@Qualifier(JOB_NAME + "Job") Job job) {
                super.setJob(job);
            }
        };
    }


    @Bean(name = JOB_NAME + "Job")
    public Job deliveryStatusJob(){
        return jobBuilderFactory.get(JOB_NAME + "Job")
                .preventRestart()
                .start(deliveryStatusJobStep())
                .build();
    }

    @Bean
    @JobScope
    public Step deliveryStatusJobStep(){
        return stepBuilderFactory.get(JOB_NAME + "Step")
                .<ReceivedDeliveryStatus, Delivery> chunk(chunkSize)
                .reader(deliveryStatusReader(""))
                .processor(deliveryStatusProcessor())
                .writer(deliveryStatusWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<ReceivedDeliveryStatus> deliveryStatusReader(
            @Value("#{jobParameters[createAt]}") String createAt
    ){
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("createAt", LocalDateTime.parse(createAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        log.info("# createAt: {}", LocalDateTime.parse(createAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        return new JpaPagingItemReaderBuilder<ReceivedDeliveryStatus>()
                .name(JOB_NAME + "Reader")
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(parameters)
                .pageSize(chunkSize)
                .queryString("select d from ReceivedDeliveryStatus d where d.createAt > :createAt and d.applyYn = 'N' order by d.createAt")
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<ReceivedDeliveryStatus, Delivery> deliveryStatusProcessor(){
        return receivedDeliveryStatus -> {
            Delivery delivery = receivedDeliveryStatus.getDelivery();
            delivery.updateStatus(receivedDeliveryStatus.getStatus());
            receivedDeliveryStatus.apply();
            delivery.addReceivedDeliveryStatus(receivedDeliveryStatus);
            log.info("# Updated Delivery : (extDeliveryId={}, status={}, applied={})", delivery.getExtDeliveryId(), delivery.getStatus(), receivedDeliveryStatus.getApplyYn());
            return delivery;
        };
    }

    @Bean
    @StepScope
    public JpaItemWriter<Delivery> deliveryStatusWriter(){
        return new JpaItemWriterBuilder<Delivery>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }




}
