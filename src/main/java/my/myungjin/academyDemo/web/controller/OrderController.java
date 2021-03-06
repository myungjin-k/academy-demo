package my.myungjin.academyDemo.web.controller;

import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.Payment;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.event.Coupon;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.CartItem;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.Order;
import my.myungjin.academyDemo.iamport.IamportClient;
import my.myungjin.academyDemo.security.MyAuthentication;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.member.MemberService;
import my.myungjin.academyDemo.service.order.CartService;
import my.myungjin.academyDemo.service.order.OrderService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.CartRequest;
import my.myungjin.academyDemo.web.request.OrderRequest;
import my.myungjin.academyDemo.web.request.PageRequest;
import my.myungjin.academyDemo.web.response.MemberRatingInfoResponse;
import my.myungjin.academyDemo.web.response.OrderDetailResponse;
import my.myungjin.academyDemo.web.response.OrderInfoResponse;
import my.myungjin.academyDemo.web.response.OrderResponse;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api/mall")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final CartService cartService;

    private final OrderService orderService;

    private final MemberService memberService;

    private final IamportClient iamportClient;

    @PostMapping("/member/{id}/cart")
    @ApiOperation(value = "장바구니 추가 (리스트)")
    public Response<List<CartItem>> addCart(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String id,
            @RequestBody List<CartRequest> requestList, @AuthenticationPrincipal MyAuthentication authentication){
        return OK(
                cartService.addItems(
                        Id.of(Member.class, id),
                        Id.of(Member.class, authentication.id),
                        requestList)
        );
    }

    @GetMapping("/member/{id}/cart/list")
    @ApiOperation(value = "장바구니 리스트 조회")
    public Response<List<CartItem>> cart(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String id,
            @AuthenticationPrincipal MyAuthentication authentication
    ){
      return OK(
              cartService.findByMember(Id.of(Member.class, id), Id.of(Member.class, authentication.id))
      );
    }

    @PatchMapping("/member/{id}/cart/{itemId}")
    @ApiOperation(value = "장바구니 아이템 수량 수정")
    public Response<CartItem> modifyCartItemCount(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String id,
            @PathVariable @ApiParam(value = "대상 장바구니 상품 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String itemId,
            @RequestParam int count, @AuthenticationPrincipal MyAuthentication authentication){
        return OK(
                cartService.modify(
                        Id.of(Member.class, id),
                        Id.of(Member.class, authentication.id),
                        Id.of(CartItem.class, itemId),
                        count
                )
        );
    }

    @DeleteMapping("/member/{id}/cart/{itemId}")
    @ApiOperation(value = "장바구니 아이템 삭제")
    public Response<CartItem> deleteCartItem(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String id,
            @PathVariable @ApiParam(value = "대상 장바구니 상품 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String itemId,
            @AuthenticationPrincipal MyAuthentication authentication){
        return OK(
                cartService.delete(
                        Id.of(Member.class, id),
                        Id.of(Member.class, authentication.id),
                        Id.of(CartItem.class, itemId)
                )
        );
    }


    @GetMapping("/order/orderMemberInfo")
    @ApiOperation(value = "회원정보에서 주문자 정보 로드")
    public Response<OrderInfoResponse> findOrderMemberInfo(@AuthenticationPrincipal MyAuthentication authentication){
        return OK(
                OrderInfoResponse.of(
                        orderService.findMemberInfo(Id.of(Member.class, authentication.id))
                                .orElse(null)
                )
        );
    }

    // TODO 결제 엔티티 생성
    @GetMapping("/pay/{uid}")
    @ApiOperation(value = "결제정보 조회")
    public Response<Payment> getPayInfo(
            @PathVariable @ApiParam(value = "결제 완료 응답 uid", example = "imp_448280090638") String uid) throws IOException, IamportResponseException {
        return OK(
                iamportClient.paymentByImpUid(uid).getResponse()
        );
    }

    @PostMapping("/member/{memberId}/order")
    @ApiOperation(value = "주문 생성")
    public Response<Order> order(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @RequestBody OrderRequest orderRequest) {
        return OK(
                orderService.ordering(
                        Id.of(Member.class, memberId),
                        orderRequest.newOrder(),
                        orderRequest.newDelivery(),
                        orderRequest.collectItems(),
                        orderRequest.getUsedCouponId().isEmpty() ?
                                empty() : Optional.of(Id.of(Coupon.class, orderRequest.getUsedCouponId()))
                )
        );
    }

    @GetMapping("/member/{memberId}/ratingInfo")
    @ApiOperation(value = "회원등급 및 쿠폰보유 정보(주문내역 페이지)")
    public Response<MemberRatingInfoResponse> ratingInfo(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId){
        return OK(
                new MemberRatingInfoResponse().of(memberService.findMyInfo(Id.of(Member.class, memberId)))
        );
    }

    @GetMapping("/member/{memberId}/order/list")
    @ApiOperation(value = "회원별 전체 주문 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "ASC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<OrderResponse>> findAllOrdersByMember(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            PageRequest pageRequest
    ){
        return OK(
                orderService.findAllMyMemberWithPage(Id.of(Member.class, memberId), pageRequest.of())
                        .map(order -> new OrderResponse().of(order))
        );
    }

    @GetMapping("/member/{memberId}/order/{orderId}")
    @ApiOperation(value = "회원별 단건 주문 조회")
    public Response<OrderDetailResponse> getOrderDetail(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @PathVariable @ApiParam(value = "조회 대상 주문 PK", example = "03039b4535404247bfee52cfd934c779") String orderId){
        return OK(
                new OrderDetailResponse().of(orderService.getOrderDetail(Id.of(Member.class, memberId), Id.of(Order.class, orderId)))
        );
    }

    @PutMapping("/member/{memberId}/order/{orderId}")
    @ApiOperation(value = "회원별 단건 주문 수정")
    public Response<Order> modify(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @PathVariable @ApiParam(value = "조회 대상 주문 PK", example = "03039b4535404247bfee52cfd934c779") String orderId,
            @RequestBody OrderRequest request
    ){
        return OK(
                orderService.modify(Id.of(Member.class, memberId), Id.of(Order.class, orderId), request.toOrder(Id.of(Order.class, orderId)))
        );
    }


    @PutMapping("/member/{memberId}/order/{orderId}/delivery/{deliveryId}")
    @ApiOperation(value = "회원별 단건 배송정보 수정")
    public Response<Delivery> modify(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @PathVariable @ApiParam(value = "조회 대상 주문 PK", example = "03039b4535404247bfee52cfd934c779") String orderId,
            @PathVariable @ApiParam(value = "조회 대상 배송정보 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String deliveryId,
            @RequestBody OrderRequest request
    ){
        return OK(
                orderService.modify(Id.of(Member.class, memberId), Id.of(Order.class, orderId), Id.of(Delivery.class, deliveryId),
                        request.toDelivery(Id.of(Delivery.class, deliveryId)))
        );
    }

    @Transactional
    @PatchMapping("/member/{memberId}/order/{orderId}/cancel")
    @ApiOperation(value = "회원별 단건 주문 취소")
    public Response<Order> cancel(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @PathVariable @ApiParam(value = "조회 대상 주문 PK", example = "03039b4535404247bfee52cfd934c779") String orderId
    ) throws IOException, IamportResponseException {
        Order cancelled = orderService.cancel(Id.of(Member.class, memberId), Id.of(Order.class, orderId));
        iamportClient.cancelPaymentByImpUid(new CancelData(cancelled.getPaymentUid(), true));
        return OK(cancelled);
    }

    @PostMapping("/pay/{uid}/cancel")
    @ApiOperation(value = "결제 취소")
    public Response<Payment> cancel(
            @PathVariable @ApiParam(value = "결제 완료 응답 uid", example = "imp_448280090638") String uid) throws IOException, IamportResponseException {
        return OK(
                iamportClient.cancelPaymentByImpUid(new CancelData(uid, true)).getResponse()
        );
    }
}
