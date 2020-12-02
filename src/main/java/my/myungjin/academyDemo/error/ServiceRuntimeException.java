package my.myungjin.academyDemo.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ServiceRuntimeException extends RuntimeException{

    private final String messageKey;

    private final String detailKey;

    private final Object[] params;

}
