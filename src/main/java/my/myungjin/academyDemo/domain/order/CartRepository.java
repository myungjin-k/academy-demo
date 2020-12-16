package my.myungjin.academyDemo.domain.order;

import my.myungjin.academyDemo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CartRepository extends JpaRepository<CartItem, String> {

    List<CartItem> findAllByMember(Member member);

    void deleteAllByMember(Member member);
}
