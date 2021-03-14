package my.myungjin.academyDemo.domain.qna;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
@ToString(exclude = "reply")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "seq")
public class Qna {

    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long seq;

    @Column(name = "attached_image_url")
    private String attachedImgUrl;

    @Getter @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @Getter @NotBlank
    @Column(name = "content", nullable = false)
    private String content;

    @Getter
    @Column(name = "status", insertable = false, columnDefinition = "char default 'W'", nullable = false)
    @Convert(converter = QnaStatusConverter.class)
    private QnaStatus status;

    @Getter
    @Column(name = "secret_yn", nullable = false, columnDefinition = "char defalut 'N'")
    private char secretYn;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "item_id")
    private ItemDisplay item;

    @Getter @Setter
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CommonCode category;

    @Getter @Setter
    @JsonManagedReference
    @OneToOne(mappedBy = "qna", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private QnaReply reply;

/*    @Getter @Setter
    @JsonBackReference
    @OneToMany(mappedBy = "review", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ReviewComment> comments;*/

    @Builder
    public Qna(long seq, String attachedImgUrl, @NotBlank String title, @NotBlank String content, char secretYn,
               Member writer, ItemDisplay item, CommonCode category, QnaStatus status, LocalDateTime createAt) {
        this.seq = seq;
        this.attachedImgUrl = attachedImgUrl;
        this.title = title;
        this.content = content;
        this.secretYn = secretYn;
        this.writer = writer;
        this.item = item;
        this.category = category;
        this.status = status;
        this.createAt = createAt;
    }

    public Qna empty() {
        return Qna.builder()
                .seq(seq)
                .title(title)
                .writer(writer)
                .content("secret post")
                .secretYn(secretYn)
                .createAt(createAt)
                .build();
    }

    public Optional<String> getAttachedImgUrl() { return ofNullable(attachedImgUrl); }

    public Optional<ItemDisplay> getItem() { return ofNullable(item); }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void modifyContent(Qna qna){
        this.title = qna.getTitle();
        this.content = qna.getContent();
        this.attachedImgUrl = qna.getAttachedImgUrl().orElse(null);
        this.secretYn = qna.getSecretYn();
        this.updateAt = now();
    }

    public void delete(){
        this.status = QnaStatus.DELETED;
        this.updateAt = now();
    }

    public void answered(QnaReply reply){
        reply.setQna(this);
        this.status = QnaStatus.ANSWERED;
        this.reply = reply;
        this.updateAt = now();
    }

}
