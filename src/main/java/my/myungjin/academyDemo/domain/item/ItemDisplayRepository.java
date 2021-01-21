package my.myungjin.academyDemo.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ItemDisplayRepository extends JpaRepository<ItemDisplay, String>, QuerydslPredicateExecutor<ItemDisplay> {

    // 상품마스터 PK로 검색
    Optional<ItemDisplay> findByItemMasterId(String masterID);

    // 상품 전시상태로 검색
    Page<ItemDisplay> findAllByStatusEquals(ItemStatus status, Pageable pageable);

    // 상품 대분류, 전시상태로 검색
    Page<ItemDisplay> findAllByItemMasterCategoryIdOrItemMasterCategoryCodeGroupIdAndStatusEquals(String categoryId, String groupId, ItemStatus status, Pageable pageable);
}
