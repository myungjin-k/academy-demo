package my.myungjin.academyDemo.domain.qna;


import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.member.Member;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "qna")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Qna {

    @Getter
    @Id
    @GeneratedValue(generator = GenerationType.IDENTITY)
    private long seq;

    @Setter
    @Column(name = "attached_image_url")
    private String attachedImgUrl;

    @Getter @NotBlank
    @Column(name = "content", nullable = false)
    private String content;

    @Getter
    @Column(name = "status", insertable = false, columnDefinition = "char default 'W'")
    @Convert(converter = QnaStatusConverter.class)
    private QnaStatus status;

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
    @JoinColumn(name = "item_id")
    private ItemDisplay item;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CommonCode category;

/*    @Getter @Setter
    @JsonBackReference
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewComment> comments;*/

    @Builder
    public Qna(String attachedImgUrl, @NotBlank String content, Member member, ItemDisplay item, CommonCode category, QnaStatus status) {
        this.attachedImgUrl = attachedImgUrl;
        this.content = content;
        this.member = member;
        this.item = item;
        this.category = category;
        this.status = status;
    }

    public Optional<String> getAttachedImgUrl() { return ofNullable(attachedImgUrl); }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void modifyContent(String content, String attachedImgUrl){
        this.content = content;
        this.attachedImgUrl = attachedImgUrl;
        this.updateAt = now();
    }

    public void delete(){
        this.status = QnaStatus.DELETED;
        this.updateAt = now();
    }

    public void answered(){
        this.status = QnaStatus.ANSWERED;
        this.updateAt = now();
    }

}
