package my.myungjin.academyDemo.domain.common;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.service.admin.CommonCodeService;
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
public class CommonCodeServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private CommonCodeService commonService;

    private String groupCode;
    private String groupNameEng;
    private String groupNameKor;
    private Id<CodeGroup, String> groupId;
    private Id<CommonCode, String> codeId;

    @BeforeAll
    void setUp() {
        groupId = Id.of(CodeGroup.class, Util.getUUID());
        groupCode = "999";
        groupNameEng = "TEST";
        groupNameKor = "테스트";
        codeId = Id.of(CommonCode.class, Util.getUUID());
    }
    @Test
    @Order(1)
    void 코드그룹_작성하기(){
        LocalDateTime now = LocalDateTime.now();
        CodeGroup codeGroup = CodeGroup.builder()
                .id(groupId.value())
                .code(groupCode)
                .nameEng(groupNameEng)
                .nameKor(groupNameKor)
                .updateAt(now)
                .build();

        CodeGroup result = commonService.registGroup(codeGroup);

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
        String code = "XXX";
        String nameEng = "EMPTY";
        String nameKor = "빈값";

        CodeGroup updated = commonService.modifyGroup(groupId.value(), code, nameEng, nameKor);
        MatcherAssert.assertThat(updated.getId(), is(groupId.value()));
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
                .id(codeId.value())
                .code("XXX100")
                .nameEng("EMPTY_100")
                .nameKor("빈값_100")
                .groupId(groupId.value())
                .updateAt(now)
                .build();
        CommonCode result = commonService.registCommonCode(groupId, commonCode);

        //then
        MatcherAssert.assertThat(result, is(notNullValue()));
        log.info("Written commonCode: {}", result);
    }

    @Test
    @Order(5)
    void 공통코드_조회하기(){
        CodeGroup group = commonService.findAllCommonCodesByGroupId(groupId);
        log.info("Group: {}", group);
        MatcherAssert.assertThat(group, is(notNullValue()));
        MatcherAssert.assertThat(group.getCommonCodes().size(), is(1));
    }

    @Test
    @Order(6)
    void 공통코드_수정하기(){
        String code = "M";
        String nameEng = "MODIFIED";
        String nameKor = "수정";
        CommonCode updated = commonService.modifyCode(groupId, codeId, code, nameEng, nameKor);
        MatcherAssert.assertThat(updated, is(notNullValue()));
        log.info("Updated CommonCode: {}", updated);
    }


    @Test
    @Order(7)
    void 공통코드_삭제하기(){
        String deleted = commonService.removeCode(groupId, codeId);
        MatcherAssert.assertThat(deleted, is(notNullValue()));
        MatcherAssert.assertThat(deleted, is(codeId.value()));

        CodeGroup chk = commonService.findAllCommonCodesByGroupId(groupId);
        MatcherAssert.assertThat(chk.getCommonCodes().size(), is(0));

    }

    @Test
    @Order(8)
    void 코드그룹_삭제하기(){
        String deleted = commonService.removeGroup(groupId.value());
        MatcherAssert.assertThat(deleted, is(notNullValue()));
        MatcherAssert.assertThat(deleted, is(groupId.value()));
    }
}
