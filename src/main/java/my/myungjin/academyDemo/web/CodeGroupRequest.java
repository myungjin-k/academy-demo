package my.myungjin.academyDemo.web;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.common.CodeGroup;
import my.myungjin.academyDemo.util.Util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import static my.myungjin.academyDemo.util.Util.timestampOf;

@AllArgsConstructor
@Getter
@ToString
public class CodeGroupRequest {
    private String code;
    private String nameEng;
    private String nameKor;

    public CodeGroup newCodeGroup(){
        Timestamp now = timestampOf(LocalDateTime.now());
        return CodeGroup.builder()
                .id(Util.getUUID())
                .code(code)
                .nameEng(nameEng)
                .nameKor(nameKor)
                .createAt(now)
                .updateAt(now)
                .build();
    }
}
