package my.myungjin.academyDemo.service.review;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.OrderItem;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.util.Util;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)

public class ReviewServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReviewService reviewService;

    private Id<Member, String> memberId;

    private Id<ItemDisplay, String> itemId;

    private Id<OrderItem, String> orderItemId;

    private Id<Review, String> reviewId;

    @BeforeAll
    void setup(){
        memberId = Id.of(Member.class, "3a18e633a5db4dbd8aaee218fe447fa4");
        itemId = Id.of(ItemDisplay.class, "f23ba30a47194a2c8a3fd2ccadd952a4");
        orderItemId = Id.of(OrderItem.class, "c7bb4cb6efcd4f4bb388eafb6fa52fac");
    }


    @Test
    @Order(1)
    void 리뷰_작성하기() throws IOException {
        reviewId = Id.of(Review.class, Util.getUUID());
        Review review = Review.builder()
                .id(reviewId.value())
                .score(4)
                .content(randomAlphabetic(100))
                .build();
        URL fileUrl = getClass().getResource("/review.jpg");
        File file = new File(fileUrl.getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile =
                new MockMultipartFile("file", file.getName(), "image/jpeg", toByteArray(fileInputStream));
        Review saved = reviewService.write(memberId, memberId, orderItemId, review, toAttachedFile(multipartFile));
        assertThat(saved, is(notNullValue()));
        log.info("Saved Review: {}", saved);
    }

    @Test
    @Order(2)
    void 리뷰_조회하기_상품별로() {

        PageRequest request = new PageRequest();
        request.setPage(0);
        request.setSize(5);
        request.setDirection(Sort.Direction.DESC);

        Page<Review> results = reviewService.findAllByItem(itemId, request.of());

        assertThat(results, is(notNullValue()));
        log.info("Results : {}", results.getContent());
    }

    @Test
    @Order(3)
    void 리뷰_조회하기_회원과_아이템으로() {

        Review found = reviewService.findByMemberAndItem(memberId, orderItemId);
        assertThat(found, is(notNullValue()));
        log.info("Result : {}", found);
    }

    @Test
    @Order(4)
    void 리뷰_수정하기() throws IOException {
        Review review = Review.builder()
                .id(reviewId.value())
                .score(5)
                .content(randomAlphabetic(100))
                .build();
        URL fileUrl = getClass().getResource("/review.jpg");
        File file = new File(fileUrl.getFile());
        FileInputStream fileInputStream = new FileInputStream(file);
        MultipartFile multipartFile =
                new MockMultipartFile("file", file.getName(), "image/jpeg", toByteArray(fileInputStream));
        Review updated = reviewService.modify(
                memberId,
                memberId,
                reviewId,
                randomAlphabetic(100),
                5,
                toAttachedFile(multipartFile));
        assertThat(updated, is(notNullValue()));
        log.info("Updated CartItem: {}", updated);
    }

    @Test
    @Order(5)
    void 리뷰_적립금_업데이트() {
        Member member = reviewService.updateReserves(memberId, 0, 1000);
        assertThat(member, notNullValue());
        log.info("Member: {}", member);
    }

}
