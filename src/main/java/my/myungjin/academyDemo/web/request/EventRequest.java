package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.Event;
import my.myungjin.academyDemo.domain.event.EventStatus;
import my.myungjin.academyDemo.domain.event.EventType;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Rating;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ToString
@AllArgsConstructor
@Getter
public class EventRequest {

    private String name;

    private int status;

    private String type;

    private List<String> ratings;

    private int amount;

    private int minPurchaseAmount;

    private int ratio;

    private String startAt;

    private String endAt;

    private List<String> itemStringIds = new ArrayList<>();

    public Event newEvent(){
        return Event.builder()
                .name(name)
                .status(EventStatus.of(status))
                .type(EventType.of(type))
                .ratings(ratings.stream()
                        .map(Rating::valueOf)
                        .collect(Collectors.toSet()))
                .minPurchaseAmount(minPurchaseAmount)
                .amount(amount)
                .ratio(ratio)
                .startAt(LocalDate.parse(startAt, DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .endAt(LocalDate.parse(endAt, DateTimeFormatter.ofPattern("yyyy.MM.dd")))
                .build();
    }

    public List<Id<ItemDisplay, String>> toItemIds(){
        return itemStringIds.stream()
                .map(id -> Id.of(ItemDisplay.class, id))
                .collect(Collectors.toList());
    }

}
