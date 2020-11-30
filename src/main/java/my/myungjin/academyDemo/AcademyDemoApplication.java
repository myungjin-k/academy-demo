package my.myungjin.academyDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@EnableCaching
@SpringBootApplication
public class AcademyDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(AcademyDemoApplication.class, args);
	}

}


