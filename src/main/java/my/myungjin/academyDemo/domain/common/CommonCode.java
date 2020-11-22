package my.myungjin.academyDemo.domain.common;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "common_code")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class CommonCode {

    @Id
    private String id;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private String nameEng;

    @Column(nullable = false)
    private String nameKor;

    @Column(nullable = false)
    private String groupId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(nullable = false)
    private LocalDateTime updateAt;

    @Builder
    public CommonCode(String id, String code, String nameEng, String nameKor, String groupId, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.code = code;
        this.nameEng = nameEng;
        this.nameKor = nameKor;
        this.groupId = groupId;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public void update(String code, String nameEng, String nameKor){
        this.code = code;
        this.nameEng = nameEng;
        this.nameKor= nameKor;
        this.updateAt = now();
    }

}
