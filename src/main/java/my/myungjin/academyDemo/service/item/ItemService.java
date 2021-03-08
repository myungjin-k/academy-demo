package my.myungjin.academyDemo.service.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.event.*;
import my.myungjin.academyDemo.domain.item.*;
import my.myungjin.academyDemo.domain.order.TopSeller;
import my.myungjin.academyDemo.domain.order.TopSellerRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemDisplayRepository itemDisplayRepository;

    private final ItemMasterRepository itemMasterRepository;

    private final ItemDisplayOptionRepository itemDisplayOptionRepository;

    private final TopSellerRepository topSellerRepository;

    private final EventRepository eventRepository;

    private final ItemDisplayPriceHistoryRepository itemDisplayPriceHistoryRepository;

    @Transactional(readOnly = true)
    public Page<ItemDisplay> findAll(Pageable pageable){
        return itemDisplayRepository.findAllByStatusEquals(ItemStatus.ON_SALE, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ItemDisplay> findAllByCategoryGroup(@Valid Id<CommonCode, String> categoryId, Pageable pageable){
        return itemDisplayRepository.findAllByItemMasterCategoryIdOrItemMasterCategoryCodeGroupIdAndStatusEquals(
                categoryId.value(), categoryId.value(), ItemStatus.ON_SALE, pageable
        );
    }

    @Transactional(readOnly = true)
    public List<ItemDisplay> findTopSellerItems(){
        return topSellerRepository.findByCreateAtAfter(LocalDate.now().atStartOfDay())
                .stream()
                .map(TopSeller::getItem)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ItemDisplay findByIdWithOptions(@Valid Id<ItemDisplay, String> itemDisplayId){
        return itemDisplayRepository.findById(itemDisplayId.value())
                .map(itemDisplay -> {
                    ItemDisplay item = findEventAndApplyEventPrice(itemDisplayId);
                    List<ItemDisplayOption> options = itemDisplayOptionRepository.findAllByItemDisplay(item);
                    if(!options.isEmpty())
                        item.setOptions(options);
                    return item;
                }).orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemDisplayId));
    }

    @Transactional(readOnly = true)
    public Page<ItemDisplay> searchByNameAndCreateAt(String displayName, LocalDate start, LocalDate end, Pageable pageable){
        return itemDisplayRepository.findAll(ItemDisplayPredicate.searchByNameAndDate(displayName, start, end, true), pageable);
    }

    private ItemDisplay findEventAndApplyEventPrice(@Valid Id<ItemDisplay, String> itemDisplayId) {
        final LocalDate today = LocalDate.now();

        ItemDisplay item = itemDisplayRepository.findById(itemDisplayId.value())
                .orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemDisplayId));

        List<Event> events = eventRepository.findByTypeAndItemsItemId(EventType.DISCOUNT_PRODUCT, item.getId())
                .stream()
                .filter(event -> EventStatus.ON.equals(event.getStatus())
                                && event.getStartAt().isBefore(today.plusDays(1))
                                && event.getEndAt().isAfter(today.minusDays(1)))
                .collect(Collectors.toList());

        if(!events.isEmpty()){
            int eventPrice = item.getSalePrice();
            for(Event e : events) {
                eventPrice = eventPrice - (int) (eventPrice *  ((double) e.getRatio() / 100));

                String eventSeqStr = String.valueOf(e.getSeq());
                if(!itemDisplayPriceHistoryRepository.existsByRefAndItemId(eventSeqStr, item.getId())){
                    int nextSeq = itemDisplayPriceHistoryRepository.findByItemId(item.getId()).size() + 1;
                    itemDisplayPriceHistoryRepository.save(new ItemDisplayPriceHistory(eventPrice, item, nextSeq, eventSeqStr));
                    item.updateSalePrice(eventPrice);
                }
            }
        }
        return itemDisplayRepository.save(item);
    }

}
