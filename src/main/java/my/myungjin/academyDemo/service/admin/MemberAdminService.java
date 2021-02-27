package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.member.*;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberAdminService {

    private final MemberRepository memberRepository;

    private final ReservesHistoryRepository reservesHistoryRepository;

    @Transactional(readOnly = true)
    public List<Member> search(String memberId, String userId){
        return ((ArrayList<Member>) memberRepository.findAll(MemberPredicate.search(memberId, userId)));

    }

    @Transactional(readOnly = true)
    public Member findByIdWithReservesHistory(Id<Member, String> memberId){
        return memberRepository.findById(memberId.value())
                .map(member -> {
                    member.setReservesHistories(reservesHistoryRepository.findByMemberOrderByCreateAtDesc(member));
                    return member;
                })
                .orElseThrow(() -> new NotFoundException(Member.class, memberId));
    }

    @Transactional
    public Member updateReserves(@Valid Id<Member, String> memberId, int minus, int plus){
        return memberRepository.findById(memberId.value())
                .map(member -> {
                    member.flushReserves(minus);
                    member.addReserves(plus);
                    ReservesHistory newHistory = ReservesHistory.builder()
                            .amount(-minus + plus)
                            .type(ReservesType.ADMIN)
                            .build();
                    member.addReservesHistory(newHistory);
                    return save(member);
                }).orElseThrow(() -> new NotFoundException(Member.class, memberId));
    }

    private Member save(Member member){
        return memberRepository.save(member);
    }
}
