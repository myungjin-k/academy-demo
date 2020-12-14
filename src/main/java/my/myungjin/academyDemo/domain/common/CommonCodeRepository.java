package my.myungjin.academyDemo.domain.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {

    List<CommonCode> findAllByCodeGroup(CodeGroup codeGroup);

    Page<CommonCode> findAllByCodeGroup(CodeGroup codeGroup, Pageable pageable);

    Page<CommonCode> findByNameKorContaining(String nameKor, Pageable pageable);

    @Query("select c from CommonCode c join c.codeGroup g where g.code like ?1% and c.nameKor like ?2%")
    List<CommonCode> searchByGroupCodeAndNameKor(String groupCode, String nameKor);

}
