package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Admin;
import my.myungjin.academyDemo.domain.member.AdminRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;


    @Transactional
    public String login(@NotBlank String adminId, @NotBlank String password){
        // TODO validation
        return findByAdminId(adminId).map(admin -> {
            admin.login(passwordEncoder, password);
            return admin.getId();
        }).orElseThrow(() -> new NotFoundException(Admin.class, adminId));
    }
    private Optional<Admin> findByAdminId(@NotBlank String userId){
        return adminRepository.findByAdminId(userId);
    }
}
