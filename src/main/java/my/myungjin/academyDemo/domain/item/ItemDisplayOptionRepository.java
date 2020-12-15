package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemDisplayOptionRepository extends JpaRepository<ItemDisplay.ItemDisplayOption, String> {

    List<ItemDisplay.ItemDisplayOption> findAllByItemDisplay(ItemDisplay display);
}
