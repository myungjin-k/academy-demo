package my.myungjin.academyDemo.web;

import lombok.Getter;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@Getter
public class Error {

    private final String message;

    private final int status;

    Error(Throwable throwable, HttpStatus status){
        this(throwable.getMessage(), status);
    }

    Error(String message, HttpStatus status){
        this.message = message;
        this.status = status.value();
    }
}
