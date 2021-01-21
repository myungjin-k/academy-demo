package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.service.item.ItemDisplayService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.ItemSearchRequest;
import my.myungjin.academyDemo.web.request.PageRequest;
import my.myungjin.academyDemo.web.response.ItemDetailResponse;
import my.myungjin.academyDemo.web.response.ItemDisplayResponse;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/api/mall")
@RestController
public class ItemSaleController {
    private final ItemDisplayService itemDisplayService;

    @GetMapping(path = "/item/list")
    @ApiOperation(value = "전시상품 전체 리스트 조회(API 키 필요없음)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemDisplayResponse>> allItems(PageRequest pageRequest){
        return OK(
                itemDisplayService.findAll(pageRequest.of())
                .map(itemDisplay -> new ItemDisplayResponse().of(itemDisplay))
        );
    }

    @GetMapping(path = "/item/{id}")
    @ApiOperation(value = "전시상품 상세 조회(API 키 필요없음)")
    public Response<ItemDetailResponse> itemDetail(@PathVariable @ApiParam(value = "대상 전시상품 PK", defaultValue = "f23ba30a47194a2c8a3fd2ccadd952a4") String id){
        return OK(
                new ItemDetailResponse().of(itemDisplayService.findByIdWithOptions(Id.of(ItemDisplay.class, id)))
        );
    }


    @GetMapping("/item")
    @ApiOperation(value = "전시상품 검색(상품명) (api key 필요 없음)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "itemName", dataType = "string", paramType = "query", value = "상품명"),
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemDisplay>> searchDisplayItems(ItemSearchRequest request, PageRequest pageRequest){
        return  OK(
                itemDisplayService.searchByNameAndCreateAt(request.getItemName(), null, null, true, pageRequest.of())
        );
    }


    @GetMapping("/category/{categoryId}/item/list")
    @ApiOperation(value = "카테고리별 전시상품 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemDisplayResponse>> searchMaster(@PathVariable @ApiParam(value = "대상 카테고리 PK", defaultValue = "3ebebfeb9fbe4ecfa5935f96ed308854") String categoryId,
                                                    PageRequest pageRequest){
        return  OK(
                itemDisplayService.findAllByCategoryGroup(Id.of(CommonCode.class, categoryId), pageRequest.of())
                        .map(itemDisplay -> new ItemDisplayResponse().of(itemDisplay))
        );
    }
}
