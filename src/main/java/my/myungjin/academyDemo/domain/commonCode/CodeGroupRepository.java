package my.myungjin.academyDemo.domain.commonCode;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CodeGroupRepository extends JpaRepository<CodeGroup, String> {

    @Query("SELECT cg FROM CodeGroup cg ORDER BY cg.createAt")
    List<CodeGroup> findAll();

    Optional<CodeGroup> findByCode(String code);

    @Query("UPDATE CodeGroup cg SET cg.code = :code, cg.nameEng = :nameEng, cg.nameKor = :nameKor WHERE cg.id = :id")
    void update(@Param("id") String id, @Param("code") String code, @Param("nameEng") String nameEng, @Param("nameKor") String nameKor);

}
