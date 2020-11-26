package my.myungjin.academyDemo.domain.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByAdminId(String adminId);
}
