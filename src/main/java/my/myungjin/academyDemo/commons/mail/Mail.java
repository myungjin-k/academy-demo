package my.myungjin.academyDemo.commons.mail;


import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class Mail {

    private String to;

    private String title;

    private String content;

    @Builder
    public Mail(String to, String title, String content) {
        this.to = to;
        this.title = title;
        this.content = content;
    }
}
