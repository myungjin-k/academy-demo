package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.domain.item.ItemStatus;
import my.myungjin.academyDemo.service.item.ItemDisplayOptionService;
import my.myungjin.academyDemo.service.item.ItemDisplayService;
import my.myungjin.academyDemo.service.item.ItemMasterService;
import my.myungjin.academyDemo.service.item.ItemOptionService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class ItemAdminController {

    private final ItemMasterService itemMasterService;

    private final ItemOptionService itemOptionService;

    private final ItemDisplayService itemDisplayService;

    private final ItemDisplayOptionService itemDisplayOptionService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @GetMapping("/itemMaster/list")
    @ApiOperation(value = "상품 마스터 전체 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemMaster>> allItems(PageRequest pageRequest){
        Page<ItemMaster> res = itemMasterService.findAllItems(pageRequest.of());
        log.info("Result: {}", res.getContent().get(0).getOptions());
        return OK(res);
    }

    @GetMapping("/itemMaster/search")
    @ApiOperation(value = "상품 마스터 검색(상품명)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemMaster>> search(@RequestParam String itemName, PageRequest pageRequest){
        return OK(itemMasterService.search(itemName, null, null, pageRequest.of()));
    }

    @GetMapping("/commonCode/category/list")
    @ApiOperation(value = "상품 카테고리 검색")
    public Response<List<CommonCode>> searchCategory(@RequestParam @ApiParam(value = "상품 카테고리 검색어(카테고리 한글이름)", defaultValue = "니트") String searchParam){
        return OK(
                itemMasterService.searchCategoryByNameKor(searchParam)
        );
    }

    @PostMapping(path = "/itemMaster", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
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

    @PutMapping(path = "/itemMaster/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "상품 마스터 수정")
    public Response<ItemMaster> updateMaster(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK", defaultValue = "8c1cbb792b8d447e9128d53920cf9366") String  id,
            @ModelAttribute ItemMasterRequest request,
            @RequestPart(required = false) MultipartFile thumbnail
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

    @DeleteMapping("/itemMaster/{id}")
    @ApiOperation(value = "상품 마스터 삭제")
    public Response<ItemMaster> deleteMaster(@PathVariable @ApiParam(value = "대상 상품 마스터 PK", defaultValue = "8c1cbb792b8d447e9128d53920cf9366") String id){
        return OK(
                itemMasterService.deleteItemMasterById(Id.of(ItemMaster.class, id))
        );
    }


    @GetMapping( "/itemMaster/{id}/itemOption/list")
    @ApiOperation(value = "마스터별 상품 옵션 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemMaster.ItemOption>> options(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK", defaultValue = "8c1cbb792b8d447e9128d53920cf9366") String id,
             PageRequest pageRequest) {
        return OK(
                itemOptionService.findAllByMasterIdWithPage(Id.of(ItemMaster.class, id), pageRequest.of())
        );
    }

    @PostMapping( "/itemMaster/{id}/itemOption")
    @ApiOperation(value = "상품 옵션 등록")
    public Response<ItemMaster.ItemOption> saveOption(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK", defaultValue = "8c1cbb792b8d447e9128d53920cf9366") String id,
            @RequestBody ItemOptionRequest request) {
        return OK(
                itemOptionService.add(
                    Id.of(ItemMaster.class, id), request.newItemOption()
                )
        );
    }

    @PostMapping( "/itemMaster/{id}/itemOption/list")
    @ApiOperation(value = "상품 옵션 등록 (리스트)")
    public Response<List<ItemMaster.ItemOption>> saveOptionList(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK", defaultValue = "8c1cbb792b8d447e9128d53920cf9366") String id,
            @RequestBody List<ItemOptionRequest> request) {
        return OK(
                itemOptionService.addList(
                        Id.of(ItemMaster.class, id),
                        request.stream()
                            .map(ItemOptionRequest::newItemOption)
                            .collect(Collectors.toList())
                )
        );
    }

    @PutMapping( "/itemOption/{optionId}")
    @ApiOperation(value = "상품 옵션 수정")
    public Response<ItemMaster.ItemOption> updateOption(
            @PathVariable @ApiParam(value = "대상 상품 옵션 PK", defaultValue = "fb32787a91614b978cb94b0d47d7c676") String optionId,
            @RequestBody ItemOptionRequest request) {
        return OK(
                itemOptionService.modify(Id.of(ItemMaster.ItemOption.class, optionId), request.getColor(), request.getSize())
        );
    }

    @DeleteMapping("/itemOption/{optionId}")
    @ApiOperation(value = "상품 옵션 삭제")
    public Response<ItemMaster.ItemOption> deleteOption(
            @PathVariable @ApiParam(value = "대상 상품 옵션 PK", defaultValue = "fb32787a91614b978cb94b0d47d7c676")  String optionId){
        return OK(
                itemOptionService.deleteById(Id.of(ItemMaster.ItemOption.class, optionId))
        );
    }


    @GetMapping("/itemDisplay/list")
    @ApiOperation(value = "전시상품 리스트 전체 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemDisplay>> allDisplayItems(PageRequest pageRequest){
        return OK(
                itemDisplayService.findAll(pageRequest.of())
        );
    }


    @GetMapping("/itemDisplay/{displayId}")
    @ApiOperation(value = "전시상품 단건 조회")
    public Response<ItemDisplay> getDisplayItem(
            @PathVariable @ApiParam(value = "대상 전시상품 PK", defaultValue = "f23ba30a47194a2c8a3fd2ccadd952a4") String displayId){
        return OK(
                itemDisplayService.findById(Id.of(ItemDisplay.class, displayId))
        );
    }
    @GetMapping("/itemMaster/{masterId}/itemDisplay")
    @ApiOperation(value = "마스터별 전시상품 조회")
    public Response<ItemDisplay> saveDisplayItem(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK", defaultValue = "8c1cbb792b8d447e9128d53920cf9366") String masterId
    ) {
        return OK(
                itemDisplayService.findByItemMaster(Id.of(ItemMaster.class, masterId)).orElse(null)
        );
    }

    @PostMapping(value = "/itemMaster/{masterId}/itemDisplay", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "전시상품 등록")
    public Response<ItemDisplay> saveDisplayItem(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK", defaultValue = "8c1cbb792b8d447e9128d53920cf9366") String masterId,
            @ModelAttribute ItemDisplayRequest request,
            @RequestPart MultipartFile detailImageFile
    ) throws IOException {
        return OK(
               itemDisplayService.save(Id.of(ItemMaster.class, masterId), request.newItemDisplay(), toAttachedFile(detailImageFile))
        );
    }

    @PutMapping(value = "/itemMaster/{masterId}/itemDisplay/{displayId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "전시상품 수정")
    public Response<ItemDisplay> updateDisplayItem(
            @PathVariable @ApiParam(value = "대상 상품 마스터 PK", defaultValue = "8c1cbb792b8d447e9128d53920cf9366") String masterId,
            @PathVariable @ApiParam(value = "대상 전시상품 PK", defaultValue = "f23ba30a47194a2c8a3fd2ccadd952a4") String displayId,
            @ModelAttribute ItemDisplayRequest request,
            @RequestPart(required = false) MultipartFile detailImageFile
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

    @DeleteMapping("/itemDisplay/{displayId}")
    @ApiOperation(value = "전시상품 삭제")
    public Response<ItemDisplay> deleteDisplayItem(
            @PathVariable @ApiParam(value = "대상 전시상품 PK", defaultValue = "f23ba30a47194a2c8a3fd2ccadd952a4") String displayId){
        return OK(
                itemDisplayService.deleteItemById(Id.of(ItemDisplay.class, displayId))
        );
    }

    @GetMapping("/itemDisplay")
    @ApiOperation(value = "전시상품 검색(상품명, 등록일(from), 등록일(to)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemDisplay>> searchMaster(ItemSearchRequest request, PageRequest pageRequest){
        return  OK(
                itemDisplayService.searchByNameAndCreateAt(request.getItemName(), request.getStart(), request.getEnd(), false, pageRequest.of())
        );
    }

    @GetMapping( "/itemDisplay/{id}/itemDisplayOption/list")
    @ApiOperation(value = "전시상품별 상품 옵션 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemDisplay.ItemDisplayOption>> displayOptions(
            @PathVariable @ApiParam(value = "대상 상품 전시 PK", defaultValue = "8c1cbb792b8d447e9128d53920cf9366") String id,
            PageRequest pageRequest) {
        return OK(
                itemDisplayOptionService.findAllByMasterId(Id.of(ItemDisplay.class, id), pageRequest.of())
        );
    }

    @PutMapping( "/itemDisplayOption/{optionId}")
    @ApiOperation(value = "전시 상품 옵션 수정")
    public Response<ItemDisplay.ItemDisplayOption> updateDisplayOption(
            @PathVariable @ApiParam(value = "대상 전시 상품 옵션 PK", defaultValue = "fb32787a91614b978cb94b0d47d7c676") String optionId,
            @RequestBody ItemDisplayOptionRequest request) {
        return OK(
                itemDisplayOptionService.modify(Id.of(ItemDisplay.ItemDisplayOption.class, optionId), request.getColor(), request.getSize(), ItemStatus.valueOf(request.getStatus()))
        );
    }

    @DeleteMapping("/itemDisplayOption/{optionId}")
    @ApiOperation(value = "전시 상품 옵션 삭제")
    public Response<ItemDisplay.ItemDisplayOption> deleteDisplayOption(
            @PathVariable @ApiParam(value = "대상 전시 상품 옵션 PK", defaultValue = "fb32787a91614b978cb94b0d47d7c676")  String optionId){
        return OK(
                itemDisplayOptionService.deleteById(Id.of(ItemDisplay.ItemDisplayOption.class, optionId))
        );
    }



}
