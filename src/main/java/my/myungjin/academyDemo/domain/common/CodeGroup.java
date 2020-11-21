package my.myungjin.academyDemo.domain.common;

import lombok.*;
import my.myungjin.academyDemo.util.Util;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;
import static my.myungjin.academyDemo.util.Util.timestampOf;

@Entity
@Table(name = "code_group")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class CodeGroup {
    @Id
    private String id;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "name_eng", nullable = false)
    private String nameEng;

    @Column(name = "name_kor", nullable = false)
    private String nameKor;

    @Column(name = "create_at", nullable = false, updatable = false)
    private Timestamp createAt;

    @Column(name = "update_at", nullable = false)
    private Timestamp updateAt;


    @OneToMany(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private List<CommonCode> codes = new ArrayList<>();

    @Builder
    public CodeGroup(String id, String code, String nameEng, String nameKor, Timestamp createAt, Timestamp updateAt) {
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
        this.updateAt = timestampOf(now());
    }

}
