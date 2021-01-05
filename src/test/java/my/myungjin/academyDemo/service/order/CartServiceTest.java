package my.myungjin.academyDemo.service.order;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.CartItem;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest // H2 데이터베이스를 자동으로 실행
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CartServiceTest {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private CartService cartService;

    private Id<Member, String> memberId;

    private Id<ItemDisplay.ItemDisplayOption, String> itemId;

    private Id<CartItem, String> cartItemId;

    @BeforeAll
    void setup(){
        memberId = Id.of(Member.class, "3a18e633a5db4dbd8aaee218fe447fa4");
        itemId = Id.of(ItemDisplay.ItemDisplayOption.class, "91cc1c18f11e4d018566524b51d8419a");
    }

    @Test
    @Sql("/db/order-data-setup.sql")
    @Order(1)
    void 장바구니_추가하기() {
        CartItem added = cartService.add(memberId, memberId, itemId, 2);
        cartItemId = Id.of(CartItem.class, added.getId());
        assertThat(added, is(notNullValue()));
        log.info("Added Item: {}", added);
    }

    @Test
    @Order(2)
    void 장바구니_조회하기() {
        List<CartItem> cart = cartService.findByMember(memberId, memberId);
        //assertThat(cart.size(), is(1));
        log.info("Cart: {}", cart);
    }

    @Test
    @Order(3)
    void 장바구니_상품_수량_수정하기() {
        CartItem updated = cartService.modify(memberId, memberId, cartItemId, 10);
        assertThat(updated, is(notNullValue()));
        log.info("Updated CartItem: {}", updated);
    }



    @Test
    @Order(4)
    void 장바구니_추가하기_아이템_중복() {
        CartItem added = cartService.add(memberId, memberId, itemId, 2);
        cartItemId = Id.of(CartItem.class, added.getId());
        assertThat(added, is(notNullValue()));
        assertThat(added.getCount(), is(12));
        log.info("Added Item: {}", added);
    }

    @Test
    @Order(5)
    void 장바구니_상품_삭제하기() {
        CartItem deleted = cartService.delete(memberId, memberId, cartItemId);
        assertThat(deleted, is(notNullValue()));
        log.info("Deleted CartItem: {}", deleted);
    }
}
