package my.myungjin.academyDemo.service.order;

import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplayOption;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.CartItem;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.web.request.CartRequest;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.ArrayList;
import java.util.Arrays;
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

    private Id<ItemDisplayOption, String> itemId;

    private Id<ItemDisplayOption, String> itemId2;


    private Id<CartItem, String> cartItemId;

    @BeforeAll
    void setup(){
        memberId = Id.of(Member.class, "3a18e633a5db4dbd8aaee218fe447fa4");
        itemId = Id.of(ItemDisplayOption.class, "91cc1c18f11e4d018566524b51d8419a");
        itemId2 = Id.of(ItemDisplayOption.class, "invalid");
    }

    @Test
    @Order(1)
    void 장바구니_추가하기() {
        List<CartRequest> requestList = Arrays.asList(
                new CartRequest(itemId.value(), 2)
                //new CartRequest(itemId2.value(), 100) // 트랜잭션 확인용
        );

        List<CartItem> added = new ArrayList<>();
        try {
            added = cartService.addItems(memberId, memberId, requestList);
        } catch (NotFoundException e){
            log.warn(e.getMessage());
        } finally {
            cartItemId = Id.of(CartItem.class, added.get(0).getId());
            assertThat(added, is(notNullValue()));

            cartService.findByMember(memberId, memberId).forEach(
                item -> log.info("Cart Item: {}, count: {}", item.getItemOption().getId(), item.getCount())
            );
        }
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
        List<CartRequest> requestList = Arrays.asList(
                new CartRequest(itemId.value(), 2)
        );

        List<CartItem> added = cartService.addItems(memberId, memberId, requestList);
        assertThat(added, is(notNullValue()));
        assertThat(added.get(0).getCount(), is(12));
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
