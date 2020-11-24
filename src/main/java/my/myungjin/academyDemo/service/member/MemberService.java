package my.myungjin.academyDemo.service.member;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public Member join(Member newMember) {
        Member saved = save(newMember);

        return findById(saved.getId()).orElse(newMember);
    }

    private Optional<Member> findById(String id){
        return memberRepository.findById(id);
    }
    private Member save(Member member){
        return memberRepository.save(member);
    }
}
