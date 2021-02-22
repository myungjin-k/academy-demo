package my.myungjin.academyDemo.configure.batch;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.util.Util;
import org.springframework.beans.factory.annotation.Value;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Getter
@NoArgsConstructor
public class CreateJobParameter {

    private Timestamp createAt;

    @Value("#{jobParameters[createAt]}")
    public void setCreateAt(String createAt) {
        this.createAt = Util.timestampOf(LocalDateTime.parse(createAt, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
    }
}
