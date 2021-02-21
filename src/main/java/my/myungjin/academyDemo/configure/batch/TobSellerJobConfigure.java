package my.myungjin.academyDemo.configure.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.domain.order.TopSeller;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.BeanPropertyRowMapper;

import javax.sql.DataSource;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class TobSellerJobConfigure {

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    private final DataSource dataSource;

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
                .<TopSeller, TopSeller> chunk(chunkSize)
                .reader(topSellerReader())
                .writer(topSellerWriter())
                .build();
    }

    @Bean
    @StepScope
    public JdbcCursorItemReader<TopSeller> topSellerReader(){
        return new JdbcCursorItemReaderBuilder<TopSeller>()
                .name("topSellerReader")
                .dataSource(dataSource)
                .rowMapper(new BeanPropertyRowMapper<>(TopSeller.class))
                .fetchSize(chunkSize)
                .sql(
                        "select t.* " +
                                "from (" +
                                "       select id.id as itemId, sum(oi.count) as saleCount " +
                                "       from order_item oi " +
                                "       join item_display_option ido on (ido.id = oi.item_id)" +
                                "       join item_display id on (id.id = ido.display_id)" +
                                "       join delivery_item di on (di.id = oi.delivery_item_id)" +
                                "       join delivery d on (d.id = di.delivery_id)" +
                                "       where d.status = 4 " +
                                "       and oi.create_at > dateadd('day', -1, current_timestamp)" +
                                "       group by id.id" +
                                ") t " +
                                "order by t.saleCount desc"
                )
                .build();
    }


    @Bean
    public ItemWriter<TopSeller> topSellerWriter(){
        return list -> {
            for(TopSeller item : list){
                log.info("Sale Item : {}", item);
            }
        };
    }

}
