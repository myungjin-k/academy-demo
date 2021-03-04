package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import my.myungjin.academyDemo.domain.event.EventItem;

@Getter
public class EventItemResponse {

    private String eventItemId;

    private String itemId;

    private String itemName;

    public EventItemResponse(EventItem item) {
        this.eventItemId = item.getId();
        this.itemId = item.getItem().getId();
        this.itemName = item.getItem().getItemDisplayName();
    }
}
