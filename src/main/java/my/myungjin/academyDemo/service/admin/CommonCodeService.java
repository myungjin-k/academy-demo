package my.myungjin.academyDemo.service.admin;

import lombok.RequiredArgsConstructor;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.common.*;
import my.myungjin.academyDemo.error.NotFoundException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Optional;

@Validated
@RequiredArgsConstructor
@Service
public class CommonCodeService {

    private final CodeGroupRepository codeGroupRepository;

    private final CommonCodeRepository commonCodeRepository;
    //TODO 페이징 처리

    @Transactional(readOnly = true)
    public List<CodeGroup> findAllCategoryGroups(){
        return (List<CodeGroup>) search("C", null, null);
    }

    @Transactional(readOnly = true)
    public Page<CodeGroup> findAllGroups(Pageable pageable){
        return codeGroupRepository.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public Optional<CodeGroup> findGroupByCode(@NotBlank String code){
        return codeGroupRepository.findByCode(code);
    }

    @Transactional
    public CodeGroup registGroup(@Valid CodeGroup codeGroup){
        return saveGroup(codeGroup);
    }

    @Transactional
    public CodeGroup modifyGroup(@Valid Id<CodeGroup, String> id, @NotBlank String code,
                                 @NotBlank String nameEng, @NotBlank String nameKor){
        return findGroupById(id.value()).map(codeGroup -> {
            codeGroup.update(code, nameEng, nameKor);
            return saveGroup(codeGroup);
        }).orElseThrow(() -> new NotFoundException(CodeGroup.class, id));
    }

    @Transactional
    public String removeGroup(@Valid Id<CodeGroup, String> id){
        return findGroupById(id.value())
                .map(codeGroup -> deleteGroup(codeGroup.getId()))
                .orElseThrow(() -> new NotFoundException(CodeGroup.class, id));
    }

    @Transactional(readOnly = true)
    public List<CodeGroup> search(String code, String nameEng, String nameKor){
        return (List<CodeGroup>) codeGroupRepository.findAll(CodeGroupPredicate.search(code, nameEng, nameKor));
    }

    @Cacheable(value="commonCodeCache", key="#groupId.value()")
    @Transactional(readOnly = true)
    public List<CommonCode> findAllCommonCodesByGroupId(@Valid Id<CodeGroup, String> groupId){
        return findGroupById(groupId.value())
                .map(commonCodeRepository::findAllByCodeGroup)
                .orElseThrow(() -> new NotFoundException(CodeGroup.class, groupId));
    }

    @Transactional(readOnly = true)
    public Page<CommonCode> findAllCommonCodeByGroupIdWithPage(@Valid Id<CodeGroup, String> groupId, Pageable pageable){
        return findGroupById(groupId.value())
                .map(group ->
                    commonCodeRepository.findAllByCodeGroup(group, pageable)
                ).orElseThrow(() -> new NotFoundException(CodeGroup.class, groupId));
    }

    @CacheEvict(value="commonCodeCache", key="#groupId.value()")
    @Transactional
    public CommonCode registCommonCode(@Valid Id<CodeGroup, String> groupId, @Valid CommonCode commonCode){
        return findGroupById(groupId.value())
                .map(code -> saveCode(commonCode))
                .orElseThrow(() -> new NotFoundException(CodeGroup.class, groupId));
    }

    @CacheEvict(value="commonCodeCache", key="#groupId.value()")
    @Transactional
    public CommonCode modifyCode(@Valid Id<CodeGroup, String> groupId, @Valid Id<CommonCode, String> codeId,
                                 @NotBlank String code, @NotBlank String nameEng, @NotBlank String nameKor){
        return findCodeById(codeId)
                .map(commonCode -> {
                    commonCode.update(code, nameEng, nameKor);
                    return saveCode(commonCode);
                }).orElseThrow(() -> new NotFoundException(CommonCode.class, codeId));
    }

    @CacheEvict(value="commonCodeCache", key="#groupId.value()")
    @Transactional
    public String removeCode(@Valid Id<CodeGroup, String> groupId, @Valid Id<CommonCode, String> codeId){
        return findCodeById(codeId)
                .map(commonCode -> deleteCode(codeId.value()))
                .orElseThrow(() -> new NotFoundException(CommonCode.class, codeId));
    }

    @Transactional(readOnly = true)
    public List<CommonCode> findAllCommonCodesByGroupCode(@NotBlank String code){
        return findGroupByCode(code)
                .map(commonCodeRepository::findAllByCodeGroup)
                .orElseThrow(() -> new NotFoundException(CodeGroup.class, code));
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

    private Optional<CommonCode> findCodeById(Id<CommonCode, String> codeId){
        return commonCodeRepository.findById(codeId.value());
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
