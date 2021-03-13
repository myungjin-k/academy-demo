package my.myungjin.academyDemo.service.qna;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayRepository;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import my.myungjin.academyDemo.domain.qna.Qna;
import my.myungjin.academyDemo.domain.qna.QnaPredicate;
import my.myungjin.academyDemo.domain.qna.QnaRepository;
import my.myungjin.academyDemo.domain.qna.QnaStatus;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class QnaService {

    private final QnaRepository qnaRepository;

    private final MemberRepository memberRepository;

    private final CommonCodeRepository commonCodeRepository;

    private final ItemDisplayRepository itemDisplayRepository;

    @Transactional(readOnly = true)
    public List<Qna> findByStatus(QnaStatus status){
        //return (ArrayList<Qna>) qnaRepository.findAll(QnaPredicate.searchByStatus(status));
        return qnaRepository.findByStatus(status);
    }

    @Transactional(readOnly = true)
    public List<Qna> findByCategoryWithSecretYn(@Valid Id<Member, String> memberId, @Valid Id<CommonCode, String> cateId,
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
                }).collect(Collectors.toList());
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

    @Transactional
    public Qna ask(@Valid Id<Member, String> memberId, @Valid Id<CommonCode, String> categoryId,
                   Optional<Id<ItemDisplay, String>> itemId, @Valid Qna newQuestion){
        return memberRepository.findById(memberId.value())
                .map(member -> {
                    CommonCode cate = findQnaCategory(categoryId.value());
                    newQuestion.setCategory(cate);
                    newQuestion.setWriter(member);
                    if(!"Q_ITEM".equals(cate.getCode()))
                        return save(newQuestion);
                    ItemDisplay item = findItem(itemId.map(Id::value).orElse(null));
                    newQuestion.setItem(item);
                    return save(newQuestion);
                }).orElseThrow(() -> new NotFoundException(Member.class, memberId));
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

    private Qna save(Qna qna){
        return qnaRepository.save(qna);
    }

}
