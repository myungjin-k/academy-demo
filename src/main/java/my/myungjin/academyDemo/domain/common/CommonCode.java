package my.myungjin.academyDemo.domain.common;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "common_code")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class CommonCode {

    @Id @Getter
    private String id;

    @Getter
    @Column(nullable = false)
    private String code;

    @Getter
    @Column(nullable = false)
    private String nameEng;

    @Getter
    @Column(nullable = false)
    private String nameKor;

    @Getter
    @Column(nullable = false)
    private String groupId;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Builder
    public CommonCode(String id, String code, String nameEng, String nameKor, String groupId, LocalDateTime updateAt) {
        this.id = id;
        this.code = code;
        this.nameEng = nameEng;
        this.nameKor = nameKor;
        this.groupId = groupId;
        this.updateAt = updateAt;
    }

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
