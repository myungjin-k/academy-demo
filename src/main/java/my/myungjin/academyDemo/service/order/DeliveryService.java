package my.myungjin.academyDemo.service.order;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.item.ItemDisplay;
import my.myungjin.academyDemo.domain.item.ItemDisplayOptionRepository;
import my.myungjin.academyDemo.domain.order.Delivery;
import my.myungjin.academyDemo.domain.order.DeliveryItem;
import my.myungjin.academyDemo.domain.order.DeliveryItemRepository;
import my.myungjin.academyDemo.domain.order.DeliveryRepository;
import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.util.Util;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final DeliveryItemRepository deliveryItemRepository;

    private final ItemDisplayOptionRepository itemDisplayOptionRepository;

    @Transactional(readOnly = true)
    public Delivery findById(@Valid Id<Delivery, String> deliveryId){
        return deliveryRepository.findById(deliveryId.value())
                .map(delivery -> {
                    delivery.setItems(deliveryItemRepository.findAllByDelivery(delivery));
                    return delivery;
                }).orElseThrow(() -> new NotFoundException(Delivery.class, deliveryId));
    }

    @Transactional(readOnly = true)
    public Optional<DeliveryItem> findItemByDeliveryAndItem(@Valid Id<Delivery, String> deliveryId, @Valid Id<ItemDisplay.ItemDisplayOption, String> itemId){
        return deliveryItemRepository.findByDelivery_idAndItemOption_id(deliveryId.value(), itemId.value());
    }

    @Transactional
    public Delivery addDeliveryItem(@Valid Id<Delivery, String> deliveryId, @Valid Id<ItemDisplay.ItemDisplayOption, String> itemId){
        return deliveryRepository.findById(deliveryId.value())
                .map(delivery -> {
                    boolean chk = deliveryItemRepository.existsByDelivery_idAndItemOption_id(deliveryId.value(), itemId.value());
                    if(!chk){
                        DeliveryItem item = new DeliveryItem(Util.getUUID());
                        saveItem(item, itemId);
                        delivery.addItem(item);
                    }
                    return save(delivery);
                }).orElseThrow(() -> new NotFoundException(Delivery.class, deliveryId));
    }

    @Transactional
    public DeliveryItem deleteDeliveryItem(@Valid Id<Delivery, String> deliveryId, @Valid Id<ItemDisplay.ItemDisplayOption, String> itemId){
        return findItemByDeliveryAndItem(deliveryId, itemId)
                .map(deliveryItem -> {
                    delete(deliveryItem);
                    return deliveryItem;
                }).orElseThrow(() -> new NotFoundException(DeliveryItem.class, deliveryId, itemId));
    }

    private void saveItem(DeliveryItem deliveryItem, Id<ItemDisplay.ItemDisplayOption, String> itemId){
        itemDisplayOptionRepository.findById(itemId.value())
            .map(displayOption -> {
                deliveryItem.setItemOption(displayOption);
                return save(deliveryItem);
            }).orElseThrow(() ->  new NotFoundException(ItemDisplay.ItemDisplayOption.class, itemId));
    }

    private DeliveryItem save(DeliveryItem item){
        return deliveryItemRepository.save(item);
    }

    private Delivery save(Delivery delivery){
        return deliveryRepository.save(delivery);
    }

    private void delete(DeliveryItem deliveryItem){
        deliveryItemRepository.delete(deliveryItem);
    }
}
