package my.myungjin.academyDemo.domain.qna;

import my.myungjin.academyDemo.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;
import java.util.Set;

public interface QnaRepository extends JpaRepository<Qna, Long>, QuerydslPredicateExecutor<Qna> {

    Set<Qna> findByStatus(QnaStatus status);

    Optional<Qna> findByWriterIdAndSeq(String memberId, long seq);

    Set<Qna> findByWriterAndStatusIsNot(Member writer, QnaStatus status);

}
