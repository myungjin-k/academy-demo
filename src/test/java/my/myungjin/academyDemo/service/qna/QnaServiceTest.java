package my.myungjin.academyDemo.service.qna;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Admin;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.qna.Qna;
import my.myungjin.academyDemo.domain.qna.QnaReply;
import my.myungjin.academyDemo.domain.qna.QnaStatus;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.Set;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@Slf4j
@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QnaServiceTest {

    @Autowired
    private QnaService qnaService;

    private Id<Member, String> memberId;

    private Id<Qna, Long> qnaSeq;

    private Id<Admin, String> adminId;

    private Id<QnaReply, String> replyId;

    @BeforeAll
    void setup() {
        memberId = Id.of(Member.class, "3a18e633a5db4dbd8aaee218fe447fa4");
        adminId = Id.of(Admin.class, "3a18e633a5db4dbd8aaee218fe447fa4");

    }

    @Test
    @Order(1)
    void 문의를_등록한다_상품(){

        Id<ItemDisplay, String> itemId = Id.of(ItemDisplay.class, "6bdbf6eea40b425caae4410895ca4809");
        // item
        Id<CommonCode, String> cateId = Id.of(CommonCode.class, "fe72f5c7a9a04442ae6d8eb767a8833b");

        Qna newQna = Qna.builder()
                .title("상품 문의")
                .content("test 문의")
                .secretYn('Y')
                .build();

        Qna saved = qnaService.ask(memberId, memberId, cateId, Optional.of(itemId), newQna, null);

        assertThat(saved, is(notNullValue()));
        qnaSeq = Id.of(Qna.class, saved.getSeq());
        log.info("Saved Qna : {}", saved);

    }

    @Test
    @Order(2)
    void 문의를_등록한다_주문 () throws IOException {

        // order
        Id<CommonCode, String> cateId = Id.of(CommonCode.class, "5c5b2a2b17e24f07ae647f45cdbda705");

        File file = new ClassPathResource("/qna.jpg").getFile(); // 컴파일 된 파일이어야 함
        MultipartFile multipartFile =
                new MockMultipartFile("file", file.getName(), "image/jpeg", toByteArray(new FileInputStream(file)));

        Qna newQna = Qna.builder()
                .title("배송 문의")
                .content("test 주문/배송 문의")
                .secretYn('Y')
                .build();

        Qna saved = qnaService.ask(memberId, memberId, cateId, Optional.empty(), newQna, toAttachedFile(multipartFile));

        assertThat(saved, is(notNullValue()));
        qnaSeq = Id.of(Qna.class, saved.getSeq());
        log.info("Saved Qna : {}", saved);

    }
    @Test
    @Order(3)
    void 문의를_조회한다_상태별 (){

        Set<Qna> results = qnaService.findByStatus(QnaStatus.WAITING);
        assertThat(results.size(), greaterThan(0));
        log.info("Qna Results : {}", results);

    }

    @Test
    @Order(4)
    void 문의를_조회한다_단건_잠금 (){

        Qna result = qnaService.findQnaWithSecretYn(memberId, qnaSeq);

        assertThat(result, is(notNullValue()));
        log.info("Qna Result : {}", result);

    }

    @Test
    @Order(4)
    void 문의_목록을_조회한다_카테고리별_잠금 (){

        // item
        Id<CommonCode, String> cateId = Id.of(CommonCode.class, "fe72f5c7a9a04442ae6d8eb767a8833b");
        Id<ItemDisplay, String> itemId = Id.of(ItemDisplay.class, "6bdbf6eea40b425caae4410895ca4809");
        // temp member id
        Id<Member, String> wrongMemberId = Id.of(Member.class, "WRONG");
        try {
            Set<Qna> results = qnaService.findByCategoryWithSecretYn(wrongMemberId, cateId, Optional.of(itemId));
            log.info("Qna Results : {}", results);
        } catch (IllegalArgumentException e) {
            log.warn("Qna Search Exception: {}", e.getMessage());
        }
    }


    @Test
    @Order(5)
    void 문의를_수정한다_상품 (){

        // order
        Id<CommonCode, String> cateId = Id.of(CommonCode.class, "fe72f5c7a9a04442ae6d8eb767a8833b");

        Qna newQna = Qna.builder()
                .title("[수정] 상품 문의")
                .content("[수정] test 상품 문의")
                .secretYn('Y')
                .build();

        Qna updated = qnaService.modify(memberId, memberId, qnaSeq, cateId, newQna, null);

        assertThat(updated, is(notNullValue()));
        log.info("Updated Qna : {}", updated);

    }

    @Test
    @Order(6)
    void 문의_답글을_등록한다(){

        QnaReply newQnaReply = QnaReply.builder()
                .title("RE: 상품 문의")
                .content("test 문의 답변")
                .secretYn('Y')
                .build();

        QnaReply saved = qnaService.reply(qnaSeq, adminId, newQnaReply);

        assertThat(saved, is(notNullValue()));
        replyId = Id.of(QnaReply.class, saved.getId());
        log.info("Saved Qna Reply : {}", saved);

    }

    @Test
    @Order(7)
    void 문의_답글을_수정한다(){

        QnaReply newQnaReply = QnaReply.builder()
                .title("RE: [수정] 상품 문의")
                .content("[수정] test 문의 답변")
                .secretYn('Y')
                .build();

        QnaReply updated = qnaService.modifyReply(adminId, qnaSeq, replyId, newQnaReply);

        assertThat(updated, is(notNullValue()));
        log.info("Updated Qna Reply : {}", updated);

    }

    @Test
    @Order(8)
    void 문의_답글을_삭제한다(){

        QnaReply deleted = qnaService.deleteReply(adminId, qnaSeq, replyId);

        assertThat(deleted, is(notNullValue()));
        log.info("Deleted Qna Reply: {}", deleted);

    }

    @Test
    @Order(9)
    void 문의를_삭제한다_상품 (){
        Qna deleted = qnaService.delete(memberId, memberId, qnaSeq);

        assertThat(deleted, is(notNullValue()));
        log.info("Deleted Qna : {}", deleted);

    }
}
