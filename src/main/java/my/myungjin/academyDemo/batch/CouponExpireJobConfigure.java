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
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManagerFactory;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@Configuration
public class CouponExpireJobConfigure {

    private final String JOB_NAME = "couponExpire";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 100;

    private final CreateJobParameter jobParameter;

    public CouponExpireJobConfigure(JobBuilderFactory jobBuilderFactory,
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
    public JobLauncherTestUtils getJobLauncherTestUtil4() {
        return new JobLauncherTestUtils() {
            @Override
            @Autowired
            public void setJob(@Qualifier(JOB_NAME + "Job") Job job) {
                super.setJob(job);
            }
        };
    }

    @Bean
    public Job couponExpireJob() throws Exception {
        return jobBuilderFactory.get(JOB_NAME + "Job")
                .preventRestart()
                .start(couponExpireStep())
                .build();
    }

    @Bean
    //@JobScope
    public Step couponExpireStep() {
        return stepBuilderFactory.get(JOB_NAME + "Step")
                .<Coupon, Coupon> chunk(chunkSize)
                .reader(couponExpireReader())
                .processor(couponExpireProcessor())
                .writer(couponExpireWriter())
                .build();
    }

    @Bean
    @StepScope
    protected JpaPagingItemReader<Coupon> couponExpireReader() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("now", jobParameter.getToday());
        String queryString = "select c from Coupon c where c.event.event.endAt < :now";
        return new JpaPagingItemReaderBuilder<Coupon>()
                .name(JOB_NAME + "Reader")
                .entityManagerFactory(entityManagerFactory)
                .parameterValues(parameters)
                .queryString(queryString)
                .pageSize(chunkSize)
                .maxItemCount(chunkSize)
                .build();
    }

    @Bean
    public ItemProcessor<Coupon, Coupon> couponExpireProcessor(){
        return coupon -> {
            log.info("# Coupon: eventSeq={}, userId={}", coupon.getEvent().getEvent().getSeq(), coupon.getMember().getUserId());
            coupon.expire();
            return coupon;
        };
    }


    @Bean
    public JpaItemWriter<Coupon> couponExpireWriter() {
        JpaItemWriter<Coupon> jpaItemWriter = new JpaItemWriter<>();
        jpaItemWriter.setEntityManagerFactory(entityManagerFactory);
        return jpaItemWriter;
    }

}
