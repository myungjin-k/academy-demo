package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ItemMasterRepository extends JpaRepository<ItemMaster, String> {

    List<ItemMaster> findAllByMainCategoryId(String mainCategoryId);

}
