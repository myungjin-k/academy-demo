package my.myungjin.academyDemo.web.controller;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemOption;
import my.myungjin.academyDemo.service.item.ItemDisplayService;
import my.myungjin.academyDemo.service.item.ItemMasterService;
import my.myungjin.academyDemo.service.item.ItemOptionService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.*;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/api/admin/item")
@RestController
public class ItemAdminController {

    private final ItemMasterService itemMasterService;

    private final ItemOptionService itemOptionService;

    private final ItemDisplayService itemDisplayService;

    @GetMapping("/all")
    public Response<Page<ItemMaster>> allItems(PageRequest pageRequest){
        return OK(
                itemMasterService.findAllItems(pageRequest.of())
        );
    }

    @GetMapping("/category")
    public Response<List<CommonCode>> searchCategory(@RequestParam String searchParam){
        return OK(
                itemMasterService.searchCategoryByNameKor(searchParam)
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<ItemMaster> saveMaster(
            @ModelAttribute ItemMasterRequest request,
            @RequestPart MultipartFile thumbnail
    ) throws IOException {
        return OK(
                itemMasterService.saveItemMaster(
                        Id.of(CommonCode.class,request.getCategoryId()),
                        request.newItemMaster(),
                        toAttachedFile(thumbnail)
                )
        );
    }

    @PutMapping(path = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<ItemMaster> updateMaster(
            @PathVariable String id,
            @ModelAttribute ItemMasterRequest request,
            @RequestPart MultipartFile thumbnail
    ) throws IOException {
        return OK(
                itemMasterService.modifyItemMaster(
                        Id.of(ItemMaster.class, id),
                        Id.of(CommonCode.class, request.getCategoryId()),
                        request.getItemName(),
                        request.getPrice(),
                        toAttachedFile(thumbnail)
                )
        );
    }

    @DeleteMapping("/{id}")
    public Response<ItemMaster> deleteMaster(@PathVariable String id){
        return OK(
                itemMasterService.deleteItemMasterById(Id.of(ItemMaster.class, id))
        );
    }

    @GetMapping("/search")
    public Response<List<ItemMaster>> searchMaster(@RequestBody ItemMasterSearchRequest request){
        return  OK(
                (List<ItemMaster>) itemMasterService.search(request.getItemName(), request.getStart(), request.getEnd())
        );
    }

    @PostMapping( "/{id}/option")
    public Response<ItemOption> saveOption(@PathVariable String id, @RequestBody ItemOptionRequest request) {
        return OK(
                itemOptionService.add(Id.of(ItemMaster.class, id), request.newItemOption())
        );
    }

    @PutMapping( "/{id}/option/{optionId}")
    public Response<ItemOption> updateOption(@PathVariable String optionId, @RequestBody ItemOptionRequest request) {
        return OK(
                itemOptionService.modify(Id.of(ItemOption.class, optionId), request.getColor(), request.getSize())
        );
    }

    @DeleteMapping("/{id}/option/{optionId}")
    public Response<Id<ItemOption, String>> deleteOption(@PathVariable String optionId){
        return OK(
                itemOptionService.deleteById(Id.of(ItemOption.class, optionId))
        );
    }


    @GetMapping("/display/all")
    public Response<Page<ItemDisplay>> allDisplayItems(PageRequest pageRequest){
        return OK(
                itemDisplayService.findAll(pageRequest.of())
        );
    }

    @GetMapping("/display/{displayId}")
    public Response<ItemDisplay> getDisplayItem(@PathVariable String displayId){
        return OK(
                itemDisplayService.findById(Id.of(ItemDisplay.class, displayId))
        );
    }

    @PostMapping(value = "/{masterId}/display", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<ItemDisplay> saveDisplayItem(
            @PathVariable String masterId,
            @ModelAttribute ItemDisplayRequest request,
            @RequestPart MultipartFile detailImageFile
    ) throws IOException {
        return OK(
               itemDisplayService.save(Id.of(ItemMaster.class, masterId), request.newItemDisplay(), toAttachedFile(detailImageFile))
        );
    }

    @PostMapping(value = "/{masterId}/display/{displayId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Response<ItemDisplay> updateDisplayItem(
            @PathVariable String masterId,
            @PathVariable String displayId,
            @ModelAttribute ItemDisplayRequest request,
            @RequestPart MultipartFile detailImageFile
    ) throws IOException {
        return OK(
                itemDisplayService.modify(
                        Id.of(ItemDisplay.class, displayId),
                        Id.of(ItemMaster.class, masterId),
                        request.toItemDisplay(Id.of(ItemDisplay.class, displayId)),
                        toAttachedFile(detailImageFile)
                )
        );
    }

    @DeleteMapping("/{id}/display/{displayId}")
    public Response<ItemDisplay> deleteDisplayItem(@PathVariable String displayId){
        return OK(
                itemDisplayService.deleteItemById(Id.of(ItemDisplay.class, displayId))
        );
    }

}
