package my.myungjin.academyDemo.domain.item;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "item_option")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ItemOption {
    @Id
    @Getter
    @GeneratedValue(generator = "itemOptionId")
    @GenericGenerator(name = "itemOptionId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @Size(max = 10)
    @Column(name = "size", nullable = false, columnDefinition = "varchar(10) default 'ONE SIZE'")
    private String size;

    @Getter
    @Size(max = 10)
    @Column(name = "color", nullable = false, columnDefinition = "varchar(10) default 'ONE COLOR'")
    private String color;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Setter @Getter
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    private ItemMaster itemMaster;

    @Builder
    public ItemOption(String id, @Size(min = 1, max = 10) String size, @Size(min = 1, max = 10) String color) {
        this.id = id;
        this.size = size;
        this.color = color;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void modify(String color, String size){
        this.color = color;
        this.size = size;
    }
}
