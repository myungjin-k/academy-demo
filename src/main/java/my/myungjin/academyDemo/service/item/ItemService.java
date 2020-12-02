package my.myungjin.academyDemo.service.item;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemMasterRepository;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemMasterRepository itemMasterRepository;

    public List<ItemMaster> findAllItems(){
        return itemMasterRepository.findAll();
    }

    public List<ItemMaster> findByMainCategory(@NotBlank Id<CodeGroup, String> categoryId){
        return itemMasterRepository.findAllByMainCategoryId(categoryId.value());
    }

}
