package my.myungjin.academyDemo.service.item;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.commons.AttachedFile;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemMasterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Validated
@RequiredArgsConstructor
@Service
public class ItemMasterService {

    private final ItemMasterRepository itemMasterRepository;

    private final S3Client s3Client;

    private Logger log = LoggerFactory.getLogger(ItemMasterService.class);

    @Transactional(readOnly = true)
    public List<ItemMaster> findAllItems(){
        return itemMasterRepository.findAllDesc();
    }

    @Transactional(readOnly = true)
    public List<ItemMaster> findByMainCategory(@Valid Id<CommonCode, String> categoryId){
        return itemMasterRepository.findAllByCategoryId(categoryId.value());
    }

    private String uploadThumbnail(AttachedFile thumbnailFile) {
        String thumbnailUrl = null;
        if (thumbnailFile != null) {
            String key = thumbnailFile.randomName("itemMaster", "jpeg");
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
    public ItemMaster saveItemMaster(@Valid ItemMaster newItem, @NotNull AttachedFile thumbnailFile) {
        newItem.setThumbnail(uploadThumbnail(thumbnailFile));
        return save(newItem);
    }

    private ItemMaster save(ItemMaster itemMaster){
        return itemMasterRepository.save(itemMaster);
    }

}
