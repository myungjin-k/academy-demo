package my.myungjin.academyDemo.service.admin.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.*;
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
    public Page<ItemDisplayOption> findAllByMasterId(Id<ItemDisplay, String> itemDisplayId, Pageable pageable){
        return findDisplay(itemDisplayId)
                .map(itemDisplay -> itemDisplayOptionRepository.findAllByItemDisplay(itemDisplay, pageable))
                .orElse(Page.empty());
    }

    @Transactional(readOnly = true)
    public List<ItemDisplayOption> findAllByMasterId(Id<ItemDisplay, String> itemDisplayId){
        return findDisplay(itemDisplayId)
                .map(itemDisplayOptionRepository::findAllByItemDisplay)
                .orElse(emptyList());
    }

    @Transactional
    public ItemDisplayOption add(@Valid Id<ItemDisplay, String> itemDisplayId, @Valid ItemDisplayOption newOption){
        return findDisplay(itemDisplayId)
                .map(itemDisplay -> {
                    itemDisplay.addOption(newOption);
                    return save(newOption);
                }).orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemDisplayId));
    }

    @Transactional
    public ItemDisplayOption deleteById(@Valid Id<ItemDisplayOption, String> itemDisplayOptionId){
        return findById(itemDisplayOptionId)
                .map(itemDisplayOption -> {
                    itemDisplayOptionRepository.deleteById(itemDisplayOptionId.value());
                    return itemDisplayOption;
                }).orElseThrow(() -> new NotFoundException(ItemDisplayOption.class, itemDisplayOptionId));
    }

    @Transactional
    public ItemDisplayOption modify(@Valid Id<ItemDisplayOption, String> itemDisplayOptionId,
                                                @NotBlank String color, @NotBlank String size, @NotNull ItemStatus status){
        return findById(itemDisplayOptionId)
                .map(itemDisplayOption -> {
                    itemDisplayOption.modify(color, size, status);
                    return save(itemDisplayOption);
                }).orElseThrow(() -> new NotFoundException(ItemDisplayOption.class, itemDisplayOptionId));
    }

    @Transactional
    public List<ItemDisplayOption> search(Id<ItemDisplay, String> itemDisplayId, String itemName){
        return itemDisplayOptionRepository.findAllByItemDisplayIdEqualsOrItemDisplayItemDisplayNameContaining(itemDisplayId.value(), itemName);
    }

    private Optional<ItemDisplay> findDisplay(Id<ItemDisplay, String> itemDisplayId){
        return itemDisplayRepository.findById(itemDisplayId.value());
    }

    private Optional<ItemDisplayOption> findById(Id<ItemDisplayOption, String> optionId){
        return itemDisplayOptionRepository.findById(optionId.value());
    }

    private ItemDisplayOption save(ItemDisplayOption displayOption){
        return itemDisplayOptionRepository.save(displayOption);
    }

}
