package my.myungjin.academyDemo.commons.mail;


import lombok.*;

import java.util.Map;

@Getter
@ToString
@NoArgsConstructor
public class Mail {

    private String to;

    private String title;

    private Map<String, Object> variables;

    private String htmlBody;

    @Builder
    public Mail(String to, String title, Map<String, Object> variables, String htmlBody) {
        this.to = to;
        this.title = title;
        this.variables = variables;
        this.htmlBody = htmlBody;
    }
}
