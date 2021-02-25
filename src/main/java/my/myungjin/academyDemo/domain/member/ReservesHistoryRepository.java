package my.myungjin.academyDemo.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservesHistoryRepository extends JpaRepository<ReservesHistory, String> {

    // 회원 엔티티로 검색
    List<ReservesHistory> findByMember(Member member);

}
