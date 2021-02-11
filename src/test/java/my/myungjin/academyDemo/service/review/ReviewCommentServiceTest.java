package my.myungjin.academyDemo.service.review;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Admin;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.domain.review.ReviewComment;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReviewCommentServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReviewCommentService reviewCommentService;

    private Id<Review, String> reviewId;

    private Id<Admin, String> adminId;

    private Id<ReviewComment, String> commentId;

    @BeforeAll
    void setup(){
        reviewId = Id.of(Review.class, "43f217fd86c34ce0a305e02b9972a29e");
        adminId = Id.of(Admin.class, "3a18e633a5db4dbd8aaee218fe447fa4");
    }


    @Test
    @Order(1)
    void 리뷰_코멘트를_작성한다() throws Exception {
        ReviewComment newComment = new ReviewComment("리뷰 감사합니다. 만족스러우셨다니 다행입니다. 500원 적립 도와드리겠습니다~!");
        ReviewComment written = reviewCommentService.write(reviewId, adminId, newComment);
        assertThat(written, is(notNullValue()));
        log.info("Written Comment : {}", written);
        commentId = Id.of(ReviewComment.class, written.getId());
    }

    @Test
    @Order(2)
    void 리뷰_코멘트를_조회한다() throws Exception {
        List<ReviewComment> comments = reviewCommentService.findAllByReview(reviewId);
        assertThat(comments.size(), is(1));
        log.info("Comments : {}", comments);
    }

    @Test
    @Order(3)
    void 리뷰_코멘트를_수정한다() throws Exception {
        String content = "수정됨";
        ReviewComment updated = reviewCommentService.modifyContent(reviewId, commentId, content, adminId);
        assertThat(updated, is(notNullValue()));
        assertThat(updated.getContent(), is(content));
        log.info("Updated Comment : {}", updated);
    }

    @Test
    @Order(4)
    void 리뷰_코멘트를_삭제한다() throws Exception {
        ReviewComment deleted = reviewCommentService.delete(reviewId, commentId);
        assertThat(deleted, is(notNullValue()));

        Optional<ReviewComment> empty = reviewCommentService.findById(commentId);
        assertThat(empty, is(Optional.empty()));
    }
}
