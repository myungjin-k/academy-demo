package my.myungjin.academyDemo.domain.common;

import my.myungjin.academyDemo.service.sample.SampleService;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.not;
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
        groupCode = "999";
        groupNameEng = "TEST";
        groupNameKor = "테스트";
    }
    @Test
    @Order(1)
    void 코드그룹_작성하기(){
        CodeGroup codeGroup = new CodeGroup.CodeGroupBuilder()
                .code(groupCode)
                .nameEng(groupNameEng)
                .nameKor(groupNameKor)
                .build();

        CodeGroup result = commonService.saveGroup(codeGroup);
        groupId = result.getId();

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
        CodeGroup group = commonService.findGroupByCode(groupCode).orElse(null);
        MatcherAssert.assertThat(group, is(notNullValue()));
        String id = group.getId();
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
}
