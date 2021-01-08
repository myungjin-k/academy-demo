package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.OrderItem;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.member.MemberService;
import my.myungjin.academyDemo.service.review.ReviewService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.PageRequest;
import my.myungjin.academyDemo.web.request.ReviewRequest;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static my.myungjin.academyDemo.web.Response.OK;

@RequestMapping("/api")
@RequiredArgsConstructor
@RestController
public class ReviewController {

    private final ReviewService reviewService;

    private final MemberService memberService;

    @GetMapping("/mall/item/{itemId}/review/list")
    @ApiOperation(value = "상품별 리뷰 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<Review>> findAllReviewsByItem(
            @PathVariable @ApiParam(value = "조회 대상 전시상품 PK", example = "f23ba30a47194a2c8a3fd2ccadd952a4") String itemId,
            PageRequest pageRequest){
        return OK(
                reviewService.findAllByItem(Id.of(ItemDisplay.class, itemId), pageRequest.of())
        );
    }

    @PostMapping("/mall/member/{memberId}/orderItem/{itemId}/review")
    @ApiOperation(value = "리뷰 작성")
    public Response<Review> review(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @PathVariable @ApiParam(value = "조회 대상 주문상품 PK", example = "c7bb4cb6efcd4f4bb388eafb6fa52fac") String itemId,
            @ModelAttribute ReviewRequest reviewRequest,
            @RequestPart(required = false) MultipartFile reviewImgFile,
            @AuthenticationPrincipal User authentication) throws IOException {
        return OK(
                reviewService.write(
                        Id.of(Member.class, memberId),
                        Id.of(Member.class,  authentication.getId()),
                        Id.of(OrderItem.class, itemId),
                        reviewRequest.newReview(),
                        toAttachedFile(reviewImgFile)
                )
        );
    }

    @PutMapping("/mall/member/{memberId}/review/{reviewId}")
    @ApiOperation(value = "리뷰 수정")
    public Response<Review> modifyReview(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @PathVariable @ApiParam(value = "조회 대상 리뷰 PK", example = "c7bb4cb6efcd4f4bb388eafb6fa52fac") String reviewId,
            @ModelAttribute ReviewRequest reviewRequest,
            @RequestPart(required = false) MultipartFile reviewImgFile,
            @AuthenticationPrincipal User authentication) throws IOException {
        return OK(
                reviewService.modify(
                        Id.of(Member.class, memberId),
                        Id.of(Member.class, authentication.getId()),
                        Id.of(Review.class, reviewId),
                        reviewRequest.getContent(),
                        reviewRequest.getScore(),
                        toAttachedFile(reviewImgFile)
                )
        );
    }

    @PatchMapping("/admin/review/reserves/{memberId}")
    @ApiOperation(value = "회원 적립금 업데이트")
    public Response<Member> updateReserves(
            @PathVariable @ApiParam(value = "조회 대상 회원 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String memberId,
            @RequestParam int plus, @RequestParam int minus) {
        return OK(
                memberService.updateReserves(Id.of(Member.class, memberId), minus, plus)

        );
    }
}
