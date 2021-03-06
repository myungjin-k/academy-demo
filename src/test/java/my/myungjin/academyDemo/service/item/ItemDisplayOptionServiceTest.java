package my.myungjin.academyDemo.service.item;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayOption;
import my.myungjin.academyDemo.domain.item.ItemStatus;
import my.myungjin.academyDemo.service.admin.item.ItemDisplayOptionService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.not;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemDisplayOptionServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemDisplayOptionService itemDisplayOptionService;

    private Id<ItemDisplay, String> itemDisplayId;

    private Id<ItemDisplayOption, String> itemDisplayOptionId;

    @BeforeAll
    void setup(){
        itemDisplayId = Id.of(ItemDisplay.class, "f23ba30a47194a2c8a3fd2ccadd952a4");
    }

    @Test
    @Order(1)
    void 상품_옵션_등록하기() {
        ItemDisplayOption newOption = ItemDisplayOption.builder()
                .color("마론핑크")
                .size("S")
                .status(ItemStatus.ON_SALE)
                .build();
        ItemDisplayOption saved = itemDisplayOptionService.add(itemDisplayId, newOption);
        this.itemDisplayOptionId = Id.of(ItemDisplayOption.class, saved.getId());
        assertThat(saved, is(notNullValue()));
        log.info("Saved Option: {}", saved);

    }

    @Test
    @Order(2)
    void 상품_옵션_마스터별로_조회하기() {
        List<ItemDisplayOption> options = itemDisplayOptionService.findAllByMasterId(itemDisplayId);
        assertThat(options.size(), is(5));
        log.info("Found Options: {}", options);
    }

    @Test
    @Order(3)
    void 상품_옵션_수정하기() {
        ItemDisplayOption updated = itemDisplayOptionService.modify(itemDisplayOptionId, "라이트카키", "ONE SIZE", ItemStatus.ON_SALE);
        assertThat(updated, is(notNullValue()));
        log.info("Updated Option: {}", updated);
    }

    @Test
    @Order(4)
    void 상품_옵션_삭제하기() {
        ItemDisplayOption deleted = itemDisplayOptionService.deleteById(itemDisplayOptionId);
        assertThat(deleted, is(notNullValue()));
        log.info("Deleted Option: {}", deleted);

        List<ItemDisplayOption> options = itemDisplayOptionService.findAllByMasterId(itemDisplayId);
        assertThat(options.size(), is(4));
    }

    @Test
    @Order(5)
    void 상품_옵션_검색하기() {
        List<String> result = itemDisplayOptionService.search(Id.of(ItemDisplay.class, ""), "팬츠")
                .stream()
                .map(displayOption ->
                        displayOption.getItemDisplay().getItemDisplayName()
                        .concat(displayOption.getColor())
                        .concat(displayOption.getSize()))
                .collect(Collectors.toList());

        assertThat(result, is(notNullValue()));
        log.info("Found Option: {}", result);
    }
}
