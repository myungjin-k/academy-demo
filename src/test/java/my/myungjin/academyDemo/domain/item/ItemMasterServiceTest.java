package my.myungjin.academyDemo.domain.item;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.service.item.ItemMasterService;
import my.myungjin.academyDemo.util.Util;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static my.myungjin.academyDemo.commons.AttachedFile.toAttachedFile;
import static org.apache.commons.io.IOUtils.toByteArray;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ItemMasterServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private ItemMasterService itemMasterService;

    private Id<CommonCode, String> categoryId;

    private Id<ItemMaster, String> itemMasterId;

    @BeforeAll
    void setup(){
        categoryId = Id.of(CommonCode.class, "5105a5a02b754b4f9975975c1f1f58ea");
    }

    @Test
    @Order(1)
    void 상품_가져오기_메인카테고리(){
        List<ItemMaster> itemList = itemMasterService.findByCategory(categoryId);
        assertThat(itemList, is(notNullValue()));
        //log.info("Saved Member: {}", saved);

    }

    @Test
    @Order(2)
    void 상품_등록하기() throws IOException {
        Id<ItemMaster, String> itemMasterId = Id.of(ItemMaster.class, Util.getUUID());
        this.itemMasterId = itemMasterId;
        ItemMaster newItem = ItemMaster.builder()
                .id(itemMasterId.value())
                .itemName("데어 워머 터틀넥 티셔츠 (3color)")
                .price(19000)
                .build();
        URL testThumbnail = getClass().getResource("/logo.png");
        File file = new File(testThumbnail.getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile =
                new MockMultipartFile("file", file.getName(), "image/jpeg", toByteArray(input));
        ItemMaster saved = itemMasterService.saveItemMaster(categoryId, newItem, toAttachedFile(multipartFile));
        assertThat(saved, is(notNullValue()));
        log.info("Saved Item: {}", saved);

    }

    @Test
    @Order(3)
    void 상품_수정하기() throws IOException {

        URL testThumbnail = getClass().getResource("/item1.jpg");
        File file = new File(testThumbnail.getFile());
        FileInputStream input = new FileInputStream(file);
        MultipartFile multipartFile =
                new MockMultipartFile("file", file.getName(), "image/jpeg", toByteArray(input));
        ItemMaster updated = itemMasterService.modifyItemMaster(
                itemMasterId,
                categoryId,
                "데어 워머 터틀넥 티셔츠 (3color)",
                12900,
                toAttachedFile(multipartFile)
        );
        assertThat(updated, is(notNullValue()));
        log.info("Updated Item: {}", updated);

    }
    @Test
    @Order(4)
    void 상품_삭제하기() {
        ItemMaster deleted = itemMasterService.deleteItemMasterById(itemMasterId);
        assertThat(deleted, is(notNullValue()));
        log.info("Delete item: {}", deleted);
    }

}