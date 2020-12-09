package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ItemDisplayRepository extends JpaRepository<ItemDisplay, String> {

    Optional<ItemDisplay> findByItemMaster(ItemMaster master);

}
