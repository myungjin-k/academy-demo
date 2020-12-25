package my.myungjin.academyDemo.domain.member;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import my.myungjin.academyDemo.domain.order.CartItem;
import my.myungjin.academyDemo.domain.order.Order;
import my.myungjin.academyDemo.domain.review.Review;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "member")
@ToString(exclude = {"cartItems", "orders", "reviews"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Member{

    @Id @Getter
    private String id;

    @Getter
    @Size(min = 1, max = 50)
    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private String userId;

    @Getter
    @Size(min = 1, max = 255)
    @Column(name = "password", nullable = false)
    private String password;

    @Getter
    @Size(min = 1, max = 10)
    @Column(name = "name", nullable = false)
    private String name;

    @Getter
    @Email
    @Column(name = "email", nullable = false)
    private String email;

    @Getter
    @Pattern(regexp = "^\\d{3}-\\d{4}-\\d{4}$", message = "전화번호는 010-0000-0000 형태여야 합니다.")
    @Column(name = "tel" , nullable = false)
    private String tel;

    @Getter
    @Column(name = "addr1", nullable = false)
    private String addr1;

    @Getter
    @Column(name = "addr2", nullable = false)
    private String addr2;

    @Getter
    @Column(name = "rating", insertable = false, columnDefinition = "char default 'B'")
    @Convert(converter = RatingConverter.class)
    private Rating rating;

    @Getter
    @Column(name = "reserves", insertable = false, columnDefinition = "number default 0")
    private int reserves;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter
    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<CartItem> cartItems;

    @Getter
    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private Collection<Order> orders;

    @Getter
    @JsonIgnore
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<Review> reviews;

    @Builder
    public Member(String id, String userId, String password, String name, String email, String tel, String addr1, String addr2,
                  Rating rating, int reserves, LocalDateTime updateAt) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.email = email;
        this.tel = tel;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.rating = rating;
        this.reserves = reserves;
        this.updateAt = updateAt;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void updatePassword(String newPassword){
        password = newPassword;
        this.updateAt = now();
    }

    public void update(Member member){
        this.name = member.name;
        this.email = member.email;
        this.tel = member.tel;
        this.addr1 = member.addr1;
        this.addr2 = member.addr2;
        this.updateAt = now();
    }

    public void encryptPassword(PasswordEncoder encoder){
        password = encoder.encode(password);
    }

    public void login(PasswordEncoder encoder, String credentials){
        if(!encoder.matches(credentials, password)){
            throw new IllegalArgumentException("Bad Credential");
        }
    }
}
