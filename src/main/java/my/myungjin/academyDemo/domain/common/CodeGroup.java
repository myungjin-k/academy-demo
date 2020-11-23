package my.myungjin.academyDemo.domain.common;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

import static java.time.LocalDateTime.now;

@Entity
@Table(name = "code_group")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class CodeGroup {
    @Id
    private String id;

    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Column(name = "name_eng", nullable = false)
    private String nameEng;

    @Column(name = "name_kor", nullable = false)
    private String nameKor;

    @Column(name = "create_at", nullable = false, updatable = false)
    private LocalDateTime createAt;

    @Column(name = "update_at", nullable = false)
    private LocalDateTime updateAt;

    @OneToMany(mappedBy = "groupId", fetch = FetchType.EAGER) //JOIN
    private List<CommonCode> commonCodes;

    @Builder
    public CodeGroup(String id, String code, String nameEng, String nameKor, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.code = code;
        this.nameEng = nameEng;
        this.nameKor = nameKor;
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
