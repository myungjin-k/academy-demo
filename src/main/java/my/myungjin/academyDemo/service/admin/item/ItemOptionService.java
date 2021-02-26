package my.myungjin.academyDemo.service.admin.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.*;
import my.myungjin.academyDemo.error.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;

@RequiredArgsConstructor
@Service
public class ItemOptionService {

    private final ItemOptionRepository itemOptionRepository;

    private final ItemMasterRepository itemMasterRepository;

    private Logger log = LoggerFactory.getLogger(getClass());

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
    public List<ItemMaster.ItemOption> addList(@Valid Id<ItemMaster, String> itemMasterId, List<ItemMaster.ItemOption> newOptions){
        ItemMaster master = findMaster(itemMasterId).orElseThrow(() -> new NotFoundException(ItemMaster.class, itemMasterId));
        return newOptions.stream()
                .filter(newOption -> {
                    if(!findAllByColorOrSize(itemMasterId.value(), newOption.getColor(), newOption.getSize()).isEmpty()){
                        log.warn("Duplicated Option: {}", newOption);
                        return false;
                    }
                    return true;
                }).map(itemOption -> {
                    itemOption.setItemMaster(master);
                    return save(itemOption);
                }).collect(Collectors.toList());
    }

    @Transactional
    public ItemMaster.ItemOption add(@Valid Id<ItemMaster, String> itemMasterId, @Valid ItemMaster.ItemOption newOption){
        if(!findAllByColorOrSize(itemMasterId.value(), newOption.getColor(), newOption.getSize()).isEmpty())
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
    public List<ItemMaster.ItemOption> modifyColor(@Valid Id<ItemMaster, String> itemMasterId, @NotBlank String color, @NotBlank String newColor){
        ItemMaster master = findMaster(itemMasterId)
                .orElseThrow(() -> new NotFoundException(ItemMaster.class, itemMasterId));
        if(!findAllByColorOrSize(master.getId(), newColor, null).isEmpty())
            throw new IllegalArgumentException("duplicated option=" + newColor);
        return findAllByColorOrSize(master.getId(), color, null).stream()
                .map(itemOption -> {
                    itemOption.modify(newColor, itemOption.getSize());
                    return save(itemOption);
                }).collect(Collectors.toList());
    }

    @Transactional
    public List<ItemMaster.ItemOption> modifySize(@Valid Id<ItemMaster, String> itemMasterId, @NotBlank String size, @NotBlank String newSize){
        ItemMaster master = findMaster(itemMasterId)
                .orElseThrow(() -> new NotFoundException(ItemMaster.class, itemMasterId));
        if(!findAllByColorOrSize(master.getId(), newSize, null).isEmpty())
            throw new IllegalArgumentException("duplicated option=" + newSize);
        return findAllByColorOrSize(master.getId(), size, null).stream()
                .map(itemOption -> {
                    itemOption.modify(newSize, itemOption.getSize());
                    return save(itemOption);
                }).collect(Collectors.toList());
    }

    @Transactional
    public ItemMaster.ItemOption modify(@Valid Id<ItemMaster.ItemOption, String> itemOptionId, @NotBlank String color, @NotBlank String size){
        return findById(itemOptionId)
                .map(itemOption -> {
                    if(!findAllByColorOrSize(itemOption.getItemMaster().getId(), color, size).isEmpty())
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

    private List<ItemMaster.ItemOption> findAllByColorOrSize(String masterId, String color, String size){
        return (List<ItemMaster.ItemOption>) itemOptionRepository.findAll(ItemOptionPredicate.search(masterId, color, size));
    }

}
