package my.myungjin.academyDemo.domain.mail;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class Mail {

    private String address;

    private String title;

    private String content;

    @Builder
    public Mail(String address, String title, String content){
        this.address = address;
        this.title = title;
        this.content = content;
    }
}
