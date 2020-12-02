package my.myungjin.academyDemo.domain.item;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.service.item.ItemService;
import my.myungjin.academyDemo.util.Util;
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
public class ItemServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemService itemService;

    private Id<CodeGroup, String> mainCategoryId;


    @BeforeAll
    void setup(){
        mainCategoryId = Id.of(CodeGroup.class, "5105a5a02b754b4f9975975c1f1f58ea");
    }

    @Test
    @Order(1)
    void 상품_가져오기_메인카테고리(){
        List<ItemMaster> itemList = itemService.findByMainCategory(mainCategoryId);
        assertThat(itemList, is(notNullValue()));
        //log.info("Saved Member: {}", saved);

    }
}
