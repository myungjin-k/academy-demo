package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryItem;
import my.myungjin.academyDemo.domain.order.Order;
import my.myungjin.academyDemo.domain.order.OrderItem;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.service.order.DeliveryService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.DeliveryRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api/admin")
@RequiredArgsConstructor
@RestController
public class DeliveryController {

    private final DeliveryService deliveryService;

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

    @GetMapping("/delivery/{deliveryId}/item/{itemId}")
    @ApiOperation(value = "배송상품 단건 조회")
    public Response<DeliveryItem> findDeliveryItem(
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "cd2940ee2dfc418384eedc450be832a2") String deliveryId,
            @PathVariable @ApiParam(value = "조회 대상 배송상품 PK", example = "d14b36612cd047a0b1e4e71d993dc9b2") String itemId){
        return OK(
                deliveryService.findItem(
                        Id.of(Delivery.class, deliveryId),
                        Id.of(DeliveryItem.class, itemId))
                        .orElseThrow(() -> new NotFoundException(DeliveryItem.class, deliveryId, itemId))
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
