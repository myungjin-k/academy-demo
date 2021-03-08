package my.myungjin.academyDemo.service.item;

import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@Slf4j
@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;

    @Test
    @Order(1)
    void 쇼핑몰_전시상품_상세정보를_조회한다 (){
        ItemDisplay item = itemService.findByIdWithOptions(Id.of(ItemDisplay.class, "6bdbf6eea40b425caae4410895ca4809"));
        assertThat(item, is(notNullValue()));
        log.info("Item : {}", item);
    }
}
