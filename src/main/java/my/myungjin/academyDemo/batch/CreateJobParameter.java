package my.myungjin.academyDemo.batch;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@NoArgsConstructor
public class CreateJobParameter {

    private LocalDateTime createAt;

    private LocalDate today;


    public CreateJobParameter(String createAt, String today) {
        this.createAt = LocalDateTime.parse(createAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.today = LocalDate.parse(today, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
