package my.myungjin.academyDemo.web.response;

import lombok.Getter;
import lombok.ToString;
import my.myungjin.academyDemo.domain.member.Member;

import java.time.format.DateTimeFormatter;

@ToString
@Getter
public class AdminMemberListResponse {

    private String id;

    private String name;

    private String userId;

    private String createAt;

    public AdminMemberListResponse(Member entity) {
        this.id = entity.getId();
        this.name = entity.getName();
        this.userId = entity.getUserId();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.createAt = entity.getCreateAt().format(formatter);
    }
}
