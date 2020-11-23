package my.myungjin.academyDemo.web;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@ToString
@Getter
@RequiredArgsConstructor
public class Response<T> {

    private final boolean success;

    private final T response;

    private final Error error;

    public static <T> Response<T> OK(T response){
        return new Response<>(true, response, null);
    }

    public static Response<?> ERROR(Throwable throwable, HttpStatus status){
        return new Response<>(false, null, new Error(throwable, status));
    }

    public static Response<?> ERROR(String message, HttpStatus status){
        return new Response<>(false, null, new Error(message, status));
    }
}
