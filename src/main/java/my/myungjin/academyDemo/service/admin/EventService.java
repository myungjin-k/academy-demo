package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Event> findOnEvents(){
        return eventRepository.findByStatusEquals(EventStatus.ON)
                .stream()
                .peek(event -> event.setItems(eventItemRepository.findByEventSeq(event.getSeq())))
                .collect(Collectors.toList());
    }

}
