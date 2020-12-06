package my.myungjin.academyDemo.domain.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {

    List<CommonCode> findAllByCodeGroup(CodeGroup codeGroup);

    Page<CommonCode> findAllByCodeGroup(CodeGroup codeGroup, Pageable pageable);

}
