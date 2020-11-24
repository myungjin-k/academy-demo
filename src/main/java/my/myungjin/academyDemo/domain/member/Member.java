package my.myungjin.academyDemo.domain.member;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "member")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class Member {
    @Id
    private String id;

    @Column(name = "user_id", nullable = false, updatable = false, unique = true)
    private String userId;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "tel")
    private String tel;

    @Column(name = "addr1")
    private String addr1;

    @Column(name = "addr2")
    private String addr2;

    @Column(name = "rating", insertable = false, columnDefinition = "char default 'B'")
    @Convert(converter = RatingConverter.class)
    private Rating rating;

    @Column(name = "reserves", insertable = false, columnDefinition = "integer default 0")
    private int reserves;

    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at", insertable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime updateAt;

    @Builder
    public Member(String id, String userId, String password, String name, String tel, String addr1, String addr2,
                  Rating rating, int reserves, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.tel = tel;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.rating = rating;
        this.reserves = reserves;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public void update(String name, String tel, String addr1, String addr2){
        this.name = name;
        this.tel = tel;
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.updateAt = now();
    }
}
