package my.myungjin.academyDemo.domain.order;

import my.myungjin.academyDemo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, String> {

    // 회원 PK로 검색
    List<CartItem> findAllByMember(Member member);

    // 회원 엔티티로 삭제
    void deleteAllByMember(Member member);

    // 상품옵션 PK로 검색
    CartItem getByItemOptionId(String itemId);
}
