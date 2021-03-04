package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.event.Event;
import my.myungjin.academyDemo.domain.event.EventStatus;

import java.time.format.DateTimeFormatter;

@ToString
@Getter
public class EventListResponse {

    private long seq;

    private String name;

    private String startAt;

    private String endAt;

    private EventStatus status;

    public EventListResponse(Event event) {
        this.seq = event.getSeq();
        this.name = event.getName();
        this.startAt = event.getStartAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.endAt = event.getEndAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.status = event.getStatus();
    }
}
