package my.myungjin.academyDemo.domain.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface CodeGroupRepository extends JpaRepository<CodeGroup, String>, QuerydslPredicateExecutor<CodeGroup> {

    // 전체 검색
    Page<CodeGroup> findAll(Pageable pageable);

    // 코드로 검색
    Optional<CodeGroup> findByCode(String code);

}
