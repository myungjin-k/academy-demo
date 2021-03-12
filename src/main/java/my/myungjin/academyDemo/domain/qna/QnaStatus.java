package my.myungjin.academyDemo.domain.qna;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QnaStatus {

    WAITING("WAITING", 'W', "답변 대기중"),
    ANSWERED("ANSWERED", 'A', "답변 완료"),
    DELETED("DELETED", 'D', "삭제된 문의")
    ;

    private final String value;

    private final char code;

    private final String description;

    public static QnaStatus of(char code){
        for(QnaStatus q : QnaStatus.values()){
            if(q.code == code){
                return q;
            }
        }
        return null;
    }

}
