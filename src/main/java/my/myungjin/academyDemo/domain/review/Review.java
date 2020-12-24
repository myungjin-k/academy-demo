package my.myungjin.academyDemo.domain.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "review")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Getter
    @Id
    private String id;

    @Getter @Setter
    @Size(min = 1, max = 5)
    @Column(name = "score", columnDefinition = "number default 1")
    private int score;

    @Getter @Setter
    @Column(name = "review_img")
    private String reviewImg;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDisplay item;

    @Builder
    public Review(String id, @Size(min = 1, max = 5) int score, String reviewImg) {
        this.id = id;
        this.score = score;
        this.reviewImg = reviewImg;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

}
