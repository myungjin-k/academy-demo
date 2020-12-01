package my.myungjin.academyDemo.web.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.util.Util;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
@ToString
public class CodeGroupRequest {
    private String code;
    private String nameEng;
    private String nameKor;

    public CodeGroup newCodeGroup(){
        return CodeGroup.builder()
                .id(Util.getUUID())
                .code(code)
                .nameEng(nameEng)
                .nameKor(nameKor)
                .build();
    }


}
