package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.domain.common.CodeGroupRepository;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.domain.common.CommonCodeRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommonCodeService {
    private final CodeGroupRepository codeGroupRepository;
    private final CommonCodeRepository commonCodeRepository;

    public List<CodeGroup> findAllGroups(){
        return codeGroupRepository.findAll();
    }

    public Optional<CodeGroup> findGroupByCode(String code){
        return codeGroupRepository.findByCode(code);
    }

    public CodeGroup registGroup(CodeGroup codeGroup){
        return saveGroup(codeGroup);
    }

    public CodeGroup modifyGroup(String id, String code, String nameEng, String nameKor){
        return findGroupById(id).map(codeGroup -> {
            codeGroup.update(code, nameEng, nameKor);
            return saveGroup(codeGroup);
        }).orElseThrow(() -> new IllegalArgumentException("invalid id =" + id));
    }

    public String removeGroup(String id){
        return findGroupById(id)
                .map(codeGroup -> deleteGroup(id))
                .orElseThrow(() -> new IllegalArgumentException("invalid id =" + id));
    }

    @Cacheable(value="commonCodeCache", key="#groupId.value()")
    public CodeGroup findAllCommonCodesByGroupId(Id<CodeGroup, String> groupId){
        return findGroupById(groupId.value()).orElseThrow(() -> new IllegalArgumentException("invalid id =" + groupId));
    }

    @CacheEvict(value="commonCodeCache", key="#groupId.value()")
    public CommonCode registCommonCode(Id<CodeGroup, String> groupId, CommonCode commonCode){
        return findGroupById(groupId.value())
                .map(code -> saveCode(commonCode))
                .orElseThrow(() -> new IllegalArgumentException("invalid id =" + groupId));
    }

    @CacheEvict(value="commonCodeCache", key="#groupId.value()")
    public CommonCode modifyCode(Id<CodeGroup, String> groupId, Id<CommonCode, String> codeId,
                                 String code, String nameEng, String nameKor){
        return findCodeById(groupId, codeId)
                .map(commonCode -> {
                    commonCode.update(code, nameEng, nameKor);
                    return saveCode(commonCode);
                }).orElseThrow(() -> new IllegalArgumentException("invalid id =" + groupId + ", " + codeId));
    }

    @CacheEvict(value="commonCodeCache", key="#groupId.value()")
    public String removeCode(Id<CodeGroup, String> groupId, Id<CommonCode, String> codeId){
        return findCodeById(groupId, codeId)
                .map(commonCode -> deleteCode(codeId.value()))
                .orElseThrow(() -> new IllegalArgumentException("invalid id =" + groupId + ", " + codeId));
    }

    public CodeGroup findAllCommonCodesByGroupCode(String code){
        return findGroupByCode(code).orElseThrow(() -> new IllegalArgumentException("invalid code =" + code));
    }

    private CodeGroup saveGroup(CodeGroup codeGroup){
        return codeGroupRepository.save(codeGroup);
    }

    private CommonCode saveCode(CommonCode commonCode){
        return commonCodeRepository.save(commonCode);
    }

    private Optional<CodeGroup> findGroupById(String id){
        return codeGroupRepository.findById(id);
    }

    private Optional<CommonCode> findCodeById(Id<CodeGroup, String> groupId, Id<CommonCode, String> codeId){
        return findGroupById(groupId.value())
                .map(codeGroup -> commonCodeRepository.findById(codeId.value()))
                .orElseThrow(() -> new IllegalArgumentException("invalid id =" + groupId));
    }

    private String deleteGroup(String id){
        codeGroupRepository.deleteById(id);
        return id;
    }

    private String deleteCode(String id){
        commonCodeRepository.deleteById(id);
        return id;
    }
}
