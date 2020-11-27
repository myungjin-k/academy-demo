package my.myungjin.academyDemo.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findByUserId(String userId);

    Optional<Member> findByTel(String tel);

    Optional<Member> findByEmail(String email);

}
