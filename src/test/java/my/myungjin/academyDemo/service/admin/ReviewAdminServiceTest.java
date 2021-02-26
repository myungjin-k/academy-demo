package my.myungjin.academyDemo.service.admin;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.ReservesHistoryRepository;
import my.myungjin.academyDemo.domain.member.ReservesType;
import my.myungjin.academyDemo.domain.order.OrderItem;
import my.myungjin.academyDemo.domain.review.Review;
import my.myungjin.academyDemo.service.review.ReviewService;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.assertj.core.util.Lists.emptyList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReviewAdminServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ReviewAdminService reviewAdminService;

    @Autowired
    private ReservesHistoryRepository reservesHistoryRepository;

    private Id<Review, String> reviewId;

    @BeforeAll
    void setup(){
        reviewId = Id.of(Review.class, "54ef07aad6864e5488d790dd88708be0");
    }

    @Test
    @Order(1)
    void 리뷰_조회하기() {
        Review found = reviewAdminService.findById(reviewId);

        assertThat(found, is(not(emptyList())));
        log.info("Found Review : {}", found);
    }

    @Test
    @Order(2)
    void 리뷰_적립금_지급하기() {
        Review review = reviewAdminService.payReserves(reviewId, 1000);
        assertThat(review, is(notNullValue()));
        log.info("Review Writer: {}", review.getMember());
        boolean chk = reservesHistoryRepository.existsByTypeAndRefId(ReservesType.REVIEW, reviewId.value());
        assertThat(chk, is(true));
    }

    @Test
    @Order(3)
    void 리뷰_검색하기() {
        List<Review> results = reviewAdminService.search(null, null, "REPLIED");
        //assertThat(results.size(), not(0));
        log.info("Review Search Results: {}", results);
    }
}
