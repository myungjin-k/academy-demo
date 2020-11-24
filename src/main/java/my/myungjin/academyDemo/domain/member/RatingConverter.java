package my.myungjin.academyDemo.domain.member;

import javax.persistence.AttributeConverter;

public class RatingConverter implements AttributeConverter<Rating, String> {

    @Override
    public String convertToDatabaseColumn(Rating rating) {
        return rating.getCode();
    }

    @Override
    public Rating convertToEntityAttribute(String code) {
        return Rating.of(code);
    }
}
