package my.myungjin.academyDemo.domain.member;

import javax.persistence.AttributeConverter;

public class ReservesTypeConverter implements AttributeConverter<ReservesType, String> {

    @Override
    public String convertToDatabaseColumn(ReservesType type) {
        return type.name();
    }

    @Override
    public ReservesType convertToEntityAttribute(String name) {
        return ReservesType.valueOf(name);
    }
}
