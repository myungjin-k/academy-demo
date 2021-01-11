package my.myungjin.academyDemo.domain.order;

import javax.persistence.AttributeConverter;

public class DeliveryStatusConverter implements AttributeConverter<DeliveryStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(DeliveryStatus status) {
        return status.getValue();
    }

    @Override
    public DeliveryStatus convertToEntityAttribute(Integer value) {
        return DeliveryStatus.of(value);
    }
}
