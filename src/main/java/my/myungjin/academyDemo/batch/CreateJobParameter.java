package my.myungjin.academyDemo.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static java.time.LocalDateTime.now;

@Slf4j
@Getter
@NoArgsConstructor
public class CreateJobParameter {

    private LocalDateTime createAt;

    public CreateJobParameter(@Nullable String createAt) {
        this.createAt = createAt == null ? now() : LocalDateTime.parse(createAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
