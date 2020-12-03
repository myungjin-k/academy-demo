package my.myungjin.academyDemo.service.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemMasterRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemMasterService {

    private final ItemMasterRepository itemMasterRepository;

    @Transactional(readOnly = true)
    public List<ItemMaster> findAllItems(){
        return itemMasterRepository.findAllDesc();
    }

    @Transactional(readOnly = true)
    public List<ItemMaster> findByMainCategory(@NotBlank Id<CommonCode, String> categoryId){
        return itemMasterRepository.findAllByCategoryId(categoryId.value());
    }

    @Transactional
    public ItemMaster saveItemMaster(@Valid ItemMaster newItem){
        return save(newItem);
    }

    private ItemMaster save(ItemMaster itemMaster){
        return itemMasterRepository.save(itemMaster);
    }

}
