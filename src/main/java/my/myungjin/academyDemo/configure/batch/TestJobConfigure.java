package my.myungjin.academyDemo.configure.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableBatchProcessing // spring boot starter에 미리 정의된 설정 실행
@Configuration
public class TestJobConfigure {

    // TODO 베스트 셀러 배치 프로그램 생성
    // 지난 1일 동안 /  배송완료 주문 / 주문 수량 내림차순으로 조회
    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }

}
