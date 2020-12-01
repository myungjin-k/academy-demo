package my.myungjin.academyDemo.domain.item;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "item_option")
@ToString
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class ItemOption {
    @Id
    private String id;

    @Size(min = 1, max = 10)
    @Column(name = "size", columnDefinition = "DEFAULT 'FREE")
    private String size;

    @Size(min = 1, max = 10)
    @Column(name = "color", columnDefinition = "DEFAULT 'ONE COLOR")
    private String color;


    @Size(min = 1, max = 50)
    @Column(name = "master_id", nullable = false)
    private String masterId;

    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at", insertable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime updateAt;

}
