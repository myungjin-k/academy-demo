package my.myungjin.academyDemo.domain.common;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommonCodeRepository extends JpaRepository<CommonCode, String> {

    // 코드그룹으로 검색
    List<CommonCode> findAllByCodeGroup(CodeGroup codeGroup);

    // 코드그룹으로 검색(페이징)
    Page<CommonCode> findAllByCodeGroup(CodeGroup codeGroup, Pageable pageable);

    // 코드이름으로 검색
    Page<CommonCode> findByNameKorContaining(String nameKor, Pageable pageable);

    // 코드그룹과 코드명으로 검색
    List<CommonCode> findAllByCodeGroupCodeStartsWithAndNameKorContaining(String groupCode, String nameKor);

}
