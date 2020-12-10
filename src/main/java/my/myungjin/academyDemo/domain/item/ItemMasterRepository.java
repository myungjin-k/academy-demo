package my.myungjin.academyDemo.domain.item;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface ItemMasterRepository extends JpaRepository<ItemMaster, String>, QuerydslPredicateExecutor<ItemMaster> {

    Page<ItemMaster> findAll(Pageable pageable);

    List<ItemMaster> findAllByCategoryId(String categoryId);

}
