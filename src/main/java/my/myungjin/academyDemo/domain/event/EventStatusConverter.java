package my.myungjin.academyDemo.domain.event;

import javax.persistence.AttributeConverter;

public class EventStatusConverter implements AttributeConverter<EventStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(EventStatus status) {
        return status.getValue();
    }

    @Override
    public EventStatus convertToEntityAttribute(Integer value) {
        return EventStatus.of(value);
    }

}
