package my.myungjin.academyDemo.service.member;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.util.Util;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;


@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemberService memberService;

    private Id<Member, String> id;
    private String userId;
    private String password;

    @BeforeAll
    void setup(){
        userId = "test";
        password = "mjkim_password";

    }

    @Test
    @Order(1)
    void 사용자_가입(){
        String name = "테스트";
        String email = "rla_mj@naver.com";
        String tel = "010-2345-5678";
        String addr1 = "XX시 OO구 XXXX로 12";
        String addr2 = "1-1111";

        Member newMember = Member.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .tel(tel)
                .addr1(addr1)
                .addr2(addr2)
                .build();
        Member saved = memberService.join(newMember);
        id = Id.of(Member.class, saved.getId());
        assertThat(saved, is(notNullValue()));
        log.info("Saved Member: {}", saved);

    }

    @Test
    @Order(2)
    void 사용자_아이디_찾기(){
        String tel = "010-2345-5678";

        String found = memberService.findUserId(tel).orElse("");
        assertThat(found, is(notNullValue()));
        assertThat(found, not(emptyString()));
        assertThat(found, is(userId));
        log.info("Found Member Id: {}", found);

    }
    @Test
    @Order(3)
    void 사용자_아이디_찾기_사용자_없음(){
        String tel = "010-2345-7890";

        String found = memberService.findUserId(tel).orElse("");
        assertThat(found, is(emptyString()));

    }
    // TODO Unexpected Rollback : rollback-only 예외
    /*@Test
    @Order(4)
    void 사용자_비밀번호_찾기(){
        String email = "rla_mj@naver.com";

        String found = memberService.findPassword(email).orElse("");
        assertThat(found, is(notNullValue()));
        assertThat(found, is(email));

    }*/
    @Test
    @Order(5)
    void 사용자_비밀번호_찾기_사용자_없음(){
        String email = "open7894.v3@gmail.com";

        String found = memberService.findPassword(email).orElse("");
        assertThat(found, is(emptyString()));

    }
    @Test
    @Order(6)
    void 사용자_비밀번호_변경(){
        String newPwd = "new_password";
        Member modified = memberService.modifyPassword(id, newPwd);
        assertThat(modified, is(notNullValue()));
        log.info("Modified Member: {}", modified);

        String logined = memberService.login(userId, newPwd);
        assertThat(logined, is(notNullValue()));
    }

    @Test
    @Order(7)
    void 사용자_정보_조회(){

        Member myInfo = memberService.findMyInfo(id);
        assertThat(myInfo, is(notNullValue()));
        log.info("{}", myInfo);
    }

    @Test
    @Order(8)
    void 사용자_정보_변경(){

        String name = "테스트";
        String email = "rla_mj@naver.com";
        String tel = "010-2345-5678";
        String addr1 = "XX시 OO구 XXXX로 12";
        String addr2 = "1-1111";

        Member member = Member.builder()
                .id(id.value())
                .userId(userId)
                .password("new_password")
                .name(name)
                .email(email)
                .tel(tel)
                .addr1(addr1)
                .addr2(addr2)
                .build();

        Member modified = memberService.modify(id, member);
        assertThat(modified, is(notNullValue()));
        log.info("Modified Member: {}", modified);
    }

    @Test
    @Order(9)
    void 리뷰_적립금_업데이트() {
        Member member = memberService.updateReserves(id, 0, 1000);
        assertThat(member, notNullValue());
        log.info("Member: {}", member);
    }
}
