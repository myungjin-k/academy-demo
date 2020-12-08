package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemOptionRepository extends JpaRepository<ItemOption, String> {

    List<ItemOption> findAllByItemMaster(ItemMaster itemMaster);

}
