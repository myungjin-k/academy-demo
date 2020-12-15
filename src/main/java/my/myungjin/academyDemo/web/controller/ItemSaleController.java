package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.item.ItemMaster;
import my.myungjin.academyDemo.service.item.ItemMasterService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/api/mall/item")
@RestController
public class ItemSaleController {
    private final ItemMasterService itemMasterService;

    @GetMapping(path = "/all", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "전시상품 전체 리스트 조회(API 키 필요없음)")
    public Response<Page<ItemMaster>> allItems(PageRequest pageRequest){
        return OK(
                itemMasterService.findAllItems(pageRequest.of())
        );
    }
}
