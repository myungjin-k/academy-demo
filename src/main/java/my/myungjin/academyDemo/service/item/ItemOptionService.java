package my.myungjin.academyDemo.service.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemMasterRepository;
import my.myungjin.academyDemo.domain.item.ItemOptionRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Page<ItemMaster.ItemOption> findAllByMasterIdWithPage(@Valid Id<ItemMaster, String> itemMasterId, Pageable pageable){
        return findMaster(itemMasterId)
                .map(master -> itemOptionRepository.findAllByItemMaster(master, pageable))
                .orElse(Page.empty());
    }

    @Transactional(readOnly = true)
    public List<ItemMaster.ItemOption> findAllByMasterId(Id<ItemMaster, String> itemMasterId){
        return findMaster(itemMasterId)
                .map(itemOptionRepository::findAllByItemMaster)
                .orElse(emptyList());
    }

    @Transactional
    public ItemMaster.ItemOption add(@Valid Id<ItemMaster, String> itemMasterId, @Valid ItemMaster.ItemOption newOption){
        if(existsByColorAndSize(itemMasterId.value(), newOption.getColor(), newOption.getSize()))
            throw new IllegalArgumentException("duplicated option=" + newOption.getColor() + ", " + newOption.getSize());
        return findMaster(itemMasterId)
                .map(itemMaster -> {
                    itemMaster.addOption(newOption);
                    return save(newOption);
                }).orElseThrow(() -> new NotFoundException(ItemMaster.class, itemMasterId));
    }

    @Transactional
    public ItemMaster.ItemOption deleteById(@Valid Id<ItemMaster.ItemOption, String> itemOptionId){
        return findById(itemOptionId)
                .map(itemOption -> {
                    itemOptionRepository.deleteById(itemOptionId.value());
                    return itemOption;
                }).orElseThrow(() -> new NotFoundException(ItemMaster.ItemOption.class, itemOptionId));
    }

    @Transactional
    public ItemMaster.ItemOption modify(@Valid Id<ItemMaster.ItemOption, String> itemOptionId, @NotBlank String color, @NotBlank String size){
        return findById(itemOptionId)
                .map(itemOption -> {
                    if(existsByColorAndSize(itemOption.getItemMaster().getId(), color, size))
                        throw new IllegalArgumentException("duplicated option=" + color + ", " + size);
                    itemOption.modify(color, size);
                    return save(itemOption);
                }).orElseThrow(() -> new NotFoundException(ItemMaster.ItemOption.class, itemOptionId));
    }

    private Optional<ItemMaster> findMaster(Id<ItemMaster, String> itemMasterId){
        return itemMasterRepository.findById(itemMasterId.value());
    }

    private Optional<ItemMaster.ItemOption> findById(Id<ItemMaster.ItemOption, String> optionId){
        return itemOptionRepository.findById(optionId.value());
    }

    private ItemMaster.ItemOption save(ItemMaster.ItemOption itemOption){
        return itemOptionRepository.save(itemOption);
    }

    private boolean existsByColorAndSize(String masterId, String color, String size){
        return itemOptionRepository.existsByItemMasterIdAndColorAndSize(masterId, color, size);
    }

}
