package my.myungjin.academyDemo.domain.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {

    Page<CommonCode> findAllByGroupId(String groupId, Pageable pageable);

}
