package my.myungjin.academyDemo.domain.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemMasterRepository extends JpaRepository<ItemMaster, String>, QuerydslPredicateExecutor<ItemMaster> {

    @Query("select im from ItemMaster im order by im.createAt desc")
    List<ItemMaster> findAllDesc();

    List<ItemMaster> findAllByCategoryId(String categoryId);

}
