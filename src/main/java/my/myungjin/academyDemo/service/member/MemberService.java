package my.myungjin.academyDemo.service.member;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import my.myungjin.academyDemo.domain.member.Role;
import org.springframework.data.domain.Example;
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
        newMember.encryptPassword(passwordEncoder);
        Member saved = save(newMember);
        return findById(saved.getId()).orElse(newMember);
    }

    public Member login(String userId, String password){
        // TODO validation
        return findByUserId(userId).map(member -> {
            member.login(passwordEncoder, password);
            member.setRole(Role.MEMBER);
            return member;
        }).orElseThrow(() -> new IllegalArgumentException("invalid id =" + userId));
    }
    private Optional<Member> findByUserId(String userId){
        return memberRepository.findByUserId(userId);
    }
    private Optional<Member> findById(String id){
        return memberRepository.findById(id);
    }
    private Member save(Member member){
        return memberRepository.save(member);
    }
}
