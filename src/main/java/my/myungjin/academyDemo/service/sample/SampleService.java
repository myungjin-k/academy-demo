package my.myungjin.academyDemo.service.sample;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CodeGroupRepository;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import javax.transaction.Transactional;
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
        return findGroupById(id).map(codeGroup -> {
            codeGroup.update(code, nameEng, nameKor);
            return saveGroup(codeGroup);
        }).orElseThrow(() -> new IllegalArgumentException("invalid id =" + id));
    }

    public void deleteGroup(String id){
        findGroupById(id).map(codeGroup -> {
            codeGroupRepository.deleteById(id);
            return id;
        }).orElseThrow(() -> new IllegalArgumentException("invalid id =" + id));
    }

    public List<CommonCode> findAllCommonCodesByGroupId(Id<CodeGroup, String> groupId){
        CodeGroup group = findGroupById(groupId.value()).orElseThrow(() -> new IllegalArgumentException("invalid id =" + groupId));
        return group.getCommonCodes();
    }

    public CommonCode saveCommonCode(CommonCode commonCode){
        return commonCodeRepository.save(commonCode);
    }

    private Optional<CodeGroup> findGroupById(String id){
        return codeGroupRepository.findById(id);
    }
}
