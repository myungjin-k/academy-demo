package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOptionRepository extends JpaRepository<ItemMaster.ItemOption, String> {

    List<ItemMaster.ItemOption> findAllByItemMaster(ItemMaster itemMaster);

}
