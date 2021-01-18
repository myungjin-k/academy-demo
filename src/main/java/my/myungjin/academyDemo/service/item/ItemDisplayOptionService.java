package my.myungjin.academyDemo.service.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayOptionRepository;
import my.myungjin.academyDemo.domain.item.ItemDisplayRepository;
import my.myungjin.academyDemo.domain.item.ItemStatus;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@Service
public class ItemDisplayOptionService {

    private final ItemDisplayOptionRepository itemDisplayOptionRepository;

    private final ItemDisplayRepository itemDisplayRepository;


    @Transactional(readOnly = true)
    public Page<ItemDisplay.ItemDisplayOption> findAllByMasterId(Id<ItemDisplay, String> itemDisplayId, Pageable pageable){
        return findDisplay(itemDisplayId)
                .map(itemDisplay -> itemDisplayOptionRepository.findAllByItemDisplay(itemDisplay, pageable))
                .orElse(Page.empty());
    }

    @Transactional(readOnly = true)
    public List<ItemDisplay.ItemDisplayOption> findAllByMasterId(Id<ItemDisplay, String> itemDisplayId){
        return findDisplay(itemDisplayId)
                .map(itemDisplayOptionRepository::findAllByItemDisplay)
                .orElse(emptyList());
    }

    @Transactional
    public ItemDisplay.ItemDisplayOption add(@Valid Id<ItemDisplay, String> itemDisplayId, @Valid ItemDisplay.ItemDisplayOption newOption){
        return findDisplay(itemDisplayId)
                .map(itemDisplay -> {
                    itemDisplay.addOption(newOption);
                    return save(newOption);
                }).orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemDisplayId));
    }

    @Transactional
    public ItemDisplay.ItemDisplayOption deleteById(@Valid Id<ItemDisplay.ItemDisplayOption, String> itemDisplayOptionId){
        return findById(itemDisplayOptionId)
                .map(itemDisplayOption -> {
                    itemDisplayOptionRepository.deleteById(itemDisplayOptionId.value());
                    return itemDisplayOption;
                }).orElseThrow(() -> new NotFoundException(ItemDisplay.ItemDisplayOption.class, itemDisplayOptionId));
    }

    @Transactional
    public ItemDisplay.ItemDisplayOption modify(@Valid Id<ItemDisplay.ItemDisplayOption, String> itemDisplayOptionId,
                                                @NotBlank String color, @NotBlank String size, @NotNull ItemStatus status){
        return findById(itemDisplayOptionId)
                .map(itemDisplayOption -> {
                    itemDisplayOption.modify(color, size, status);
                    return save(itemDisplayOption);
                }).orElseThrow(() -> new NotFoundException(ItemDisplay.ItemDisplayOption.class, itemDisplayOptionId));
    }

    private Optional<ItemDisplay> findDisplay(Id<ItemDisplay, String> itemDisplayId){
        return itemDisplayRepository.findById(itemDisplayId.value());
    }

    private Optional<ItemDisplay.ItemDisplayOption> findById(Id<ItemDisplay.ItemDisplayOption, String> optionId){
        return itemDisplayOptionRepository.findById(optionId.value());
    }

    private ItemDisplay.ItemDisplayOption save(ItemDisplay.ItemDisplayOption displayOption){
        return itemDisplayOptionRepository.save(displayOption);
    }

}
