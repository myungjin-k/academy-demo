package my.myungjin.academyDemo.service.item;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.commons.AttachedFile;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroupRepository;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemMasterRepository;
import my.myungjin.academyDemo.domain.item.ItemOption;
import my.myungjin.academyDemo.domain.item.ItemOptionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Validated
@RequiredArgsConstructor
@Service
public class ItemMasterService {

    private final CodeGroupRepository codeGroupRepository;

    private final CommonCodeRepository commonCodeRepository;

    private final ItemMasterRepository itemMasterRepository;

    private final ItemOptionRepository itemOptionRepository;

    private final S3Client s3Client;

    private final String S3_BASE_PATH = "itemMaster";

    private Logger log = LoggerFactory.getLogger(ItemMasterService.class);



    @Transactional(readOnly = true)
    public Page<ItemMaster> findAllItems(Pageable pageable){
        return itemMasterRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public List<ItemMaster> findByCategory(@Valid Id<CommonCode, String> categoryId){
        return itemMasterRepository.findAllByCategoryId(categoryId.value());
    }

    private String uploadThumbnail(AttachedFile thumbnailFile) {
        String thumbnailUrl = null;
        if (thumbnailFile != null) {
            String key = thumbnailFile.randomName(S3_BASE_PATH, "jpeg");
            try {
                thumbnailUrl = s3Client.upload(thumbnailFile.inputStream(),
                        thumbnailFile.length(),
                        key,
                        thumbnailFile.getContentType(),
                        null);
            } catch (AmazonS3Exception e) {
                log.warn("Amazon S3 error (key: {}): {}", key, e.getMessage(), e);
            }
        }
        return thumbnailUrl;
    }

    @Transactional
    public ItemMaster saveItem(@Valid Id<CommonCode, String> categoryId, @Valid ItemMaster newItem,
                               @NotNull AttachedFile thumbnailFile, @NotNull List<ItemOption> itemOptions){
        ItemMaster master = saveItemMaster(categoryId, newItem, thumbnailFile);
        for(ItemOption itemOption : itemOptions){
            itemOption.setItemMaster(master);
            ItemOption saved = save(itemOption);
            master.addOption(saved);
        }
        return master;
    }

    public ItemMaster saveItemMaster(@Valid Id<CommonCode, String> categoryId, @Valid ItemMaster newItem, @NotNull AttachedFile thumbnailFile) {
        newItem.setThumbnail(uploadThumbnail(thumbnailFile));
        newItem.setCategory(commonCodeRepository.getOne(categoryId.value()));
        return save(newItem);
    }

    private void deleteThumbnail(String thumbnail){
        try{
            s3Client.delete(thumbnail, S3_BASE_PATH);
        } catch (AmazonS3Exception e){
            log.warn("Amazon S3 error (key: {}): {}", thumbnail, e.getMessage(), e);
        }
    }

    @Transactional
    public ItemMaster deleteItemMasterById(@Valid Id<ItemMaster, String> itemMasterId){
        ItemMaster itemMaster = getOne(itemMasterId.value());
        itemMasterRepository.delete(itemMaster);
        deleteThumbnail(itemMaster.getThumbnail());
        return itemMaster;
    }

    @Transactional
    public ItemMaster modifyItemMaster(@Valid Id<ItemMaster, String> itemMasterId, @Valid Id<CommonCode, String> categoryId, @NotBlank String itemName,
                                       @NotNull int price, @NotNull AttachedFile thumbnailFile) {
        ItemMaster itemMaster = getOne(itemMasterId.value());
        deleteThumbnail(itemMaster.getThumbnail());
        itemMaster.modify(itemName, price);
        itemMaster.setThumbnail(uploadThumbnail(thumbnailFile));
        itemMaster.setCategory(commonCodeRepository.getOne(categoryId.value()));
        return save(itemMaster);
    }

    @Transactional(readOnly = true)
    public Optional<ItemMaster> findById(Id<ItemMaster, String> itemMasterId){
        return itemMasterRepository.findById(itemMasterId.value());
    }

    @Transactional(readOnly = true)
    public Iterable<ItemMaster> search(String itemName, LocalDate start, LocalDate end){
        return itemMasterRepository.findAll(ItemMasterPredicate.search(itemName, start, end));
    }

    @Transactional(readOnly = true)
    public List<CommonCode> searchCategoryByNameKor(@NotBlank String nameKor){
        return commonCodeRepository.searchByGroupCodeAndNameKor("C", nameKor);
    }

    private ItemMaster getOne(String id){
        return itemMasterRepository.getOne(id);
    }

    private ItemMaster save(ItemMaster itemMaster){
        return itemMasterRepository.save(itemMaster);
    }

    private ItemOption save(ItemOption itemOption) {
        return itemOptionRepository.save(itemOption);
    }
}
