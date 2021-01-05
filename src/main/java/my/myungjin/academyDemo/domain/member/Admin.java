package my.myungjin.academyDemo.domain.member;

import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "admin")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id", callSuper = false)
public class Admin{

    @Id @Getter
    @GeneratedValue(generator = "adminId")
    @GenericGenerator(name = "adminId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @Size(min = 1, max = 50)
    @Column(name = "admin_id", nullable = false, updatable = false, unique = true)
    private String adminId;

    @Getter
    @Size(min = 1, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Builder
    public Admin(String id, @Size(min = 1, max = 50) String adminId, @Size(min = 1, max = 255) String password, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.adminId = adminId;
        this.password = password;
        this.updateAt = updateAt;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void login(PasswordEncoder encoder, String credentials){
        if(!encoder.matches(credentials, password)){
            throw new IllegalArgumentException("Bad Credential");
        }
    }
}
