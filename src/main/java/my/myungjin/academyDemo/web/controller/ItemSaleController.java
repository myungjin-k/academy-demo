package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.service.item.ItemDisplayService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/api/mall/item")
@RestController
public class ItemSaleController {
    private final ItemDisplayService itemDisplayService;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "전시상품 전체 리스트 조회(API 키 필요없음)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<ItemDisplay>> allItems(PageRequest pageRequest){
        return OK(
                itemDisplayService.findAll(pageRequest.of())
        );
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "전시상품 상세 조회")
    public Response<ItemDisplay> itemDetail(@PathVariable @ApiParam(value = "대상 전시상품 PK", defaultValue = "f23ba30a47194a2c8a3fd2ccadd952a4") String id){
        return OK(
                itemDisplayService.findByIdWithOptions(Id.of(ItemDisplay.class, id))
        );
    }
}
