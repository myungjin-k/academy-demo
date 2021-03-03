package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.Event;
import my.myungjin.academyDemo.domain.event.EventItem;
import my.myungjin.academyDemo.domain.event.EventItemRepository;
import my.myungjin.academyDemo.domain.event.EventRepository;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventRepository eventRepository;

    private final EventItemRepository eventItemRepository;

    private final ItemDisplayRepository itemDisplayRepository;

    @Transactional
    public Event save(Event newEvent, List<Id<ItemDisplay, String>> itemIds){
        return saveItems(eventRepository.save(newEvent), itemIds);
    }
    //TODO  매일 신규 활성화 이벤트 조회 -> 연관 아이템 전시가격 수정 및 히스토리 저장
    private Event saveItems(Event event, List<Id<ItemDisplay, String>> itemIds){
        for(Id<ItemDisplay, String> itemId : itemIds){
            ItemDisplay item = itemDisplayRepository.findById(itemId.value())
                    .orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemId));
            EventItem newItem = new EventItem(event, item);
            EventItem eventItem = eventItemRepository.save(newItem);
            event.addItem(eventItem);
        }
        return event;
    }

}
