package my.myungjin.academyDemo.domain.common;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.service.sample.SampleService;
import my.myungjin.academyDemo.util.Util;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.core.Is.is;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SampleServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private SampleService commonService;

    private String groupCode;
    private String groupNameEng;
    private String groupNameKor;
    private String groupId;

    @BeforeAll
    void setUp() {
        groupId = Util.getUUID();
        groupCode = "999";
        groupNameEng = "TEST";
        groupNameKor = "테스트";
    }
    @Test
    @Order(1)
    void 코드그룹_작성하기(){
        LocalDateTime now = LocalDateTime.now();
        CodeGroup codeGroup = CodeGroup.builder()
                .id(groupId)
                .code(groupCode)
                .nameEng(groupNameEng)
                .nameKor(groupNameKor)
                .createAt(now)
                .updateAt(now)
                .build();

        CodeGroup result = commonService.saveGroup(codeGroup);

        //then
        MatcherAssert.assertThat(result, is(notNullValue()));
        log.info("Written codeGroup: {}", result);
    }

    @Test
    @Order(2)
    void 코드그룹_불러오기(){

        List<CodeGroup> codeGroups = commonService.findAllGroups();

        //then
        MatcherAssert.assertThat(codeGroups, is(notNullValue()));
        //MatcherAssert.assertThat(codeGroups.size(), is(1));
        log.info("Read codeGroup: {}", codeGroups.get(0));
    }


    @Test
    @Order(3)
    void 코드그룹_수정하기(){
        String id = groupId;
        String code = "XXX";
        String nameEng = "EMPTY";
        String nameKor = "빈값";

        CodeGroup updated = commonService.modifyGroup(id, code, nameEng, nameKor);
        MatcherAssert.assertThat(updated.getId(), is(id));
        MatcherAssert.assertThat(updated.getCode(), is(code));
        MatcherAssert.assertThat(updated.getNameEng(), is(nameEng));
        MatcherAssert.assertThat(updated.getNameKor(), is(nameKor));

        log.info("Updated codeGroup: {}", updated);
    }

    @Test
    @Order(4)
    void 공통코드_작성하기(){
        LocalDateTime now = LocalDateTime.now();
        CommonCode commonCode = CommonCode.builder()
                .id(Util.getUUID())
                .code("XXX100")
                .nameEng("EMPTY_100")
                .nameKor("빈값_100")
                .groupId(groupId)
                .createAt(now)
                .updateAt(now)
                .build();
        CommonCode result = commonService.saveCommonCode(commonCode);

        //then
        MatcherAssert.assertThat(result, is(notNullValue()));
        log.info("Written commonCode: {}", result);
    }


    @Test
    @Order(5)
    void 공통코드_조회하기(){
        List<CommonCode> commonCodes = commonService.findAllCommonCodesByGroupId(Id.of(CodeGroup.class, groupId));
        MatcherAssert.assertThat(commonCodes.size(), is(1));
    }
}
