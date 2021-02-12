package my.myungjin.academyDemo.web.controller;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.service.review.ReviewCommentService;
import my.myungjin.academyDemo.service.review.ReviewService;
import my.myungjin.academyDemo.web.Response;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public Response<Page<Review>> findAllReviews(
            @PathVariable @ApiParam(value = "조회 대상 전시상품 PK", example = "f23ba30a47194a2c8a3fd2ccadd952a4") String itemId,
            PageRequest pageRequest){
        return OK(
                reviewService.findAllDesc(pageRequest.of())
        );
    }

}
