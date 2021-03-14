package my.myungjin.academyDemo.domain.qna;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface QnaRepository extends JpaRepository<Qna, Long>, QuerydslPredicateExecutor<Qna> {

    List<Qna> findByStatus(QnaStatus status);

    Optional<Qna> findByWriterIdAndSeq(String memberId, long seq);

}
