package my.myungjin.academyDemo.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservesHistoryRepository extends JpaRepository<ReservesHistory, String> {

    // 회원 엔티티로 검색
    List<ReservesHistory> findByMemberOrderByCreateAtDesc(Member member);

    // type, ref id로 검색
    boolean existsByTypeAndRefId(ReservesType type, String refId);

    ReservesHistory getByTypeAndRefId(ReservesType type, String refId);

}
