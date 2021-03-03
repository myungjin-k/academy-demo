package my.myungjin.academyDemo.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDisplayOptionRepository extends JpaRepository<ItemDisplayOption, String> {

    // 전시상품 엔티티로 검색
    List<ItemDisplayOption> findAllByItemDisplay(ItemDisplay display);

    // 전시상품 엔티티로 검색
    Page<ItemDisplayOption> findAllByItemDisplay(ItemDisplay display, Pageable pageable);

    List<ItemDisplayOption> findAllByItemDisplayIdEqualsOrItemDisplayItemDisplayNameContaining(String displayId, String displayName);
}
