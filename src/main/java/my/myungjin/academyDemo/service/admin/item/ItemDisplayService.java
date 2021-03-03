package my.myungjin.academyDemo.service.admin.item;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.commons.AttachedFile;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.*;
import my.myungjin.academyDemo.domain.order.TopSellerRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
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

import static java.util.Optional.ofNullable;

@RequiredArgsConstructor
@Service
public class ItemDisplayService {

    private final ItemDisplayRepository itemDisplayRepository;

    private final ItemMasterRepository itemMasterRepository;

    private final ItemDisplayOptionRepository itemDisplayOptionRepository;

    private final TopSellerRepository topSellerRepository;

    private final ItemDisplayPriceHistoryRepository itemDisplayPriceHistoryRepository;

    private final S3Client s3Client;

    private final Environment environment;

    private final String S3_BASE_PATH = "/itemDisplay";

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Transactional(readOnly = true)
    public Page<ItemDisplay> findAll(Pageable pageable){
        return itemDisplayRepository.findAllByStatusEquals(ItemStatus.ON_SALE, pageable);
    }

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

    private String getServerProfile(){
        String[] profiles = environment.getActiveProfiles();
        for(String profile : profiles){
            if(profile.equals("real"))
                return profile;
        }
        return "dev";
    }

    private Optional<String> uploadDetailImage(AttachedFile detailImageFile) {
        String detailImageUrl = null;
        if (detailImageFile != null) {
            String key = detailImageFile.randomName(getServerProfile() + S3_BASE_PATH, "jpeg");
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
        return ofNullable(detailImageUrl);
    }

    @Transactional
    public ItemDisplay save(@Valid Id<ItemMaster, String> itemMasterId, @Valid ItemDisplay newDisplay,
                            @NotNull AttachedFile detailImgFile){
        return findMaster(itemMasterId)
                .map(itemMaster -> {
                    newDisplay.setDetailImage(uploadDetailImage(detailImgFile).orElseThrow(() -> new IllegalArgumentException("detailImage should not be null")));
                    newDisplay.setItemMaster(itemMaster);
                    ItemDisplay saved = save(newDisplay);

                    //log.info("Saved Item Display: {}", newDisplay);
                    saved.setOptions(convertItemOption((List<ItemOption>) itemMaster.getOptions(), newDisplay));
                    saveHistory(saved);
                    return saved;
                }).orElseThrow(() -> new NotFoundException(ItemMaster.class, itemMasterId));
    }

    private List<ItemDisplayOption> convertItemOption(List<ItemOption> optionList, ItemDisplay itemDisplay){
        return optionList.stream()
                .map(itemOption -> {
                    ItemDisplayOption displayOption = ItemDisplayOption.builder()
                            .color(itemOption.getColor())
                            .size(itemOption.getSize())
                            .status(ItemStatus.ON_SALE)
                            .build();
                    displayOption.setItemDisplay(itemDisplay);
                    return itemDisplayOptionRepository.save(displayOption);
                }).collect(Collectors.toList());
    }

    private void deleteDetailImage(String detailImage){
        try{
            s3Client.delete(detailImage, getServerProfile() + S3_BASE_PATH);
        } catch (AmazonS3Exception e){
            log.warn("Amazon S3 error (key: {}): {}", detailImage, e.getMessage(), e);
        }
    }

    @Transactional
    public ItemDisplay deleteItemById(@Valid Id<ItemDisplay, String> itemDisplayId){
        ItemDisplay itemDisplay = findById(itemDisplayId);
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
        String newDetailImage = uploadDetailImage(detailImgFile).orElse(null);
        if(newDetailImage != null)
            itemDisplay.setDetailImage(newDetailImage);
        itemDisplay.setItemMaster(itemMasterRepository.getOne(itemMasterId.value()));
        return save(itemDisplay);
    }

    @Transactional
    public Page<ItemDisplay> searchByNameAndCreateAt(String displayName, LocalDate start, LocalDate end, Pageable pageable){
        return itemDisplayRepository.findAll(ItemDisplayPredicate.searchByNameAndDate(displayName, start, end, false), pageable);
    }

    @Transactional
    public List<ItemDisplay> discount(List<Id<ItemDisplay, String>> itemIds, double ratio){
        List<ItemDisplay> results = itemIds.stream()
                .map(itemId -> {
                    ItemDisplay item = itemDisplayRepository.findById(itemId.value())
                            .orElseThrow(() -> new NotFoundException(ItemDisplay.class, itemId));
                    item.updateSalePrice((int) (item.getItemMaster().getPrice() * (1 - ratio)));
                    return save(item);
                }).collect(Collectors.toList());
        for(ItemDisplay item : results){
            saveHistory(item);
        }
        return results;
    }

    private void saveHistory(ItemDisplay item){
        int nextSeq = itemDisplayPriceHistoryRepository.findByItemId(item.getId()).size() + 1;
        ItemDisplayPriceHistory newHistory = itemDisplayPriceHistoryRepository.save(new ItemDisplayPriceHistory(item.getSalePrice(), item, nextSeq));
        log.info("New Item History: {}", newHistory);
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
