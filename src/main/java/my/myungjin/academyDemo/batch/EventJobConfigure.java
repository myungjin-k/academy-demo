package my.myungjin.academyDemo.batch;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.event.Event;
import my.myungjin.academyDemo.domain.event.EventItem;
import my.myungjin.academyDemo.domain.event.EventStatus;
import my.myungjin.academyDemo.domain.event.EventType;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayPriceHistory;
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
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Configuration
public class EventJobConfigure {

    private final String JOB_NAME = "event";

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final EntityManagerFactory entityManagerFactory;

    private final int chunkSize = 5;

    private final CreateJobParameter jobParameter;

    public EventJobConfigure(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                             EntityManagerFactory entityManagerFactory, @Qualifier(JOB_NAME + "JobParameter") CreateJobParameter createJobParameter) {
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


    @Bean(name = JOB_NAME + "Job")
    public Job eventJob(){
        return jobBuilderFactory.get(JOB_NAME + "Job")
                .preventRestart()
                .start(eventItemJobStep())
                .next(eventItemHistoryJobStep())
                .next(eventJobStep())
                .build();
    }


    @Bean
    public Step eventItemJobStep(){
        return stepBuilderFactory.get(JOB_NAME + "ItemStep")
                .<EventItem, ItemDisplay> chunk(chunkSize)
                .reader(eventItemReader())
                .processor(eventItemProcessor())
                .writer(eventItemWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<EventItem> eventItemReader(){
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("today", jobParameter.getToday());
        parameters.put("status", EventStatus.OFF);
        return new JpaPagingItemReaderBuilder<EventItem>()
                .name(JOB_NAME + "ItemReader")
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .parameterValues(parameters)
                .queryString("select e from EventItem e where e.event.status = :status and e.event.startAt <= :today and e.event.endAt >= :today")
                .build();
    }

    private Function<EventItem, Integer> f (Event event){
        return eventItem -> {
            if(EventType.DISCOUNT_RATIO.equals(event.getType())){
                double ratio = (double) event.getRatio() / 100;
                return (int) (eventItem.getItem().getSalePrice() * ratio);
            } else if(EventType.DISCOUNT_AMOUNT.equals(event.getType()))
                return event.getAmount();
            return 0;
        };
    }

    @Bean
    public ItemProcessor<EventItem, ItemDisplay> eventItemProcessor(){
        return eventItem -> {
                int discountAmount = f(eventItem.getEvent()).apply(eventItem);
                ItemDisplay item = eventItem.getItem();
                int newPrice = item.getSalePrice() - discountAmount;
                item.updateSalePrice(newPrice);
                log.info("# Event Item {}", item);
            return item;
        };
    }

    @Bean
    public JpaItemWriter<ItemDisplay> eventItemWriter(){
        return new JpaItemWriterBuilder<ItemDisplay>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }


    @Bean
    public Step eventItemHistoryJobStep(){
        return stepBuilderFactory.get(JOB_NAME + "ItemHistoryStep")
                .<EventItem, ItemDisplayPriceHistory> chunk(chunkSize)
                .reader(eventItemHistoryReader())
                .processor(eventItemHistoryProcessor())
                .writer(eventItemHistoryWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<EventItem> eventItemHistoryReader(){
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("today", jobParameter.getToday());
        parameters.put("status", EventStatus.OFF);
        return new JpaPagingItemReaderBuilder<EventItem>()
                .name(JOB_NAME + "ItemHistoryReader")
                .parameterValues(parameters)
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select e from EventItem e where e.event.status = :status and e.event.startAt <= :today and e.event.endAt >= :today")
                .build();
    }

    @Bean
    public ItemProcessor<EventItem, ItemDisplayPriceHistory> eventItemHistoryProcessor(){
        return eventItem -> {
            ItemDisplay item = eventItem.getItem();
            int nextSeq = item.getHistories().size() + 1;
            ItemDisplayPriceHistory history = new ItemDisplayPriceHistory(item.getSalePrice(), item, nextSeq);
            log.info("# Event history: {}", history);
            return history;
        };
    }

    @Bean
    public JpaItemWriter<ItemDisplayPriceHistory> eventItemHistoryWriter(){
        return new JpaItemWriterBuilder<ItemDisplayPriceHistory>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
    @Bean
    public Step eventJobStep(){
        return stepBuilderFactory.get(JOB_NAME + "Step")
                .<Event, Event> chunk(chunkSize)
                .reader(eventReader())
                .processor(eventProcessor())
                .writer(eventWriter())
                .build();
    }

    @Bean
    @StepScope
    public JpaPagingItemReader<Event> eventReader(){
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("today", jobParameter.getToday());
        parameters.put("status", EventStatus.OFF);
        return new JpaPagingItemReaderBuilder<Event>()
                .name(JOB_NAME + "Reader")
                .parameterValues(parameters)
                .entityManagerFactory(entityManagerFactory)
                .pageSize(chunkSize)
                .queryString("select e from Event e where e.status = :status and e.startAt <= :today and e.endAt >= :today")
                .build();
    }

    @Bean
    public ItemProcessor<Event, Event> eventProcessor(){
        return event -> {
            event.on();
            return event;
        };
    }
    @Bean
    public JpaItemWriter<Event> eventWriter(){
        return new JpaItemWriterBuilder<Event>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }
}
