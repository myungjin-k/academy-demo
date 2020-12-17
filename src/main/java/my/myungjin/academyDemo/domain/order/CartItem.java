package my.myungjin.academyDemo.domain.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "cart")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class CartItem {

    @Id
    @Getter
    private String id;

    @Getter
    @Column(name = "count", nullable = false,
            columnDefinition = "number default 1")
    private int count;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Setter @Getter
    //@JsonBackReference
    @OneToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDisplay.ItemDisplayOption itemOption;

    public CartItem(String id, int count) {
        this.id = id;
        this.count = count;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void modify(int count){
        this.count = count;
        this.updateAt = now();
    }

}

