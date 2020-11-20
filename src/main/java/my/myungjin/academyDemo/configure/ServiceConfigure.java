package my.myungjin.academyDemo.configure;

import com.zaxxer.hikari.HikariDataSource;
import net.sf.log4jdbc.Log4jdbcProxyDataSource;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ServiceConfigure {
    @Bean
    @Profile("test")
    public DataSource testDataSource() throws SQLException {
        DataSourceBuilder factory = DataSourceBuilder
                .create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test_academy;MODE=MYSQL;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1");
        HikariDataSource dataSource = (HikariDataSource) factory.build();
        dataSource.setPoolName("TEST_H2_DB");
        dataSource.setMinimumIdle(1);
        dataSource.setMaximumPoolSize(1);
/*
        return new Log4jdbcProxyDataSource(DataSourceBuilder
                .create()
                .driverClassName("org.h2.Driver")
                .url("jdbc:h2:mem:test_academy;MODE=MYSQL;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1")
                .build());*/
        return new Log4jdbcProxyDataSource(dataSource);
    }
}
