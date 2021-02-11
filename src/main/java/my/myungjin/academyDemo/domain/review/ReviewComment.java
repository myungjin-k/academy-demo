package my.myungjin.academyDemo.domain.review;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import my.myungjin.academyDemo.domain.member.Admin;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Optional;

import static java.time.LocalDateTime.now;
import static java.util.Optional.ofNullable;


@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "review_comment")
public class ReviewComment {

    @Getter
    @Id
    @GeneratedValue(generator = "reviewCommentId")
    @GenericGenerator(name = "reviewCommentId", strategy = "my.myungjin.academyDemo.commons.IdGenerator")
    private String id;

    @Getter
    @Column(name = "content", nullable = false, columnDefinition = "varchar2(2000) not null")
    private String content;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "admin_id", nullable = false, updatable = false)
    private Admin writer;

    @JsonManagedReference
    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false, updatable = false)
    private Review review;

    @Getter
    @Column(name = "create_at", insertable = false, updatable = false,
            columnDefinition = "datetime default current_timestamp")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    public ReviewComment(String content){
        this.content = content;
    }

    public Optional<LocalDateTime> getUpdateAt(){
        return ofNullable(updateAt);
    }

    public void modify(String content){
        this.content = content;
        this.updateAt = now();
    }

}
