package my.myungjin.academyDemo.service.member;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.mail.Mail;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.service.mail.MailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

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

    public String findUserId(String tel){
        return memberRepository.findByTel(tel)
                .map(Member::getUserId)
                .orElse(null);
    }

    public Optional<String> findPassword(String email){
        return memberRepository.findByEmail(email)
                .map(member -> {
                    mailService.sendMail(Mail.builder()
                            .address(email)
                            .title("[demo] 비밀번호 찾기/변경 안내")
                            .content("<p> 아래 링크에서 비밀번호 변경 가능합니다.</p> ")
                            .build()
                    );
                    return  member.getEmail();
                });
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
