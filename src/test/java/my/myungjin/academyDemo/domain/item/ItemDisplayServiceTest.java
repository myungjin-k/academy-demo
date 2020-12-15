package my.myungjin.academyDemo.domain.item;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.service.item.ItemDisplayService;
import my.myungjin.academyDemo.util.Util;
import my.myungjin.academyDemo.web.request.PageRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemDisplayServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemDisplayService itemDisplayService;

    private Id<ItemMaster, String> itemMasterId;

    private Id<ItemDisplay, String> itemDisplayId;

    @BeforeAll
    void setup(){
        itemMasterId = Id.of(ItemMaster.class, "8c1cbb792b8d447e9128d53920cf9366");
    }

    @Test
    @Order(1)
    void 전체_전시_상품_가져오기() {
        PageRequest request = new PageRequest();
        request.setPage(0);
        request.setSize(5);
        request.setDirection(Sort.Direction.DESC);

        Page<ItemDisplay> found = itemDisplayService.findAll(request.of());
        assertThat(found, is(notNullValue()));
        log.info("Found Display Item: {}", found);
    }

    @Test
    @Order(2)
    void 전시_상품_등록하기() throws IOException {
        itemDisplayId = Id.of(ItemDisplay.class, Util.getUUID());
        ItemDisplay newDisplay = ItemDisplay.builder()
                .id(itemDisplayId.value())
                .itemDisplayName("보더 알파카 니트")
                .material("알파카86 나일론13 스판1")
                .salePrice(72000)
                .size("one size\n어깨 36 가슴 43.5 암홀 20 팔통 13.5 소매단 9 팔길이 56 총길이 49")
                .description("몽글몽글한 느낌의 폭닥한 알파카 부클 니트입니다.\n" +
                        "알파카86으로 살짝 드라이하면서도\n" +
                        "기분 좋은 정도의 보송한 헤어감이 느껴지는\n" +
                        "가볍고 부드러운 텍스쳐입니다.\n" +
                        "\n" +
                        "159 모델키 기준 골반을 적당히 덮는 크롭한 기장으로\n" +
                        "착용시 허리 라인을 타고 적당하게 고정되는 밑단은\n" +
                        "전체적인 아웃핏을 알맞게 잡아줍니다.\n" +
                        "\n" +
                        "스판이 적당량 함유되어 착용감이 편안합니다.\n" +
                        "밑단과 소매단의 골지 라인은 은은하게 포인트가 됩니다.\n" +
                        "\n" +
                        "목을 살짝 덮으며 올라오는 넥라인은\n" +
                        "한층 멋스러운 느낌을 주며 보온성 또한 좋습니다.\n" +
                        "\n" +
                        "데일리로 착용하기 좋은 은은한 크림빛의 아이보리 색상과\n" +
                        "약간의 붉은 빛이 도는 부드러운 베이지 색상 준비했습니다.\n" +
                        "\n" +
                        "*착용컷보다 디테일컷이 실제 색상과 더 유사합니다.\n" +
                        "(화이트보다 크림빛이 감도는 아이보리입니다.)\n" +
                        "*소재 특성상 잡실/털빠짐/냄새/까끌거림이 있을 수 있습니다.\n" +
                        "\n" +
                        "COLOR\n" +
                        "콘베이지-약간의 붉은빛이 도는 부드러운 베이지\n" +
                        "크림아이보리-은은한 크림 빛깔의 화사한 아이보리\n" +
                        "\n" +
                        "신축성-좋음｜두께감-톡톡함｜비침-없음｜촉감-적당함")
                .status(ItemStatus.READY_TO_SALE)
                .build();
        URL testThumbnail = getClass().getResource("/item_detail.jpg");
        File file = new File(testThumbnail.getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile =
                new MockMultipartFile("file", file.getName(), "image/jpeg", toByteArray(input));
        ItemDisplay saved = itemDisplayService.save(itemMasterId, newDisplay, toAttachedFile(multipartFile));
        assertThat(saved, is(notNullValue()));
        log.info("Saved Display Item: {}", saved);

    }

    @Test
    @Order(3)
    void 전시_상품_조회하기_아이디로() {
        ItemDisplay found = itemDisplayService.findById(itemDisplayId);
        assertThat(found, is(notNullValue()));
        log.info("Found Display Item: {}", found);
    }

    @Test
    @Order(4)
    void 전시_상품_수정하기() throws IOException {
        ItemDisplay tobeUpdated = ItemDisplay.builder()
                .id(itemDisplayId.value())
                .itemDisplayName("보더 알파카 니트")
                .material("알파카86 나일론13 스판1")
                .salePrice((int) (72000*0.8))
                .size("one size\n어깨 36 가슴 43.5 암홀 20 팔통 13.5 소매단 9 팔길이 56 총길이 49")
                .description("몽글몽글한 느낌의 폭닥한 알파카 부클 니트입니다.\n" +
                        "알파카86으로 살짝 드라이하면서도\n" +
                        "기분 좋은 정도의 보송한 헤어감이 느껴지는\n" +
                        "가볍고 부드러운 텍스쳐입니다.\n" +
                        "\n" +
                        "159 모델키 기준 골반을 적당히 덮는 크롭한 기장으로\n" +
                        "착용시 허리 라인을 타고 적당하게 고정되는 밑단은\n" +
                        "전체적인 아웃핏을 알맞게 잡아줍니다.\n" +
                        "\n" +
                        "스판이 적당량 함유되어 착용감이 편안합니다.\n" +
                        "밑단과 소매단의 골지 라인은 은은하게 포인트가 됩니다.\n" +
                        "\n" +
                        "목을 살짝 덮으며 올라오는 넥라인은\n" +
                        "한층 멋스러운 느낌을 주며 보온성 또한 좋습니다.\n" +
                        "\n" +
                        "데일리로 착용하기 좋은 은은한 크림빛의 아이보리 색상과\n" +
                        "약간의 붉은 빛이 도는 부드러운 베이지 색상 준비했습니다.\n" +
                        "\n" +
                        "*착용컷보다 디테일컷이 실제 색상과 더 유사합니다.\n" +
                        "(화이트보다 크림빛이 감도는 아이보리입니다.)\n" +
                        "*소재 특성상 잡실/털빠짐/냄새/까끌거림이 있을 수 있습니다.\n" +
                        "\n" +
                        "COLOR\n" +
                        "콘베이지-약간의 붉은빛이 도는 부드러운 베이지\n" +
                        "크림아이보리-은은한 크림 빛깔의 화사한 아이보리\n" +
                        "\n" +
                        "신축성-좋음｜두께감-톡톡함｜비침-없음｜촉감-적당함")
                .status(ItemStatus.ON_SALE)
                .build();
        URL testThumbnail = getClass().getResource("/item_detail.jpg");
        File file = new File(testThumbnail.getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile =
                new MockMultipartFile("file", file.getName(), "image/jpeg", toByteArray(input));
        ItemDisplay updated = itemDisplayService.modify(itemDisplayId, itemMasterId, tobeUpdated, toAttachedFile(multipartFile));
        assertThat(updated, is(notNullValue()));
        log.info("Updated Display Item: {}", updated);

    }


    @Test
    @Order(5)
    void 전시_상품_검색하기_페이징() {
        PageRequest request = new PageRequest();
        request.setPage(0);
        request.setSize(5);
        request.setDirection(Sort.Direction.DESC);
        Page<ItemDisplay> results = itemDisplayService.searchByNameAndCreateAt("니트", LocalDate.now(), null,  request.of());

        assertThat(results.getTotalElements(), is(1L));
        log.info("Result item: {}", results.getContent());
    }

    @Test
    @Order(6)
    void 전시_상품_삭제하기() {
        ItemDisplay deleted = itemDisplayService.deleteItemById(itemDisplayId);
        assertThat(deleted, is(notNullValue()));
        log.info("Deleted Display Item: {}", deleted);

        ItemDisplay found = itemDisplayService.findByItemMaster(itemMasterId).orElse(null);
        assertThat(found, is(Matchers.nullValue()));
    }
}
