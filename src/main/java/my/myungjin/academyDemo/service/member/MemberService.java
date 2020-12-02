package my.myungjin.academyDemo.service.member;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.commons.mail.Mail;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.MemberRepository;
import my.myungjin.academyDemo.domain.member.Role;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.service.mail.MailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MemberService {

    private Logger log = LoggerFactory.getLogger(MemberService.class);

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    private final MailService mailService;

    @Transactional
    public Member join(@Valid Member newMember) {
        newMember.encryptPassword(passwordEncoder);
        Member saved = save(newMember);
        return findById(saved.getId()).orElse(newMember);
    }

    @Transactional
    public String login(@NotBlank String userId, @NotBlank String password){

        return findByUserId(userId).map(member -> {
            member.login(passwordEncoder, password);
            return member.getId();
        }).orElseThrow(() ->  new NotFoundException(Member.class, userId));
    }

    @Transactional(readOnly = true)
    public Optional<String> findUserId(@NotBlank String tel){
        return memberRepository.findByTel(tel)
                .map(Member::getUserId);
    }

    @Transactional(readOnly = true)
    public Optional<String> findPassword(@NotBlank String email){
        return memberRepository.findByEmail(email).map(member -> {
            String id = member.getId();
            Mail mail = Mail.builder()
                    .to(email)
                    .title("[demo] 비밀번호 찾기/변경 안내")
                    .content(
                                    "<p> 아래 링크에서 비밀번호 변경 가능합니다.</p> " +
                                    "<a href='http://localhost:7090/changePassword/"+ id + "'>비밀번호 변경하기</a>"
                    )
                    .build();
            try {
                mailService.sendMail(mail);
            } catch (MessagingException e){
                log.warn("Messaging Error ({}) : {}", mail, e.getMessage(), e);
            } catch (MailException me){
                log.warn("Mailing Error : {}", me.getMessage(), me);
            }
            return member.getEmail();
        });
    }

    @Transactional
    public Member modifyPassword(@NotBlank Id<Member, String> id, @NotBlank String newPassword){
        return findById(id.value()).map(member -> {
            member.updatePassword(passwordEncoder.encode(newPassword));
            save(member);
            return member;
        }).orElseThrow(() -> new NotFoundException(Member.class, id.value()));
    }

    @Transactional(readOnly = true)
    public Member findMyInfo(@NotBlank Id<Member, String> id){
        return findById(id.value()).orElseThrow(() -> new IllegalArgumentException("invalid id=" + id));
    }

    @Transactional
    public Member modify(@NotBlank Id<Member, String> id, @Valid Member member){
        return findById(id.value()).map(ori -> {
            ori.update(member);
            ori.updatePassword(passwordEncoder.encode(member.getPassword()));
            save(ori);
            return ori;
        }).orElseThrow(() -> new NotFoundException(Member.class, id.value()));
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
