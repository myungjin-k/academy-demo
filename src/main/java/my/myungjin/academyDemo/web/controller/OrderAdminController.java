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
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.DeliveryRequest;
import my.myungjin.academyDemo.web.request.OrderSearchRequest;
import my.myungjin.academyDemo.web.request.PageRequest;
import my.myungjin.academyDemo.web.response.AdminDeliveryListResponse;
import my.myungjin.academyDemo.web.response.OrderItemResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class OrderAdminController {

    private final OrderAdminService deliveryService;

    private final OrderAdminService orderAdminService;

    @GetMapping("/order/search")
    @ApiOperation(value = "주문 검색(주문번호, 등록일)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<AdminDeliveryListResponse>> findAllOrders(OrderSearchRequest request, PageRequest pageRequest){
        Page<DeliveryItem> result = orderAdminService.searchOrders(Id.of(Order.class, request.getOrderId()), request.getStart(), request.getEnd(), pageRequest.of());
        List<AdminDeliveryListResponse> returnList = result.getContent()
                .stream()
                .map(item -> new AdminDeliveryListResponse().of(item))
                .collect(Collectors.toList());
        return OK(
                new PageImpl<>(returnList)
        );
    }

    @GetMapping("/delivery/{id}/order")
    @ApiOperation(value = "배송정보 연계주문 상품 조회")
    public Response<List<OrderItem>> findOrderItems(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id){
        return OK(
                deliveryService.findAllOrderItems(Id.of(Delivery.class, id))
        );
    }

    @GetMapping("/delivery/{id}")
    @ApiOperation(value = "배송정보 단건 조회")
    public Response<Delivery> findDelivery(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id){
        return OK(
                deliveryService.findById(Id.of(Delivery.class, id))
        );
    }


    @PatchMapping("/delivery/{id}")
    @ApiOperation(value = "배송상태 업데이트")
    public Response<Delivery> updateDeliveryStatus(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id,
            @RequestParam String status
            ){
        return OK(
                deliveryService.modifyStatus(Id.of(Delivery.class, id), DeliveryStatus.valueOf(status))
        );
    }

    @PostMapping("/order/{orderId}/delivery")
    @ApiOperation(value = "배송정보 추가")
    public Response<Delivery> addDelivery(
            @PathVariable @ApiParam(value = "조회 대상 주문 PK", example = "f6f50475354d49f68916eaf30ea5b266") String orderId,
            @RequestBody DeliveryRequest deliveryRequest){
        return OK(
                deliveryService.addDelivery(Id.of(Order.class, orderId), deliveryRequest.newDelivery(), deliveryRequest.idList())
        );
    }

    @DeleteMapping("/delivery/{id}")
    @ApiOperation(value = "배송정보 삭제")
    public Response<Delivery> deleteDelivery(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String id){
        return OK(
                deliveryService.deleteDelivery(Id.of(Delivery.class, id))
        );
    }

    @PostMapping("/delivery/{deliveryId}/item")
    @ApiOperation(value = "배송상품 추가")
    public Response<DeliveryItem> addDeliveryItem(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String deliveryId,
            @RequestParam String itemId, @RequestParam int count){
        return OK(
                deliveryService.addDeliveryItem(Id.of(Delivery.class, deliveryId), Id.of(ItemDisplay.ItemDisplayOption.class, itemId), count)
                .orElse(null)
        );
    }

    @DeleteMapping("/delivery/{deliveryId}/item/{itemId}")
    @ApiOperation(value = "배송상품 삭제")
    public Response<DeliveryItem> deleteDeliveryItem(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String deliveryId,
            @PathVariable @ApiParam(value = "조회 대상 배송상품 PK", example = "d14b36612cd047a0b1e4e71d993dc9b2") String itemId){
        return OK(
                deliveryService.deleteDeliveryItem(Id.of(Delivery.class, deliveryId), Id.of(DeliveryItem.class, itemId))
        );
    }

    @PatchMapping("/delivery/{deliveryId}/item/{itemId}")
    @ApiOperation(value = "배송상품 수량 수정")
    public Response<DeliveryItem> modifyDeliveryItemCount(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String deliveryId,
            @PathVariable @ApiParam(value = "조회 대상 배송상품 PK", example = "d14b36612cd047a0b1e4e71d993dc9b2") String itemId,
            @RequestParam int count){
        return OK(
                deliveryService.modifyDeliveryItemCount(Id.of(Delivery.class, deliveryId), Id.of(DeliveryItem.class, itemId), count)
        );
    }
}
