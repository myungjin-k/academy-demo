package my.myungjin.academyDemo.configure.batch;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.order.Delivery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;

@RequiredArgsConstructor
@Configuration
public class DeliveryStatusJobConfigure {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 4;

    @Bean
    public Job deliveryStatusJob(){
        return jobBuilderFactory.get("deliveryStatusJob")
                .preventRestart()
                .start(deliveryStatusJobStep())
                .build();
    }

    @Bean
    public Step deliveryStatusJobStep(){
        return stepBuilderFactory.get("deliveryStatusStep")
                .<Delivery, Delivery> chunk(chunkSize)
                .reader(deliveryStatusReader())
                .processor(deliveryStatusProcessor())
                .writer(deliveryStatusWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Delivery> deliveryStatusReader(){
        return new JpaPagingItemReaderBuilder<Delivery>()
                .name("deliveryStatusReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select d from Delivery d where d.createAt < dateadd('hour', -1, current_timestamp) and d.status = 1")
                .build();
    }

    @Bean
    public ItemProcessor<Delivery, Delivery> deliveryStatusProcessor(){
        return Delivery::checkDelivery;
    }

    @Bean
    public JpaItemWriter<Delivery> deliveryStatusWriter(){
        return new JpaItemWriterBuilder<Delivery>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }




}
