package my.myungjin.academyDemo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableBatchProcessing
@EnableCaching
@SpringBootApplication
public class AcademyDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademyDemoApplication.class, args);
	}

}


