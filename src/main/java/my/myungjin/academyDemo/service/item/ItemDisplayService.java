package my.myungjin.academyDemo.service.item;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.commons.AttachedFile;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.*;
import my.myungjin.academyDemo.error.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class ItemDisplayService {

    private final ItemDisplayRepository itemDisplayRepository;

    private final ItemMasterRepository itemMasterRepository;

    private final ItemDisplayOptionRepository itemDisplayOptionRepository;

    private final S3Client s3Client;

    private final String S3_BASE_PATH = "itemDisplay";

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Transactional(readOnly = true)
    public Page<ItemDisplay> findAll(Pageable pageable){
        return itemDisplayRepository.findAllByStatusEquals(ItemStatus.ON_SALE, pageable);
    }

    //TODO 카테고리별 상품 조회
    @Transactional(readOnly = true)
    public Page<ItemDisplay> findAllByCategory(@Valid Id<CommonCode, String> categoryId, Pageable pageable){
        List<ItemDisplay> list = itemMasterRepository.findAllByCategoryId(categoryId.value())
                .stream()
                .map(itemMaster -> findByItemMaster(Id.of(ItemMaster.class, itemMaster.getId()))
                        .orElseThrow(() -> new NotFoundException(ItemMaster.class, itemMaster.getId())))
                .filter(itemDisplay -> itemDisplay.getStatus().equals(ItemStatus.ON_SALE))
                .collect(Collectors.toList());
        return new PageImpl<>(list, pageable, list.size());
    }

    @Transactional(readOnly = true)
    public Optional<ItemDisplay> findByItemMaster(@Valid Id<ItemMaster, String> itemMasterId){
        return itemDisplayRepository.findByItemMasterId(itemMasterId.value());
    }

    @Transactional(readOnly = true)
    public ItemDisplay findById(@Valid Id<ItemDisplay, String> itemDisplayId){
        return itemDisplayRepository.findById(itemDisplayId.value())
                .orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemDisplayId));
    }

    @Transactional(readOnly = true)
    public ItemDisplay findByIdWithOptions(@Valid Id<ItemDisplay, String> itemDisplayId){
        return itemDisplayRepository.findById(itemDisplayId.value())
                .map(itemDisplay -> {
                    List<ItemDisplay.ItemDisplayOption> options = itemDisplayOptionRepository.findAllByItemDisplay(itemDisplay);
                    if(!options.isEmpty())
                        itemDisplay.setOptions(options);
                    return itemDisplay;
                }).orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemDisplayId));
    }

    private String uploadDetailImage(AttachedFile detailImageFile) {
        String detailImageUrl = null;
        if (detailImageFile != null) {
            String key = detailImageFile.randomName(S3_BASE_PATH, "jpeg");
            try {
                detailImageUrl = s3Client.upload(detailImageFile.inputStream(),
                        detailImageFile.length(),
                        key,
                        detailImageFile.getContentType(),
                        null);
            } catch (AmazonS3Exception e) {
                log.warn("Amazon S3 error (key: {}): {}", key, e.getMessage(), e);
            }
        }
        return detailImageUrl;
    }

    @Transactional
    public ItemDisplay save(@Valid Id<ItemMaster, String> itemMasterId, @Valid ItemDisplay newDisplay,
                            @NotNull AttachedFile detailImgFile){
        return findMaster(itemMasterId)
                .map(itemMaster -> {
                    newDisplay.setDetailImage(uploadDetailImage(detailImgFile));
                    newDisplay.setItemMaster(itemMaster);
                    return save(newDisplay);
                }).orElseThrow(() -> new NotFoundException(ItemMaster.class, itemMasterId));
    }

    private void deleteDetailImage(String detailImage){
        try{
            s3Client.delete(detailImage, S3_BASE_PATH);
        } catch (AmazonS3Exception e){
            log.warn("Amazon S3 error (key: {}): {}", detailImage, e.getMessage(), e);
        }
    }

    @Transactional
    public ItemDisplay deleteItemById(@Valid Id<ItemDisplay, String> itemDisplayId){
        ItemDisplay itemDisplay = getOne(itemDisplayId.value());
        itemDisplayRepository.delete(itemDisplay);
        deleteDetailImage(itemDisplay.getDetailImage());
        return itemDisplay;
    }

    @Transactional
    public ItemDisplay modify(@Valid Id<ItemDisplay, String> itemDisplayId, @Valid Id<ItemMaster, String> itemMasterId,
                              @Valid ItemDisplay toBeUpdated, @NotNull AttachedFile detailImgFile) {
        ItemDisplay itemDisplay = getOne(itemDisplayId.value());
        deleteDetailImage(itemDisplay.getDetailImage());
        itemDisplay.modify(toBeUpdated);
        itemDisplay.setDetailImage(uploadDetailImage(detailImgFile));
        itemDisplay.setItemMaster(itemMasterRepository.getOne(itemMasterId.value()));
        return save(itemDisplay);
    }

    @Transactional
    public Page<ItemDisplay> searchByNameAndCreateAt(String displayName, LocalDate start, LocalDate end, boolean isFromMall, Pageable pageable){
        return itemDisplayRepository.findAll(ItemDisplayPredicate.searchByNameAndDate(displayName, start, end, isFromMall), pageable);
    }

    private ItemDisplay getOne(String id){
        return itemDisplayRepository.getOne(id);
    }

    private Optional<ItemMaster> findMaster(@Valid Id<ItemMaster, String> itemMasterId){
        return itemMasterRepository.findById(itemMasterId.value());
    }

    private ItemDisplay save(ItemDisplay itemDisplay){
        return itemDisplayRepository.save(itemDisplay);
    }
}
