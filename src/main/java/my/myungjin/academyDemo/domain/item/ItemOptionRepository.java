package my.myungjin.academyDemo.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface ItemOptionRepository extends JpaRepository<ItemMaster.ItemOption, String>, QuerydslPredicateExecutor<ItemMaster.ItemOption> {

    // 상품 마스터 엔티티로 검색
    List<ItemMaster.ItemOption> findAllByItemMaster(ItemMaster itemMaster);

    // 상품 마스터 엔티티로 검색
    Page<ItemMaster.ItemOption> findAllByItemMaster(ItemMaster itemMaster, Pageable pageable);

}
