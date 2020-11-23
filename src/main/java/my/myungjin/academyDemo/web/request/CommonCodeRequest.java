package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.commons.Id;
import my.myungjin.academyDemo.domain.commonCode.CodeGroup;
import my.myungjin.academyDemo.domain.commonCode.CommonCode;
import my.myungjin.academyDemo.util.Util;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

@AllArgsConstructor
@Getter
@ToString
public class CommonCodeRequest {
    private String code;
    private String nameEng;
    private String nameKor;

    public CommonCode newCommonCode(Id<CodeGroup, String> groupId){
        LocalDateTime now = now();
        return CommonCode.builder()
                .id(Util.getUUID())
                .code(code)
                .nameEng(nameEng)
                .nameKor(nameKor)
                .groupId(groupId.value())
                .createAt(now)
                .updateAt(now)
                .build();
    }
}
