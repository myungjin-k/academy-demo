package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.CartItem;
import my.myungjin.academyDemo.domain.order.Order;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.order.CartService;
import my.myungjin.academyDemo.service.order.OrderService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.OrderRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api/mall")
@RequiredArgsConstructor
@RestController
public class OrderController {

    private final CartService cartService;

    private final OrderService orderService;

    @PostMapping("/member/{id}/cart")
    @ApiOperation(value = "장바구니 추가")
    public Response<CartItem> addCart(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String id,
            @RequestParam String itemId, @RequestParam int count, @AuthenticationPrincipal Authentication authentication){
        return OK(
                cartService.add(
                        Id.of(Member.class, id),
                        Id.of(Member.class, ((User) authentication.getDetails()).getId()),
                        Id.of(ItemDisplay.ItemDisplayOption.class, itemId),
                        count
                )
        );
    }

    @GetMapping("/member/{id}/cart/list")
    @ApiOperation(value = "장바구니 리스트 조회")
    public Response<List<CartItem>> cart(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String id,
            @AuthenticationPrincipal Authentication authentication
    ){
      return OK(
              cartService.findByMember(Id.of(Member.class, id), Id.of(Member.class,  ((User) authentication.getDetails()).getId()))
      );
    }

    @PatchMapping("/member/{id}/cart/{itemId}")
    @ApiOperation(value = "장바구니 아이템 수량 수정")
    public Response<CartItem> modifyCartItemCount(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String id,
            @PathVariable @ApiParam(value = "대상 장바구니 상품 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String itemId,
            @RequestParam int count, @AuthenticationPrincipal Authentication authentication){
        return OK(
                cartService.modify(
                        Id.of(Member.class, id),
                        Id.of(Member.class, ((User) authentication.getDetails()).getId()),
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
            @AuthenticationPrincipal Authentication authentication){
        return OK(
                cartService.delete(
                        Id.of(Member.class, id),
                        Id.of(Member.class, ((User) authentication.getDetails()).getId()),
                        Id.of(CartItem.class, itemId)
                )
        );
    }

    @PostMapping("/member/{memberId}/order")
    @ApiOperation(value = "주문 생성")
    public Response<Order> order(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @RequestBody OrderRequest orderRequest){
        return OK(
                orderService.ordering(
                        Id.of(Member.class, memberId),
                        orderRequest.newOrder(),
                        orderRequest.newDelivery(),
                        orderRequest.collectItems()
                )
        );
    }

}
