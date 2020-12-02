package my.myungjin.academyDemo.domain.common;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface CodeGroupRepository extends JpaRepository<CodeGroup, String>, QuerydslPredicateExecutor<CodeGroup> {

    @Query("SELECT cg FROM CodeGroup cg ORDER BY cg.createAt")
    List<CodeGroup> findAll();

    Optional<CodeGroup> findByCode(String code);

}
