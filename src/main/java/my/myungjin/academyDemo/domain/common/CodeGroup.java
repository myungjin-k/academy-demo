package my.myungjin.academyDemo.domain.common;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "code_group")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class CodeGroup {

    @Id @Getter
    private String id;

    @Getter
    @Size(min = 1, max = 10)
    @Column(name = "code", nullable = false, unique = true)
    private String code;

    @Getter
    @Size(min = 1, max = 50)
    @Column(name = "name_eng", nullable = false)
    private String nameEng;

    @Getter
    @Size(min = 1, max = 50)
    @Column(name = "name_kor", nullable = false)
    private String nameKor;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

 /*   @Getter
    @OneToMany(mappedBy = "codeGroup", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) //JOIN
    private Collection<CommonCode> commonCodes;
*/
    @Builder
    public CodeGroup(String id, String code, String nameEng, String nameKor, LocalDateTime updateAt) {
        this.id = id;
        this.code = code;
        this.nameEng = nameEng;
        this.nameKor = nameKor;
        this.updateAt = updateAt;
    }

/*    public void addCommonCode(CommonCode commonCode) {
        commonCodes.add(commonCode);
        commonCode.setCodeGroup(this);
    }*/

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void update(String code, String nameEng, String nameKor){
        this.code = code;
        this.nameEng = nameEng;
        this.nameKor= nameKor;
        this.updateAt = now();
    }

}
