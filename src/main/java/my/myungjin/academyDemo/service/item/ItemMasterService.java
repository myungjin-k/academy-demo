package my.myungjin.academyDemo.service.item;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.commons.AttachedFile;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemMasterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Validated
@RequiredArgsConstructor
@Service
public class ItemMasterService {

    private final ItemMasterRepository itemMasterRepository;

    private final S3Client s3Client;

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
