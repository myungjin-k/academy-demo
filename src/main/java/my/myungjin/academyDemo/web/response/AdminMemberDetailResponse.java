package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.ReservesHistory;

import java.time.format.DateTimeFormatter;
import java.util.List;

@ToString
@Getter
public class AdminMemberDetailResponse {

    private String id;

    private String userId;

    private String name;

    private String createAt;

    private String email;

    private String tel;

    private String addr1;

    private String addr2;

    private String rating;

    private int orderAmount;

    private int reserves;

    private List<ReservesHistory> reservesHistories;

    public AdminMemberDetailResponse(Member entity) {
        this.id = entity.getId();
        this.userId = entity.getUserId();
        this.name = entity.getName();
        this.createAt = entity.getCreateAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));
        this.email = entity.getEmail();
        this.tel = entity.getTel();
        this.addr1 = entity.getAddr1();
        this.addr2 = entity.getAddr2();
        this.rating = entity.getRating().name();
        this.orderAmount = entity.getOrderAmount();
        this.reserves = entity.getReserves();
        this.reservesHistories = entity.getReservesHistories();
    }
}
