package my.myungjin.academyDemo.configure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.order.DeliveryStatus;
import my.myungjin.academyDemo.domain.order.TopSeller;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TobSellerJobConfigure {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 5;


    @Bean
    public JobLauncherTestUtils getJobLauncherTestUtil2() {
        return new JobLauncherTestUtils() {
            @Override
            @Autowired
            public void setJob(@Qualifier("topSellerJob") Job job) {
                super.setJob(job);
            }
        };
    }

    @Bean
    public Job topSellerJob(){
        return jobBuilderFactory.get("topSellerJob")
                .preventRestart()
                .start(topSellerJobStep())
                .build();
    }

    @Bean
    public Step topSellerJobStep(){
        return stepBuilderFactory.get("topSellerStep")
                .<ItemDisplay, TopSeller> chunk(chunkSize)
                .reader(topSellerReader())
                .processor(topSellerProcessor())
                .writer(topSellerWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<ItemDisplay> topSellerReader(){
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("status", DeliveryStatus.DELIVERED);
        parameters.put("createAt", LocalDate.now().minusDays(1).atStartOfDay());
        return new JpaPagingItemReaderBuilder<ItemDisplay>()
                .name("topSellerReader")
                .pageSize(chunkSize)
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(parameters)
                .maxItemCount(5)
                .queryString(
                        "select item.itemOption.itemDisplay " +
                        "from OrderItem item " +
                        "where item.deliveryItem.delivery.status = :status and item.createAt >= :createAt " +
                        "group by item.itemOption.itemDisplay " +
                        "order by sum(item.count)"
                ).build();
    }

    @Bean
    public ItemProcessor<ItemDisplay, TopSeller> topSellerProcessor(){
        return item -> {
            log.info("# item : {}", item.getItemDisplayName());
            return new TopSeller(item);
        };
    }


    @Bean
    public JpaItemWriter<TopSeller> topSellerWriter() {
        JpaItemWriter<TopSeller> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
