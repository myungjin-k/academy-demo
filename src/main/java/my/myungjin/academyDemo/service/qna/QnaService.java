package my.myungjin.academyDemo.service.qna;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import my.myungjin.academyDemo.aws.S3Client;
import my.myungjin.academyDemo.commons.AttachedFile;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayRepository;
import my.myungjin.academyDemo.domain.member.Admin;
import my.myungjin.academyDemo.domain.member.AdminRepository;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import my.myungjin.academyDemo.domain.qna.*;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.Optional.ofNullable;

@Slf4j
@RequiredArgsConstructor
@Service
public class QnaService {

    private final QnaRepository qnaRepository;

    private final MemberRepository memberRepository;

    private final CommonCodeRepository commonCodeRepository;

    private final ItemDisplayRepository itemDisplayRepository;

    private final QnaReplyRepository qnaReplyRepository;

    private final AdminRepository adminRepository;

    private final S3Client s3Client;

    private final String S3_BASE_PATH = "/qna";

    private final Environment environment;

    @Transactional(readOnly = true)
    public Set<Qna> findByStatus(QnaStatus status){
        //return (ArrayList<Qna>) qnaRepository.findAll(QnaPredicate.searchByStatus(status));
        return qnaRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public Set<Qna> findByMember(@Valid Id<Member, String> loginMemberId, @Valid Id<Member, String> memberId){
        //return (ArrayList<Qna>) qnaRepository.findAll(QnaPredicate.searchByStatus(status));
        if(!loginMemberId.value().equals(memberId.value()))
            throw new IllegalArgumentException("Login User Id is not equal to Member Id parameter! (logined=" + loginMemberId + ", member=" + memberId + ")");
        return memberRepository.findById(memberId.value())
                .map(member -> qnaRepository.findByWriterAndStatusIsNot(member, QnaStatus.DELETED))
                .orElseThrow(() -> new NotFoundException(Member.class, memberId));
    }

    @Transactional(readOnly = true)
    public Set<Qna> findByCategoryWithSecretYn(@Valid Id<Member, String> memberId, @Valid Id<CommonCode, String> cateId,
                                                Optional<Id<ItemDisplay, String>> itemId){
        CommonCode cate = findQnaCategory(cateId.value());
        if("Q_ITEM".equals(cate.getCode()) && itemId.isEmpty()){
            throw new IllegalArgumentException("qna item id is null!");
        }
        return ((ArrayList<Qna>) qnaRepository.findAll(
                QnaPredicate.searchByCategoryAndItem(
                        cateId.value(),
                        itemId.map(Id::value).orElse(null)
                )))
                .stream()
                .map(qna -> {
                    if(qna.getSecretYn() == 'N')
                        return qna;
                    if(!memberId.value().equals(qna.getWriter().getId()))
                        return qna.empty();
                    return qna;
                }).collect(Collectors.toSet());
    }

    @Transactional(readOnly = true)
    public Qna findQna(@Valid Id<Qna, Long> qnaId){
        return findById(qnaId.value()).orElseThrow(() -> new NotFoundException(Qna.class, qnaId));
    }

    @Transactional(readOnly = true)
    public Qna findQnaWithSecretYn(@Valid Id<Member, String> memberId, @Valid Id<Qna, Long> qnaId){
        return findById(qnaId.value())
                .map(qna -> {
                    if(qna.getSecretYn() == 'N')
                        return qna;
                    if(!memberId.value().equals(qna.getWriter().getId()))
                        throw new IllegalArgumentException("qna writer should be equal to login member!");
                    return qna;
                }).orElseThrow(() -> new NotFoundException(Qna.class, qnaId));
    }

    private String getServerProfile(){
        String[] profiles = environment.getActiveProfiles();
        for(String profile : profiles){
            if(profile.equals("real"))
                return profile;
        }
        return "dev";
    }

    private Optional<String> uploadQnaImage(AttachedFile qnaImageFile){
        String qnaImageUrl = null;
        if(qnaImageFile != null){
            String key = qnaImageFile.randomName(getServerProfile() + S3_BASE_PATH, "jpeg");
            try {
                qnaImageUrl = s3Client.upload(
                        qnaImageFile.inputStream(),
                        qnaImageFile.length(),
                        key,
                        qnaImageFile.getContentType(),
                        null
                );
            } catch (AmazonS3Exception e){
                log.warn("Amazon S3 error (key: {}): {}", key, e.getMessage(), e);
            }
        }
        return ofNullable(qnaImageUrl);
    }

    private void deleteReviewImage(String qnaImgUrl){
        try{
            s3Client.delete(qnaImgUrl, getServerProfile() + S3_BASE_PATH);
        } catch (AmazonS3Exception e){
            log.warn("Amazon S3 error (key: {}): {}", qnaImgUrl, e.getMessage(), e);
        }
    }

    @Transactional
    public Qna ask(@Valid Id<Member, String> loginMemberId, @Valid Id<Member, String> memberId, @Valid Id<CommonCode, String> categoryId,
                   Optional<Id<ItemDisplay, String>> itemId, @Valid Qna newQuestion, AttachedFile qnaImgFile){
        if(!loginMemberId.value().equals(memberId.value()))
            throw new IllegalArgumentException("Login User Id is not equal to Member Id parameter! (logined=" + loginMemberId + ", member=" + memberId + ")");
        return memberRepository.findById(memberId.value())
                .map(member -> {
                    CommonCode cate = findQnaCategory(categoryId.value());
                    newQuestion.setCategory(cate);
                    newQuestion.setWriter(member);
                    newQuestion.setAttachedImgUrl(uploadQnaImage(qnaImgFile).orElse(null));
                    if(!"Q_ITEM".equals(cate.getCode()))
                        return save(newQuestion);
                    ItemDisplay item = findItem(itemId.map(Id::value).orElse(null));
                    newQuestion.setItem(item);
                    return save(newQuestion);
                }).orElseThrow(() -> new NotFoundException(Member.class, memberId));
    }

    @Transactional
    public Qna modify(@Valid Id<Member, String> loginMemberId, @Valid Id<Member, String> memberId, @Valid Id<Qna, Long> qnaSeq,
                      @Valid Id<CommonCode, String> cateId, Qna qna, AttachedFile qnaImgFile) {
        if(!loginMemberId.value().equals(memberId.value()))
            throw new IllegalArgumentException("Login User Id is not equal to Member Id parameter! (logined=" + loginMemberId + ", member=" + memberId + ")");
        Qna updated = qnaRepository.findByWriterIdAndSeq(memberId.value(), qnaSeq.value())
                .map(q -> {
                    CommonCode category = findQnaCategory(cateId.value());
                    String newCateCode = category.getCode();
                    String cateCode = q.getCategory().getCode();
                    if("Q_ITEM".equals(cateCode) && !newCateCode.equals(cateCode))
                        throw new IllegalArgumentException("Can't modify item qna's category! (seq=" + qnaSeq + ", newCateCode=" + newCateCode + ")");
                    q.setCategory(category);
                    q.modifyContent(qna);
                    q.setAttachedImgUrl(uploadQnaImage(qnaImgFile).orElse(null));
                    return save(q);
                }).orElseThrow(() -> new NotFoundException(Qna.class, memberId, qnaSeq));
        deleteReviewImage(qna.getAttachedImgUrl().orElse(null));
        return updated;
    }


    @Transactional
    public Qna delete(@Valid Id<Member, String> loginMemberId, @Valid Id<Member, String> memberId, @Valid Id<Qna, Long> qnaSeq) {
        if(!loginMemberId.value().equals(memberId.value()))
            throw new IllegalArgumentException("Login User Id is not equal to Member Id parameter! (logined=" + loginMemberId + ", member=" + memberId + ")");
        return qnaRepository.findByWriterIdAndSeq(memberId.value(), qnaSeq.value())
                .map(q -> {
                    q.delete();
                    return save(q);
                }).orElseThrow(() -> new NotFoundException(Qna.class, memberId, qnaSeq));
    }

    @Transactional
    public QnaReply reply(@Valid Id<Qna, Long> qnaSeq, @Valid Id<Admin, String> adminId, @Valid QnaReply newReply) {
        return adminRepository.findById(adminId.value())
                .map(admin -> {
                    Qna qna = findById(qnaSeq.value()).orElseThrow(() -> new NotFoundException(Qna.class, qnaSeq));
                    newReply.setQna(qna);
                    newReply.setWriter(admin);
                    qna.answered(newReply);
                    return save(newReply);
                })
                .orElseThrow(() -> new NotFoundException(Admin.class, adminId));
    }

    @Transactional
    public QnaReply modifyReply(@Valid Id<Admin, String> adminId, @Valid Id<Qna, Long> qnaSeq,
                           @Valid Id<QnaReply, String> replyId, QnaReply reply) {
        return qnaReplyRepository.findByQnaSeqAndWriterIdAndId(qnaSeq.value(), adminId.value(), replyId.value())
                .map(qnaReply -> {
                    qnaReply.modifyContent(reply);
                    return save(qnaReply);
                })
                .orElseThrow(() -> new NotFoundException(QnaReply.class, adminId, qnaSeq, replyId));
    }

    @Transactional
    public QnaReply deleteReply(@Valid Id<Admin, String> adminId, @Valid Id<Qna, Long> qnaSeq, @Valid Id<QnaReply, String> replyId) {
        return qnaReplyRepository.findByQnaSeqAndWriterIdAndId(qnaSeq.value(), adminId.value(), replyId.value())
                .map(q -> {
                    q.delete();
                    return save(q);
                }).orElseThrow(() -> new NotFoundException(QnaReply.class, adminId, qnaSeq, replyId));
    }

    private ItemDisplay findItem(String id){
        return itemDisplayRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(ItemDisplay.class, id));
    }

    private CommonCode findQnaCategory(String id){
        return commonCodeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(CommonCode.class, id));
    }

    private Optional<Qna> findById(long seq){
        return qnaRepository.findById(seq);
    }

    private Optional<QnaReply> findById(String id) {return qnaReplyRepository.findById(id); }

    private Qna save(Qna qna){
        return qnaRepository.save(qna);
    }

    private QnaReply save(QnaReply reply){
        return qnaReplyRepository.save(reply);
    }

}
