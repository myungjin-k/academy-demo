package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.event.Event;
import my.myungjin.academyDemo.domain.member.Rating;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@ToString
@Getter
public class EventDetailResponse {

    private long seq;

    private String name;

    private String type;

    private int status;

    private Set<String> ratings;

    private int minPurchaseAmount;

    private int amount;

    private int ratio;

    private String startAt;

    private String endAt;

    private String createAt;

    private List<EventItemResponse> items;

    public EventDetailResponse(Event event) {
        this.seq = event.getSeq();
        this.name = event.getName();
        this.type = event.getType().getValue();
        this.status = event.getStatus().getValue();
        this.ratings = event.getTargets()
                .stream()
                .map(t -> t.getRating().getValue())
                .collect(Collectors.toSet());
        this.amount = event.getAmount();
        this.ratio = event.getRatio();
        this.minPurchaseAmount = event.getMinPurchaseAmount();
        this.startAt = event.getStartAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.endAt = event.getEndAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.createAt = event.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss"));
        this.items = event.getItems()
                .stream()
                .map(EventItemResponse::new)
                .collect(Collectors.toList());

    }
}
