package my.myungjin.academyDemo.domain.item;

import javax.persistence.AttributeConverter;

public class ItemStatusConverter implements AttributeConverter<ItemStatus, Integer> {

    @Override
    public Integer convertToDatabaseColumn(ItemStatus status) {
        return status.getValue();
    }

    @Override
    public ItemStatus convertToEntityAttribute(Integer value) {
        return ItemStatus.of(value);
    }
}
