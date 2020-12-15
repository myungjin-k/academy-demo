package my.myungjin.academyDemo.domain.item;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.service.item.ItemOptionService;
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
public class ItemOptionServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemOptionService itemOptionService;

    private Id<ItemMaster, String> itemMasterId;

    private Id<ItemOption, String> itemOptionId;

    @BeforeAll
    void setup(){
        itemMasterId = Id.of(ItemMaster.class, "8c1cbb792b8d447e9128d53920cf9366");
    }

    @Test
    @Order(1)
    void 상품_옵션_등록하기() {
        this.itemOptionId = Id.of(ItemOption.class, Util.getUUID());
        ItemOption newOption = ItemOption.builder()
                .id(itemOptionId.value())
                .color("멜란지그레이")
                .size("ONE SIZE")
                .build();
        ItemOption saved = itemOptionService.add(itemMasterId, newOption);
        assertThat(saved, is(notNullValue()));
        log.info("Saved Option: {}", saved);

    }

    @Test
    @Order(2)
    void 상품_옵션_마스터별로_조회하기() {
        List<ItemOption> options = itemOptionService.findAllByMasterId(itemMasterId);
        assertThat(options.size(), is(3));
        log.info("Found Options: {}", options);
    }

    @Test
    @Order(3)
    void 상품_옵션_수정하기() {
        ItemOption updated = itemOptionService.modify(itemOptionId, "라이트카키", "ONE SIZE");
        assertThat(updated, is(notNullValue()));
        log.info("Updated Option: {}", updated);
    }

    @Test
    @Order(4)
    void 상품_옵션_삭제하기() {
        Id<ItemOption, String> deleted = itemOptionService.deleteById(itemOptionId);
        assertThat(deleted, is(notNullValue()));
        log.info("Deleted Option: {}", deleted);

        List<ItemOption> options = itemOptionService.findAllByMasterId(itemMasterId);
        assertThat(options.size(), is(2));
    }
}
