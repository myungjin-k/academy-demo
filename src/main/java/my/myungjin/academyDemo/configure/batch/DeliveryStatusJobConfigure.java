package my.myungjin.academyDemo.configure.batch;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.util.Collections;

@RequiredArgsConstructor
@Configuration
public class DeliveryStatusJobConfigure {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final int chunkSize = 10;

    private final DeliveryRepository deliveryRepository;

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
    public RepositoryItemReader<Delivery> deliveryStatusReader(){
        RepositoryItemReader<Delivery> reader = new RepositoryItemReader<>();
        reader.setRepository(deliveryRepository);
        reader.setMethodName("findByCreateAtBeforeAndStatusIs");
        reader.setSort(Collections.singletonMap("createAt", Sort.Direction.ASC));
        return reader;
    }

    public ItemProcessor<Delivery, Delivery> deliveryStatusProcessor(){
        return Delivery::checkDelivery;
    }

    public ItemWriter<Delivery> deliveryStatusWriter(){
        return deliveryRepository::saveAll;
    }




}
