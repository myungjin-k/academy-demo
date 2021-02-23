package my.myungjin.academyDemo.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@NoArgsConstructor
public class CreateJobParameter {

    private LocalDateTime createAt;

    public CreateJobParameter(String createAt) {
        this.createAt = LocalDateTime.parse(createAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
