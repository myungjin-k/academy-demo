package my.myungjin.academyDemo.domain.qna;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import my.myungjin.academyDemo.domain.member.Admin;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;

@Entity
@Table(name = "qna_reply")
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode(of = "id")
public class QnaReply {

    @Id @Getter
    @GeneratedValue(generator = "qnaReplyId")
    @GenericGenerator(name = "qnaReplyId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Column(name = "attached_image_url")
    private String attachedImgUrl;

    @Getter @NotBlank
    @Column(name = "title", nullable = false)
    private String title;

    @Getter @NotBlank
    @Column(name = "content", nullable = false)
    private String content;

    @Getter
    @Column(name = "status", insertable = false, columnDefinition = "char default 'A'", nullable = false)
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
    private Admin writer;

    @Getter @Setter
    @JsonBackReference
    @OneToOne
    @JoinColumn(name = "qna_seq", nullable = false)
    private Qna qna;

    @Builder
    public QnaReply(String attachedImgUrl, @NotBlank String title, @NotBlank String content, QnaStatus status, char secretYn) {
        this.attachedImgUrl = attachedImgUrl;
        this.title = title;
        this.content = content;
        this.status = status;
        this.secretYn = secretYn;
    }


    public Optional<String> getAttachedImgUrl() { return ofNullable(attachedImgUrl); }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void modifyContent(QnaReply reply){
        this.content = reply.getContent();
        this.attachedImgUrl = reply.getAttachedImgUrl().orElse(null);
        this.secretYn = reply.getSecretYn();
        this.updateAt = now();
    }

    public void delete(){
        this.status = QnaStatus.DELETED;
        this.updateAt = now();
    }

}
