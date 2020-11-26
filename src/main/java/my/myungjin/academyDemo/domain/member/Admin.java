package my.myungjin.academyDemo.domain.member;

import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "admin")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Admin extends User {
    @Id
    private String id;

    @Size(min = 1, max = 50)
    @Column(name = "admin_id", nullable = false, updatable = false, unique = true)
    private String adminId;

    @Size(min = 1, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at", insertable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime updateAt;

    @Builder
    public Admin(String id, @Size(min = 1, max = 50) String adminId, @Size(min = 1, max = 255) String password, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.adminId = adminId;
        this.password = password;
        this.createAt = createAt;
        this.updateAt = updateAt;
        setRole(Role.of("ADMIN"));
    }

    public void login(PasswordEncoder encoder, String credentials){
        if(!encoder.matches(credentials, password)){
            throw new IllegalArgumentException("Bad Credential");
        }
    }
}
