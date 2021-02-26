package my.myungjin.academyDemo.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String>, QuerydslPredicateExecutor<Member> {

    Optional<Member> findByUserId(String userId);

    Optional<Member> findByTel(String tel);

    Optional<Member> findByEmail(String email);

}
