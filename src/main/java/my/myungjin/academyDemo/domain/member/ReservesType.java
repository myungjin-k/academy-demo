package my.myungjin.academyDemo.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ReservesType {

    ORDER("주문 적립"),
    ORDER_CANCEL("주문 취소"),
    ORDER_USED("주문 사용"),
    REVIEW("리뷰 적립"),
    EVENT("이벤트 적립"),
    JOIN("신규가입 적립"),
    ADMIN("관리자 수기 지급"),
    ;


    private final String description;

}
