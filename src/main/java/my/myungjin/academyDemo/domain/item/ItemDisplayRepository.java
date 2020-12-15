package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.Optional;

public interface ItemDisplayRepository extends JpaRepository<ItemDisplay, String>, QuerydslPredicateExecutor<ItemDisplay> {

    Optional<ItemDisplay> findByItemMaster(ItemMaster master);

}
