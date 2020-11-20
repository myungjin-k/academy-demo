package my.myungjin.academyDemo.service.sample;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CodeGroupRepository;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SampleService {
    private final CodeGroupRepository codeGroupRepository;
    private final CommonCodeRepository commonCodeRepository;

    public List<CodeGroup> findAllGroups(){
        return codeGroupRepository.findAll();
    }

    public Optional<CodeGroup> findGroupByCode(String code){
        return codeGroupRepository.findByCode(code);
    }

    public CodeGroup saveGroup(CodeGroup codeGroup){
        return codeGroupRepository.save(codeGroup);
    }

    public CodeGroup modifyGroup(String id, String code, String nameEng, String nameKor){
        return codeGroupRepository.findById(id).map(codeGroup -> {
            codeGroup.update(code, nameEng, nameKor);
            return saveGroup(codeGroup);
        }).orElseThrow(() -> new IllegalArgumentException("invalid id =" + id));
    }

    public String deleteGroup(String id){
        return codeGroupRepository.findById(id).map(codeGroup -> {
            codeGroupRepository.deleteById(id);
            return id;
        }).orElseThrow(() -> new IllegalArgumentException("invalid id =" + id));
    }

}
