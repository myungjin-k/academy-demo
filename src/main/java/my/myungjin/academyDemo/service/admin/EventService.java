package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
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
        Event saved = save(newEvent);
        return save(saveItems(saved, itemIds));
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

    @Transactional(readOnly = true)
    public List<Event> search(String eventStatus){
        return ((ArrayList<Event>) eventRepository.findAll(EventPredicate.searchByStatus(eventStatus)))
                .stream()
                .peek(event -> event.setItems(eventItemRepository.findByEventSeq(event.getSeq())))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Event findBySeqWithDetail(@Valid  Id<Event, Long> eventSeq){
        return eventRepository.findById(eventSeq.value())
                .map(event -> {
                    event.setItems(eventItemRepository.findByEventSeq(event.getSeq()));
                    return event;
                }).orElseThrow( () -> new NotFoundException(Event.class, eventSeq));
    }

    @Transactional(readOnly = true)
    public List<Event> findOnEvents(){
        return eventRepository.findByStatusEquals(EventStatus.ON)
                .stream()
                .peek(event -> event.setItems(eventItemRepository.findByEventSeq(event.getSeq())))
                .collect(Collectors.toList());
    }

    private Event save(Event event){
        return eventRepository.save(event);
    }

}
