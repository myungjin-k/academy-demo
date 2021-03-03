package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDisplayPriceHistoryRepository extends JpaRepository<ItemDisplayPriceHistory, String> {

    List<ItemDisplayPriceHistory> findByItemId(String itemId);

}
