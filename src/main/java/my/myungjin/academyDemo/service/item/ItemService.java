package my.myungjin.academyDemo.service.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
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

    @Transactional
    public Page<ItemDisplay> findAllByCategoryGroup(@Valid Id<CommonCode, String> categoryId, Pageable pageable){
        return itemDisplayRepository.findAllByItemMasterCategoryIdOrItemMasterCategoryCodeGroupIdAndStatusEquals(
                categoryId.value(), categoryId.value(), ItemStatus.ON_SALE, pageable
        ).map(itemDisplay -> findEventAndApplyEventPrice(itemDisplay.getId()));
    }

    @Transactional
    public List<ItemDisplay> findTopSellerItems(){
        return topSellerRepository.findByCreateAtAfter(LocalDate.now().atStartOfDay())
                .stream()
                .map(TopSeller::getItem)
                .map(itemDisplay -> findEventAndApplyEventPrice(itemDisplay.getId()))
                .collect(Collectors.toList());
    }

    @Transactional
    public ItemDisplay findByIdWithOptions(@Valid Id<ItemDisplay, String> itemDisplayId){
        return itemDisplayRepository.findById(itemDisplayId.value())
                .map(itemDisplay -> {
                    List<ItemDisplayOption> options = itemDisplayOptionRepository.findAllByItemDisplay(itemDisplay);
                    if(!options.isEmpty())
                        itemDisplay.setOptions(options);
                    return itemDisplay;
                }).orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemDisplayId));
    }

    @Transactional(readOnly = true)
    public Page<ItemDisplay> searchByNameAndCreateAt(String displayName, LocalDate start, LocalDate end, Pageable pageable){
        return itemDisplayRepository.findAll(ItemDisplayPredicate.searchByNameAndDate(displayName, start, end, true), pageable);
    }

    private ItemDisplay findEventAndApplyEventPrice(String itemDisplayId) {
        final LocalDate today = LocalDate.now();

        ItemDisplay item = itemDisplayRepository.findById(itemDisplayId)
                .orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemDisplayId));

        List<Event> events = eventRepository.findByTypeAndItemsItemId(EventType.DISCOUNT_PRODUCT, item.getId())
                .stream()
                .peek(event -> log.info("Found Event: {}, today - 1: {}, today + 1: {}", event, today.minusDays(1), today.plusDays(1)))
                .filter(event -> EventStatus.ON.equals(event.getStatus())
                                && event.getStartAt().isBefore(today.plusDays(1))
                                && event.getEndAt().isAfter(today.minusDays(1))
                )
                .collect(Collectors.toList());

        int newSalePrice = item.getItemMaster().getPrice();
        if(!events.isEmpty()){
            for(Event e : events) {
                newSalePrice = newSalePrice - (int) (newSalePrice *  ((double) e.getRatio() / 100));
            }
        }
        List<ItemDisplayPriceHistory> hists = itemDisplayPriceHistoryRepository.findByItemIdOrderBySeqDesc(item.getId());
        if(hists.isEmpty() || hists.get(0).getSalePrice() != newSalePrice){
            int nextSeq = itemDisplayPriceHistoryRepository.findByItemId(item.getId()).size() + 1;
            itemDisplayPriceHistoryRepository.save(new ItemDisplayPriceHistory(newSalePrice, item, nextSeq, "EVENT"));
        }
        item.updateSalePrice(newSalePrice);
        return itemDisplayRepository.save(item);
    }

}
