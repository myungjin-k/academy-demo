package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Admin;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.domain.review.ReviewComment;
import my.myungjin.academyDemo.security.User;
import my.myungjin.academyDemo.service.review.ReviewCommentService;
import my.myungjin.academyDemo.service.review.ReviewService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.PageRequest;
import my.myungjin.academyDemo.web.response.AdminReviewResponse;
import my.myungjin.academyDemo.web.response.ReviewDetailResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static my.myungjin.academyDemo.web.Response.OK;

@RequiredArgsConstructor
@RequestMapping("/api/admin")
@RestController
public class ReviewAdminController {

    private final ReviewCommentService reviewCommentService;

    private final ReviewService reviewService;

    @GetMapping("/review/list")
    @ApiOperation(value = "리뷰 목록 조회")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "direction", dataType = "string", paramType = "query", defaultValue = "DESC", value = "정렬 방향"),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", defaultValue = "0", value = "페이징 offset"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", defaultValue = "5", value = "조회 갯수")
    })
    public Response<Page<AdminReviewResponse>> findAllReviews(PageRequest pageRequest){
        List<AdminReviewResponse> result = reviewService.findAllDesc().stream()
                .map(AdminReviewResponse::new)
                .collect(Collectors.toList());
        return OK(
                new PageImpl<>(result, pageRequest.of(), result.size())
        );
    }

    @GetMapping("/review/{id}")
    @ApiOperation(value = "리뷰 단건(상세) 조회")
    public Response<ReviewDetailResponse> getReviewDetail(
            @PathVariable @ApiParam(value = "조회 대상 리뷰 PK") String id){
        return OK(
                new ReviewDetailResponse(reviewService.findById(Id.of(Review.class, id)))
        );
    }

    @PatchMapping("/review/{id}/payReserves")
    @ApiOperation(value = "리뷰 적립금 지급")
    public Response<Review> payReviewReserves(
            @PathVariable @ApiParam(value = "조회 대상 리뷰 PK") String id, @RequestParam int amount){
        return OK(
                reviewService.payReserves(Id.of(Review.class, id), amount)
        );
    }

    @PostMapping("/review/{reviewId}/comment")
    @ApiOperation(value = "리뷰 코멘트 작성")
    public Response<ReviewComment> commentReview(
            @PathVariable @ApiParam(value = "조회 대상 리뷰 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String reviewId,
            @RequestParam String content,
            @AuthenticationPrincipal Authentication authentication) {
        return OK(
                reviewCommentService.write(
                        Id.of(Review.class, reviewId),
                        Id.of(Admin.class, ((User)authentication.getDetails()).getId()),
                        new ReviewComment(content)
                )
        );
    }

    /*@PutMapping("/review/comment/{commentId}")
    @ApiOperation(value = "리뷰 코멘트 수정")
    public Response<ReviewComment> modifyReviewComment(
            @PathVariable @ApiParam(value = "조회 대상 리뷰 PK", example = "3a18e633a5db4dbd8aaee218fe447fa4") String reviewId,
            @PathVariable @ApiParam(value = "조회 대상 코멘트 PK", example = "c7bb4cb6efcd4f4bb388eafb6fa52fac") String commentId,
            @RequestParam String content,
            @AuthenticationPrincipal Authentication authentication) {
        return OK(
                reviewCommentService.modifyContent(
                        Id.of(Review.class, reviewId),
                        Id.of(ReviewComment.class, commentId),
                        content,
                        Id.of(Admin.class, ((User)authentication.getDetails()).getId())
                )
        );
    }*/
}
