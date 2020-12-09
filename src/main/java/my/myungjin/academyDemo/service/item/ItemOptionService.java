package my.myungjin.academyDemo.service.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemMasterRepository;
import my.myungjin.academyDemo.domain.item.ItemOption;
import my.myungjin.academyDemo.domain.item.ItemOptionRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@Service
public class ItemOptionService {

    private final ItemOptionRepository itemOptionRepository;

    private final ItemMasterRepository itemMasterRepository;

    @Transactional(readOnly = true)
    public List<ItemOption> findAllByMasterId(Id<ItemMaster, String> itemMasterId){
        return findMaster(itemMasterId)
                .map(itemOptionRepository::findAllByItemMaster)
                .orElse(emptyList());
    }

    @Transactional
    public ItemOption add(@Valid Id<ItemMaster, String> itemMasterId, @Valid ItemOption newOption){
        return findMaster(itemMasterId)
                .map(itemMaster -> {
                    itemMaster.addOption(newOption);
                    return save(newOption);
                }).orElseThrow(() -> new NotFoundException(ItemMaster.class, itemMasterId));
    }

    @Transactional
    public Id<ItemOption, String> deleteById(@Valid Id<ItemOption, String> itemOptionId){
        itemOptionRepository.deleteById(itemOptionId.value());
        return itemOptionId;
    }

    @Transactional
    public ItemOption modify(@Valid Id<ItemOption, String> itemOptionId, @NotBlank String color, @NotBlank String size){
        ItemOption option = itemOptionRepository.getOne(itemOptionId.value());
        option.modify(color, size);
        return option;
    }

    private Optional<ItemMaster> findMaster(Id<ItemMaster, String> itemMasterId){
        return itemMasterRepository.findById(itemMasterId.value());
    }

    private ItemOption save(ItemOption itemOption){
        return itemOptionRepository.save(itemOption);
    }

}
