package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
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
    @ApiOperation(value = "상품 마스터 전체 목록 조회")
    public Response<Page<ItemMaster>> allItems(PageRequest pageRequest){
        return OK(
                itemMasterService.findAllItems(pageRequest.of())
        );
    }

    @GetMapping("/category")
    @ApiOperation(value = "상품 카테고리 검색")
    public Response<List<CommonCode>> searchCategory(@RequestParam @ApiParam(value = "상품 카테고리 검색어(카테고리 한글이름)") String searchParam){
        return OK(
                itemMasterService.searchCategoryByNameKor(searchParam)
        );
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "상품 마스터 등록")
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
    @ApiOperation(value = "상품 마스터 수정")
    public Response<ItemMaster> updateMaster(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK") String  id,
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
    @ApiOperation(value = "상품 마스터 삭제")
    public Response<ItemMaster> deleteMaster(@PathVariable @ApiParam(value = "대상 상품 마스터 PK") String id){
        return OK(
                itemMasterService.deleteItemMasterById(Id.of(ItemMaster.class, id))
        );
    }

    @GetMapping("/search")
    @ApiOperation(value = "상품 마스터 검색(코드, 한글명, 영어명)")
    public Response<List<ItemMaster>> searchMaster(ItemMasterSearchRequest request){
        return  OK(
                (List<ItemMaster>) itemMasterService.search(request.getItemName(), request.getStart(), request.getEnd())
        );
    }

    @PostMapping( "/{id}/option")
    @ApiOperation(value = "상품 옵션 등록")
    public Response<ItemOption> saveOption(@PathVariable @ApiParam(value = "대상 상품 마스터 PK") String id, @RequestBody ItemOptionRequest request) {
        return OK(
                itemOptionService.add(Id.of(ItemMaster.class, id), request.newItemOption())
        );
    }

    @PutMapping( "/{id}/option/{optionId}")
    @ApiOperation(value = "상품 옵션 수정")
    public Response<ItemOption> updateOption(@PathVariable @ApiParam(value = "대상 상품 옵션 PK") String optionId, @RequestBody ItemOptionRequest request) {
        return OK(
                itemOptionService.modify(Id.of(ItemOption.class, optionId), request.getColor(), request.getSize())
        );
    }

    @DeleteMapping("/{id}/option/{optionId}")
    @ApiOperation(value = "상품 옵션 삭제")
    public Response<Id<ItemOption, String>> deleteOption(@PathVariable @ApiParam(value = "대상 상품 옵션 PK")  String optionId){
        return OK(
                itemOptionService.deleteById(Id.of(ItemOption.class, optionId))
        );
    }


    @GetMapping("/display/all")
    @ApiOperation(value = "전시상품 리스트 전체 조회")
    public Response<Page<ItemDisplay>> allDisplayItems(PageRequest pageRequest){
        return OK(
                itemDisplayService.findAll(pageRequest.of())
        );
    }

    @GetMapping("/display/{displayId}")
    @ApiOperation(value = "전시상품 단건 조회")
    public Response<ItemDisplay> getDisplayItem(@PathVariable @ApiParam(value = "대상 전시상품 PK") String displayId){
        return OK(
                itemDisplayService.findById(Id.of(ItemDisplay.class, displayId))
        );
    }

    @PostMapping(value = "/{masterId}/display", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "전시상품 등록")
    public Response<ItemDisplay> saveDisplayItem(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK") String masterId,
            @ModelAttribute ItemDisplayRequest request,
            @RequestPart MultipartFile detailImageFile
    ) throws IOException {
        return OK(
               itemDisplayService.save(Id.of(ItemMaster.class, masterId), request.newItemDisplay(), toAttachedFile(detailImageFile))
        );
    }

    @PostMapping(value = "/{masterId}/display/{displayId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "전시상품 수정")
    public Response<ItemDisplay> updateDisplayItem(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK") String masterId,
            @PathVariable @ApiParam(value = "대상 전시상품 PK") String displayId,
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
    @ApiOperation(value = "전시상품 삭제")
    public Response<ItemDisplay> deleteDisplayItem(@PathVariable @ApiParam(value = "대상 전시상품 PK") String displayId){
        return OK(
                itemDisplayService.deleteItemById(Id.of(ItemDisplay.class, displayId))
        );
    }

}
