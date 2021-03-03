package my.myungjin.academyDemo.domain.event;

import my.myungjin.academyDemo.domain.order.DeliveryStatus;

import javax.persistence.AttributeConverter;

public class EventTypeConverter implements AttributeConverter<EventType, String> {

    @Override
    public String convertToDatabaseColumn(EventType type) {
        return type.getValue();
    }

    @Override
    public EventType convertToEntityAttribute(String value) {
        return EventType.of(value);
    }

}
