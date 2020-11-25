package my.myungjin.academyDemo.domain.member;

import my.myungjin.academyDemo.service.member.MemberService;
import my.myungjin.academyDemo.util.Util;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;


@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class MemberServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MemberService memberService;

    private String id;
    private String userId;
    private String password;

    @BeforeAll
    void setup(){
        id = Util.getUUID();
        userId = "test";
        password = "mjkim_password";
    }

    @Test
    @Order(1)
    void 사용자_가입(){
        String name = "테스트";
        String tel = "010-1234-5678";
        String addr1 = "XX시 OO구 XXXX로 12";
        String addr2 = "1-1111";

        Member newMember = Member.builder()
                .id(id)
                .userId(userId)
                .password(password)
                .name(name)
                .tel(tel)
                .addr1(addr1)
                .addr2(addr2)
                .build();
        Member saved = memberService.join(newMember);
        assertThat(saved, is(notNullValue()));
        log.info("Saved Member: {}", saved);

    }

}
