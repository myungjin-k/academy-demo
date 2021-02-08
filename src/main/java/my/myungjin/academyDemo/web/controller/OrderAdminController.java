package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.order.*;
import my.myungjin.academyDemo.service.admin.OrderAdminService;
import my.myungjin.academyDemo.service.item.ItemDisplayOptionService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.DeliveryItemRequest;
import my.myungjin.academyDemo.web.request.DeliveryRequest;
import my.myungjin.academyDemo.web.request.OrderSearchRequest;
import my.myungjin.academyDemo.web.request.PageRequest;
import my.myungjin.academyDemo.web.response.AdminOrderListResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class OrderAdminController {

    private final OrderAdminService orderAdminService;
    
    private final ItemDisplayOptionService itemDisplayOptionService;

    @GetMapping("/order/search")
    @ApiOperation(value = "주문 검색(주문번호, 등록일)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<AdminOrderListResponse>> findAllOrders(OrderSearchRequest request, PageRequest pageRequest){
        Page<OrderItem> result = orderAdminService.searchOrders(Id.of(Order.class, request.getOrderId()), request.getStart(), request.getEnd(), pageRequest.of());
        List<AdminOrderListResponse> returnList = result.getContent()
                .stream()
                .map(item -> new AdminOrderListResponse().of(item))
                .collect(Collectors.toList());
        return OK(
                new PageImpl<>(returnList)
        );
    }


    @GetMapping("/order/{id}")
    @ApiOperation(value = "주문 아이디별 상세 조회")
    public Response<Order> getOrderDetail(
            @PathVariable @ApiParam(value = "조회 대상 주문 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id){
        return OK(
                orderAdminService.getOrderDetail(Id.of(Order.class, id))
        );
    }

    @GetMapping("/delivery/{id}/order")
    @ApiOperation(value = "배송정보 연계주문 상품 조회")
    public Response<List<OrderItem>> findOrderItems(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id){
        return OK(
                orderAdminService.findAllOrderItems(Id.of(Delivery.class, id))
        );
    }

    @GetMapping("/delivery/{id}")
    @ApiOperation(value = "배송정보 단건 조회")
    public Response<Delivery> findDelivery(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id){
        return OK(
                orderAdminService.findById(Id.of(Delivery.class, id))
        );
    }

    // TODO 배송상태 배치 업데이트

    @PatchMapping("/delivery/{id}/status/{status}")
    @ApiOperation(value = "배송상태 업데이트")
    public Response<Delivery> updateDeliveryStatus(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id,
            @PathVariable @ApiParam(value = "조회 대상 배송상태 이름", example = "DELIVERED") String status
            ){
        return OK(
                orderAdminService.modifyStatus(Id.of(Delivery.class, id), DeliveryStatus.valueOf(status))
        );
    }

    @PatchMapping("/delivery/{id}/invoiceNum/{invoiceNum}")
    @ApiOperation(value = "송장번호 업데이트")
    public Response<Delivery> updateInvoiceNum(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id,
            @PathVariable @ApiParam(value = "조회 대상 송장번호", example = "0123456789") String invoiceNum
    ){
        return OK(
                orderAdminService.updateInvoice(Id.of(Delivery.class, id), invoiceNum)
        );
    }

    @PatchMapping("/delivery/{id}/address")
    @ApiOperation(value = "배송주소 업데이트")
    public Response<Delivery> updateAddress(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id,
            @RequestBody @ApiParam(value = "조회 대상 배송주소") Map<String, String> address
    ){
        return OK(
                orderAdminService.modifyAddress(Id.of(Delivery.class, id), address.get("addr1"), address.get("addr2"))
        );
    }

    @PostMapping("/order/{orderId}/delivery")
    @ApiOperation(value = "배송정보 추가")
    public Response<Delivery> addDelivery(
            @PathVariable @ApiParam(value = "조회 대상 주문 PK", example = "f6f50475354d49f68916eaf30ea5b266") String orderId,
            @RequestBody DeliveryRequest deliveryRequest){
        return OK(
                orderAdminService.addDelivery(Id.of(Order.class, orderId), deliveryRequest.newDelivery(), deliveryRequest.idList())
        );
    }

    @DeleteMapping("/delivery/{id}")
    @ApiOperation(value = "배송정보 삭제")
    public Response<Delivery> deleteDelivery(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id){
        return OK(
                orderAdminService.deleteDelivery(Id.of(Delivery.class, id))
        );
    }

    @GetMapping("/itemDisplayOption/list")
    @ApiOperation(value = "전시상품 옵션 검색(상품명, 전시상품 아이디)")
    /*@ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })*/
    public Response<List<ItemDisplay.ItemDisplayOption>> search(@RequestParam String displayId, @RequestParam String itemName){
        return OK(itemDisplayOptionService.search(Id.of(ItemDisplay.class, displayId), itemName));
    }

    @PostMapping("/delivery/{deliveryId}/item")
    @ApiOperation(value = "배송상품 추가")
    public Response<DeliveryItem> addDeliveryItem(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String deliveryId,
            @RequestBody DeliveryItemRequest request){
        return OK(
                orderAdminService.addDeliveryItem(
                        Id.of(Delivery.class, request.getDeliveryId()),
                        Id.of(ItemDisplay.ItemDisplayOption.class, request.getItemId()),
                        request.getCount())
                .orElse(null)
        );
    }

    @DeleteMapping("/delivery/{deliveryId}/item/{itemId}")
    @ApiOperation(value = "배송상품 삭제")
    public Response<DeliveryItem> deleteDeliveryItem(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String deliveryId,
            @PathVariable @ApiParam(value = "조회 대상 배송상품 PK", example = "d14b36612cd047a0b1e4e71d993dc9b2") String itemId){
        return OK(
                orderAdminService.deleteDeliveryItem(Id.of(Delivery.class, deliveryId), Id.of(DeliveryItem.class, itemId))
        );
    }

    @PatchMapping("/delivery/{deliveryId}/item/{itemId}")
    @ApiOperation(value = "배송상품 수량 수정")
    public Response<DeliveryItem> modifyDeliveryItemCount(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String deliveryId,
            @PathVariable @ApiParam(value = "조회 대상 배송상품 PK", example = "d14b36612cd047a0b1e4e71d993dc9b2") String itemId,
            @RequestParam int count){
        return OK(
                orderAdminService.modifyDeliveryItemCount(Id.of(Delivery.class, deliveryId), Id.of(DeliveryItem.class, itemId), count)
        );
    }
}
