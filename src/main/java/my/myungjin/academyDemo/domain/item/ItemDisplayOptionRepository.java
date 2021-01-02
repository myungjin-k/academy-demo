package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDisplayOptionRepository extends JpaRepository<ItemDisplay.ItemDisplayOption, String> {

    // 전시상품 엔티티로 검색
    List<ItemDisplay.ItemDisplayOption> findAllByItemDisplay(ItemDisplay display);
}
