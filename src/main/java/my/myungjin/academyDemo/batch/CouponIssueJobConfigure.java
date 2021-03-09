package my.myungjin.academyDemo.batch;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.event.Coupon;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.LinkedHashMap;
import java.util.Map;

import static my.myungjin.academyDemo.util.Util.timestampOf;

@Slf4j
@Configuration
public class CouponIssueJobConfigure {

    private final String JOB_NAME = "couponIssue";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 100;

    private final CreateJobParameter jobParameter;

    public CouponIssueJobConfigure(JobBuilderFactory jobBuilderFactory,
                                   StepBuilderFactory stepBuilderFactory,
                                   EntityManagerFactory entityManagerFactory,
                                   @Qualifier(JOB_NAME + "JobParameter") CreateJobParameter createJobParameter) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.entityManagerFactory = entityManagerFactory;
        this.jobParameter = createJobParameter;
    }

    @Qualifier(JOB_NAME + "JobParameter")
    @Bean(JOB_NAME + "JobParameter")
    @JobScope
    public CreateJobParameter jobParameter(@Value("#{jobParameters[createAt]}") String createAt,
                                           @Value("#{jobParameters[today]}") String today){
        return new CreateJobParameter(createAt, today);
    }

    @Bean
    public JobLauncherTestUtils getJobLauncherTestUtil3() {
        return new JobLauncherTestUtils() {
            @Override
            @Autowired
            public void setJob(@Qualifier(JOB_NAME + "Job") Job job) {
                super.setJob(job);
            }
        };
    }

    @Bean
    public Job couponIssueJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME + "Job")
                .preventRestart()
                .start(couponIssueStep())
                .build();
    }

    @Bean
    //@JobScope
    public Step couponIssueStep() throws Exception {
        return stepBuilderFactory.get(JOB_NAME + "Step2")
                .<Coupon, Coupon> chunk(chunkSize)
                .reader(couponIssueReader())
                .processor(couponIssueProcessor())
                .writer(couponIssueWriter())
                .build();
    }

    @Bean
    @StepScope
    protected JpaPagingItemReader<Coupon> couponIssueReader() throws Exception {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("now", timestampOf(jobParameter.getCreateAt()));
        log.info("# createAt: {}", parameters.get("createAt"));
        String query = "select m.id as member_id," +
                "'N' as expired_yn, " +
                "'N' as used_yn, " +
                "e.seq as event_seq, "+
                "current_timestamp as create_at, " +
                "null as update_at" +
                "  from event e, member m" +
                "  where e.type = 'C'" +
                "  and e.status = '1' " +
                "  and e.start_at <= :now " +
                "  and e.rating like '%' || m.rating || '%'";
        //creating a native query provider as it  would be created in configuration
        JpaNativeQueryProvider<Coupon> queryProvider= new JpaNativeQueryProvider<>();
        queryProvider.setSqlQuery(query);
        queryProvider.setEntityClass(Coupon.class);
        queryProvider.afterPropertiesSet();

        return new JpaPagingItemReaderBuilder<Coupon>()
                .name(JOB_NAME + "Reader")
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(parameters)
                .queryProvider(queryProvider)
                .pageSize(chunkSize)
                .maxItemCount(chunkSize)
                .build();
    }

    @Bean
    public ItemProcessor<Coupon, Coupon> couponIssueProcessor(){
        return coupon -> {
            log.info("# coupon : {}", coupon);
            return coupon;
        };
    }


    @Bean
    public JpaItemWriter<Coupon> couponIssueWriter() {
        JpaItemWriter<Coupon> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
