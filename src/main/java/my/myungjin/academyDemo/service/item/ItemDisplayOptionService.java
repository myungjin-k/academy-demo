package my.myungjin.academyDemo.service.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.*;
import my.myungjin.academyDemo.error.NotFoundException;
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
    public Id<ItemDisplay.ItemDisplayOption, String> deleteById(@Valid Id<ItemDisplay.ItemDisplayOption, String> itemDisplayOptionId){
        return findById(itemDisplayOptionId)
                .map(ItemDisplayOption -> {
                    itemDisplayOptionRepository.deleteById(itemDisplayOptionId.value());
                    return itemDisplayOptionId;
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
