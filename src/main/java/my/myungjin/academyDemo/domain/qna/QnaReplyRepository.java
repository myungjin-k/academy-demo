package my.myungjin.academyDemo.domain.qna;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QnaReplyRepository extends JpaRepository<QnaReply, String> {

    Optional<QnaReply> findByQna(Qna qna);

    Optional<QnaReply> findByQnaSeqAndWriterIdAndId(long qnaSeq, String adminId, String replyId);

}
