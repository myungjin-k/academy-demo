package my.myungjin.academyDemo.domain.qna;

import my.myungjin.academyDemo.domain.member.Rating;

import javax.persistence.AttributeConverter;

public class QnaStatusConverter implements AttributeConverter<QnaStatus, Character> {

    @Override
    public Character convertToDatabaseColumn(QnaStatus status) {
        return status.getCode();
    }

    @Override
    public QnaStatus convertToEntityAttribute(Character code) {
        return QnaStatus.of(code);
    }
}
