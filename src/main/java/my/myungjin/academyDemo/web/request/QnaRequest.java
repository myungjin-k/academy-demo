package my.myungjin.academyDemo.web.request;

import lombok.*;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.qna.Qna;
import my.myungjin.academyDemo.domain.qna.QnaReply;
import my.myungjin.academyDemo.domain.qna.QnaStatus;

import java.util.Optional;

import static java.util.Optional.empty;

@ToString
@NoArgsConstructor
@Setter
@Getter
public class QnaRequest {

    private String title;

    private String content;

    private String attachedImgUrl;

    private char secretYn;

    private String cateId;

    private String itemId;


    public Qna newQna(){
        return Qna.builder()
                .title(title)
                .content(content)
                .status(QnaStatus.WAITING)
                .secretYn(secretYn)
                .build();
    }

    public Qna toEntity(Long seq){
        return Qna.builder()
                .seq(seq)
                .title(title)
                .content(content)
                .attachedImgUrl(attachedImgUrl)
                .status(QnaStatus.WAITING)
                .secretYn(secretYn)
                .build();
    }

    public QnaReply newReply(){
        return QnaReply.builder()
                .title(title)
                .content(content)
                .secretYn(secretYn)
                .build();
    }

    public Id<CommonCode, String> getCateId(){
        return Id.of(CommonCode.class, cateId);
    }

    public Optional<Id<ItemDisplay, String>> getItemId(){
        if(itemId == null){
            return empty();
        }
        return Optional.of(Id.of(ItemDisplay.class, itemId));
    }
}
