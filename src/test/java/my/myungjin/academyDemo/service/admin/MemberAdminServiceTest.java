package my.myungjin.academyDemo.service.admin;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.ReservesHistory;
import my.myungjin.academyDemo.domain.member.ReservesHistoryRepository;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberAdminServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemberAdminService memberService;

    @Autowired
    private ReservesHistoryRepository reservesHistoryRepository;

    private Id<Member, String> id;

    @BeforeAll
    void setup(){
        id = Id.of(Member.class, "3a18e633a5db4dbd8aaee218fe447fa4");
    }

    @Test
    @Order(1)
    void 회원을_검색한다() {
        List<Member> results = memberService.search(null, "mjkim");
        assertThat(results, is(notNullValue()));
        log.info("Results: {}", results);
    }

    @Test
    @Order(2)
    void 회원_상세정보를_조회한다() {
        Member member = memberService.findByIdWithReservesHistory(id);
        assertThat(member, is(notNullValue()));
        log.info("Member: {}", member);
    }

    @Test
    @Order(3)
    void 회원_적립금을_수기_지급한다() {
        Member member = memberService.updateReserves(id, 0, 1000);
        assertThat(member, is(notNullValue()));
        log.info("Member: {}", member);


        List<ReservesHistory> chk = reservesHistoryRepository.findByMemberOrderByCreateAtDesc(member);
        log.info("Member Reserves History: {}", chk);
    }


}
