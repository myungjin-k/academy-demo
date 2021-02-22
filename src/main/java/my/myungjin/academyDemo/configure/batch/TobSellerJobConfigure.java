package my.myungjin.academyDemo.configure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.order.TopSeller;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.batch.item.database.orm.JpaNativeQueryProvider;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TobSellerJobConfigure {

    private final String JOB_NAME = "topSeller";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 5;

    @Qualifier(JOB_NAME + "JobParameter")
    private final CreateJobParameter createJobParameter;

    @Bean(JOB_NAME + "JobParameter")
    @JobScope
    public CreateJobParameter jobParameter(){
        return new CreateJobParameter();
    }


    @Bean
    public JobLauncherTestUtils getJobLauncherTestUtil2() {
        return new JobLauncherTestUtils() {
            @Override
            @Autowired
            public void setJob(@Qualifier(JOB_NAME + "Job") Job job) {
                super.setJob(job);
            }
        };
    }

    @Bean
    public Job topSellerJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME + "Job")
                .preventRestart()
                .start(topSellerStep())
                .build();
    }

    @Bean
    @JobScope
    public Step topSellerStep() throws Exception {
        return stepBuilderFactory.get(JOB_NAME + "Step")
                .<ItemDisplay, TopSeller> chunk(chunkSize)
                .reader(topSellerReader())
                .processor(topSellerProcessor())
                .writer(topSellerWriter())
                .build();
    }

    @Bean
    @StepScope
    protected JpaPagingItemReader<ItemDisplay> topSellerReader() throws Exception {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("createAt", jobParameter().getCreateAt());
        log.info("# createAt: {}", jobParameter().getCreateAt());
        String query = "select id.*\n" +
                "  from (\n" +
                "\t\tselect id.id\n" +
                "\t\t  from delivery_item di\n" +
                "\t\t\t   right outer join item_display_option ido\n" +
                "\t\t\t\t\t on ido.id = di.item_id\n" +
                "\t\t\t   right outer join item_display id\n" +
                "\t\t\t\t\t on id.id = ido.display_id\n" +
                "\t\t\t   inner join delivery d\n" +
                "\t\t\t\t\t on d.id = di.delivery_id\n" +
                "\t\t where (d.status = 4 or d.status is null) and di.create_at > :createAt\n" +
                "\t\t group by id.id\n" +
                "\t\t order by sum(di.count)\n" +
                ") t\n" +
                "right outer join item_display id\n" +
                "      on id.id = t.id\n";
        //creating a native query provider as it would be created in configuration
        JpaNativeQueryProvider<ItemDisplay> queryProvider= new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(query);
        queryProvider.setEntityClass(ItemDisplay.class);
        queryProvider.afterPropertiesSet();

        return new JpaPagingItemReaderBuilder<ItemDisplay>()
                .name(JOB_NAME + "Reader")
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(parameters)
                .queryProvider(queryProvider)
                .pageSize(chunkSize)
                .maxItemCount(chunkSize)
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<ItemDisplay, TopSeller> topSellerProcessor(){
        return item -> {
            log.info("# item : {}", item.getItemDisplayName());
            return new TopSeller(item);
        };
    }


    @Bean
    @StepScope
    public JpaItemWriter<TopSeller> topSellerWriter() {
        JpaItemWriter<TopSeller> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
