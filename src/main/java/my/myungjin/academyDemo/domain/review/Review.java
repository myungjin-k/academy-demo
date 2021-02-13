package my.myungjin.academyDemo.domain.review;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;
import my.myungjin.academyDemo.domain.order.OrderItem;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.ofNullable;

@Entity
@Table(name = "review")
@ToString(exclude = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Getter @Id
    @GeneratedValue(generator = "reviewId")
    @GenericGenerator(name = "reviewId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @Max(5)
    @Column(name = "score", columnDefinition = "number default 1")
    private int score;

    @Getter @Setter
    @Column(name = "review_img")
    private String reviewImg;

    @Getter @NotBlank
    @Column(name = "content", nullable = false)
    private String content;

    // TODO 리뷰 상태 정의
    /*@Getter
    @Column(name = "status", nullable = false, columnDefinition = "number default 1")
    private String content;*/

    @Getter @NotBlank
    @Column(name = "reserves_paid", nullable = false, columnDefinition = "boolean not null default false")
    private boolean isReservesPaid;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private ItemDisplay item;

    @Getter @Setter
    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "order_item_id", nullable = false)
    private OrderItem orderItem;

    @Getter @Setter
    @JsonBackReference
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewComment> comments;

    @Builder
    public Review(String id, @Size(min = 1, max = 5) int score, String reviewImg, String content, boolean isReservesPaid) {
        this.id = id;
        this.score = score;
        this.reviewImg = reviewImg;
        this.content = content;
        this.isReservesPaid = isReservesPaid;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void modify(String content, int score){
        this.content = content;
        this.score = score;
    }
}
