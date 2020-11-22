package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.common.CommonCode;
import my.myungjin.academyDemo.util.Util;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
public class CommonCodeRequest {
    private String code;
    private String nameEng;
    private String nameKor;

    public CommonCode newCommonCode(String groupId){
        LocalDateTime now = LocalDateTime.now();
        return CommonCode.builder()
                .id(Util.getUUID())
                .code(code)
                .nameEng(nameEng)
                .nameKor(nameKor)
                .groupId(groupId)
                .createAt(now)
                .updateAt(now)
                .build();
    }
}
