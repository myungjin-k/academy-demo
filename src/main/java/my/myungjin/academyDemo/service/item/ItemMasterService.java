package my.myungjin.academyDemo.service.item;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.commons.AttachedFile;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemMasterPredicate;
import my.myungjin.academyDemo.domain.item.ItemMasterRepository;
import my.myungjin.academyDemo.domain.item.ItemOptionRepository;
import my.myungjin.academyDemo.error.NotFoundException;
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

import static java.util.Optional.ofNullable;

@Validated
@RequiredArgsConstructor
@Service
public class ItemMasterService {

    private final CommonCodeRepository commonCodeRepository;

    private final ItemMasterRepository itemMasterRepository;

    private final ItemOptionRepository itemOptionRepository;

    private final S3Client s3Client;

    private final String S3_BASE_PATH = "itemMaster";

    private Logger log = LoggerFactory.getLogger(ItemMasterService.class);

    @Transactional(readOnly = true)
    public Page<ItemMaster> findAllItems(Pageable pageable){
        Page<ItemMaster> res = itemMasterRepository.findAll(pageable);
        log.info("Result: {}", res.getContent().get(0).getOptions());
        return res;
    }

    @Transactional(readOnly = true)
    public List<ItemMaster> findByCategory(@Valid Id<CommonCode, String> categoryId){
        return itemMasterRepository.findAllByCategoryId(categoryId.value());
    }

    private Optional<String> uploadThumbnail(AttachedFile thumbnailFile) {
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
        return ofNullable(thumbnailUrl);
    }

    @Transactional
    public ItemMaster saveItemMaster(@Valid Id<CommonCode, String> categoryId, @Valid ItemMaster newItem, @NotNull AttachedFile thumbnailFile) {
        CommonCode category = commonCodeRepository.findById(categoryId.value())
                .orElseThrow(() -> new NotFoundException(CommonCode.class, categoryId));
        newItem.setThumbnail(uploadThumbnail(thumbnailFile).orElseThrow(() -> new IllegalArgumentException("thumbnail should not be null!")));
        newItem.setCategory(category);
        log.info("New Item Master: {}", newItem);
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
        ItemMaster itemMaster = findById(itemMasterId)
                .map(master -> {
                    itemMasterRepository.delete(master);
                    return master;
                }).orElseThrow(() -> new NotFoundException(ItemMaster.class, itemMasterId));
        deleteThumbnail(itemMaster.getThumbnail());
        return itemMaster;
    }

    @Transactional
    public ItemMaster modifyItemMaster(@Valid Id<ItemMaster, String> itemMasterId, @Valid Id<CommonCode, String> categoryId, @NotBlank String itemName,
                                       @NotNull int price, AttachedFile thumbnailFile) {
        ItemMaster itemMaster = getOne(itemMasterId.value());
        deleteThumbnail(itemMaster.getThumbnail());
        itemMaster.modify(itemName, price);
        String newThumbnail = uploadThumbnail(thumbnailFile).orElse(null);
        if(newThumbnail != null)
            itemMaster.setThumbnail(newThumbnail);
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

    private ItemMaster.ItemOption save(ItemMaster.ItemOption itemOption) {
        return itemOptionRepository.save(itemOption);
    }
}
