package my.myungjin.academyDemo.service.member;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Member join(@Valid Member newMember) {
        newMember.setPassword(passwordEncoder.encode(newMember.getPassword()));
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
