package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.member.Admin;
import my.myungjin.academyDemo.domain.member.AdminRepository;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.member.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AdminService {

    private final AdminRepository adminRepository;

    private final PasswordEncoder passwordEncoder;


    public Admin login(@NotBlank String adminId, @NotBlank String password){
        // TODO validation
        return findByAdminId(adminId).map(admin -> {
            admin.login(passwordEncoder, password);
            admin.setRole(Role.ADMIN);
            return admin;
        }).orElseThrow(() -> new IllegalArgumentException("invalid id =" + adminId));
    }
    private Optional<Admin> findByAdminId(String userId){
        return adminRepository.findByAdminId(userId);
    }
}
