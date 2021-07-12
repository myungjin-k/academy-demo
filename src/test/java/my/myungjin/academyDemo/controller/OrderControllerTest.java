package my.myungjin.academyDemo.controller;

import com.siot.IamportRestClient.request.CancelData;
import com.siot.IamportRestClient.response.IamportResponse;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.Order;
import my.myungjin.academyDemo.iamport.IamportClient;
import my.myungjin.academyDemo.security.EntryPointUnauthorizedHandler;
import my.myungjin.academyDemo.security.MyAccessDeniedHandler;
import my.myungjin.academyDemo.service.admin.AdminService;
import my.myungjin.academyDemo.service.member.MemberService;
import my.myungjin.academyDemo.service.order.CartService;
import my.myungjin.academyDemo.service.order.OrderService;
import my.myungjin.academyDemo.web.controller.OrderController;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@WebMvcTest(controllers = {OrderController.class},
            includeFilters = @ComponentScan.Filter(classes = {EnableWebSecurity.class}))
public class OrderControllerTest {

    private final Logger log = LoggerFactory.getLogger(getClass());


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EntryPointUnauthorizedHandler entryPointUnauthorizedHandler;

    @MockBean
    private MyAccessDeniedHandler accessDeniedHandler;

    @MockBean
    private MemberService memberService;

    @MockBean
    private AdminService adminService;

    @MockBean
    private CartService cartService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private IamportClient iamportClient;

    @Test
    @WithMockUser(roles = "MEMBER")
    public void 주문을_취소한다() throws Exception {

        Id<Member, String> memberId = Id.of(Member.class, "member_id");
/*        Order order = Order.builder()
                .orderName("aaa")
                .orderAddr1("AA시 BB구")
                .orderAddr2("1-111")
                .orderTel("010-2222-3333")
                .id("order_test_id")
                .paymentUid("pay_test_id")
                .totalAmount(1000)
                .build();
        OrderItem item = new OrderItem("order_item_id", 1);
        ItemDisplayOption option = new ItemDisplayOption("item_option_id", "S", "white", ItemStatus.ON_SALE);
        ItemDisplay display = new ItemDisplay("item_display_id", "test_item",
                1000, "-", "-", "-", "-", ItemStatus.ON_SALE, "-");
        ItemMaster master = new ItemMaster("item_master_id", "item_temp", 2000, "-", null, null);
        display.setItemMaster(master);
        option.setItemDisplay(display);
        item.setItemOption(option);
        order.addItem(item);

        Delivery delivery = Delivery.builder()
                .id("delivery_test_id")
                .receiverName(order.getOrderName())
                .receiverTel(order.getOrderTel())
                .receiverAddr1(order.getOrderAddr1())
                .receiverAddr2(order.getOrderAddr2())
                .status(DeliveryStatus.DELETED)
                .build();

        OrderRequest request = new OrderRequest(
                order.getOrderName(), order.getOrderEmail().orElse(null), order.getOrderTel(), order.getOrderAddr1(), order.getOrderAddr2(),
                order.getUsedPoints(),
        delivery.getReceiverName(), delivery.getReceiverTel(), delivery.getReceiverAddr1(), delivery.getReceiverAddr2(),
                1, delivery.getMessage(), List.of(option.getId()), null, order.getPaymentUid(),
                "", order.getTotalAmount(), order.getCouponDiscounted(), order.getItemDiscounted());*/

        Order order = Order.builder()
                .orderName("aaa")
                .orderAddr1("AA시 BB구")
                .orderAddr2("1-111")
                .orderTel("010-2222-3333")
                .id("order_test_id")
                .paymentUid("pay_test_id")
                .totalAmount(1000)
                .build();
        Id<Order, String> orderId = Id.of(Order.class, order.getId());
        when(orderService.cancel(memberId, orderId)).thenReturn(order);
        when(iamportClient.cancelPaymentByImpUid(new CancelData(order.getPaymentUid(), true))).thenReturn(new IamportResponse<>());

      mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/mall/member/" + memberId.value() + "/order/" + orderId.value() + "/cancel")
        ).andExpect(
                MockMvcResultMatchers.status().isOk()
        ).andExpect(
                MockMvcResultMatchers.jsonPath("$.response.id").value(orderId.value())
        ).andDo(MockMvcResultHandlers.print());
    }

}
