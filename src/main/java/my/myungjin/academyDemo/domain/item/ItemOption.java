package my.myungjin.academyDemo.domain.item;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_option")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ItemOption {
    @Id @Getter
    private String id;

    @Getter
    @Size(min = 1, max = 10)
    @Column(name = "size", columnDefinition = "varchar(10) default 'FREE'")
    private String size;

    @Getter
    @Size(min = 1, max = 10)
    @Column(name = "color", columnDefinition = "varchar(10) default 'ONE COLOR'")
    private String color;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Setter
    @Getter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "master_id", nullable = false)
    private ItemMaster itemMaster;

    @Builder
    public ItemOption(String id, @Size(min = 1, max = 10) String size, @Size(min = 1, max = 10) String color) {
        this.id = id;
        this.size = size;
        this.color = color;
    }
}
