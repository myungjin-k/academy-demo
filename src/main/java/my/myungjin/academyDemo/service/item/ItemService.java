package my.myungjin.academyDemo.service.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
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

    @Transactional(readOnly = true)
    public Page<ItemDisplay> findAll(Pageable pageable){
        return itemDisplayRepository.findAllByStatusEquals(ItemStatus.ON_SALE, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ItemDisplay> findAllByCategoryGroup(@Valid Id<CommonCode, String> categoryId, Pageable pageable){
        return itemDisplayRepository.findAllByItemMasterCategoryIdOrItemMasterCategoryCodeGroupIdAndStatusEquals(categoryId.value(), categoryId.value(), ItemStatus.ON_SALE, pageable);
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
                    List<ItemDisplay.ItemDisplayOption> options = itemDisplayOptionRepository.findAllByItemDisplay(itemDisplay);
                    if(!options.isEmpty())
                        itemDisplay.setOptions(options);
                    return itemDisplay;
                }).orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemDisplayId));
    }

    @Transactional
    public Page<ItemDisplay> searchByNameAndCreateAt(String displayName, LocalDate start, LocalDate end, Pageable pageable){
        return itemDisplayRepository.findAll(ItemDisplayPredicate.searchByNameAndDate(displayName, start, end, true), pageable);
    }

}
