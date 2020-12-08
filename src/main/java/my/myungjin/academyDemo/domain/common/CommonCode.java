package my.myungjin.academyDemo.domain.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemMaster;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "common_code")
@ToString(exclude = "items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = {"id"})
public class CommonCode {

    @Id @Getter
    private String id;

    @Getter
    @Size(min = 1, max = 10)
    @Column(nullable = false)
    private String code;

    @Getter
    @Size(min = 1, max = 50)
    @Column(nullable = false)
    private String nameEng;

    @Getter
    @Size(min = 1, max = 50)
    @Column(nullable = false)
    private String nameKor;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    private LocalDateTime updateAt;

    @Setter
    @Getter
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private CodeGroup codeGroup;

    @JsonIgnore
    @Getter
    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE) //JOIN
    private Collection<ItemMaster> items;

    @Builder
    public CommonCode(String id, String code, String nameEng, String nameKor, CodeGroup codeGroup, LocalDateTime updateAt) {
        this.id = id;
        this.code = code;
        this.nameEng = nameEng;
        this.nameKor = nameKor;
        this.codeGroup = codeGroup;
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

    public void addItems(ItemMaster item){
        items.add(item);
        item.setCategory(this);
    }
}
