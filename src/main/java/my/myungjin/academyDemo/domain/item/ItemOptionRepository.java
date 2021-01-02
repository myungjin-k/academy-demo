package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOptionRepository extends JpaRepository<ItemMaster.ItemOption, String> {

    // 상품 마스터 엔티티로 검색
    List<ItemMaster.ItemOption> findAllByItemMaster(ItemMaster itemMaster);

}
