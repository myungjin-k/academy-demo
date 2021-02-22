package my.myungjin.academyDemo.configure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.ReceivedDeliveryStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class DeliveryStatusJobConfigure{

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 4;

    @Bean
    public JobLauncherTestUtils getJobLauncherTestUtil1() {
        return new JobLauncherTestUtils() {
            @Override
            @Autowired
            public void setJob(@Qualifier("deliveryStatusJob") Job job) {
                super.setJob(job);
            }
        };
    }


    @Bean(name = "deliveryStatusJob")
    public Job deliveryStatusJob(){
        return jobBuilderFactory.get("deliveryStatusJob")
                .preventRestart()
                .start(deliveryStatusJobStep())
                .build();
    }

    @Bean
    public Step deliveryStatusJobStep(){
        return stepBuilderFactory.get("deliveryStatusStep")
                .<ReceivedDeliveryStatus, Delivery> chunk(chunkSize)
                .reader(deliveryStatusReader())
                .processor(deliveryStatusProcessor())
                .writer(deliveryStatusWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<ReceivedDeliveryStatus> deliveryStatusReader(){
        return new JpaPagingItemReaderBuilder<ReceivedDeliveryStatus>()
                .name("deliveryStatusReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select d from ReceivedDeliveryStatus d where d.createAt >= dateadd('day', -1, current_timestamp) and d.applyYn = 'N' order by d.createAt")
                .build();
    }

    @Bean
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
    public JpaItemWriter<Delivery> deliveryStatusWriter(){
        return new JpaItemWriterBuilder<Delivery>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }




}
