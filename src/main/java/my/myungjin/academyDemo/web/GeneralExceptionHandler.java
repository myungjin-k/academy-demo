package my.myungjin.academyDemo.web;

import my.myungjin.academyDemo.error.NotFoundException;
import my.myungjin.academyDemo.error.ServiceRuntimeException;
import my.myungjin.academyDemo.error.UnauthorizedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static my.myungjin.academyDemo.web.Response.ERROR;

@ControllerAdvice
public class GeneralExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private ResponseEntity<Response<?>> newResponse(Throwable throwable, HttpStatus status){
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        return new ResponseEntity<>(ERROR(throwable, status), headers, status);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {IllegalStateException.class, IllegalArgumentException.class, TypeMismatchException.class,
            MissingServletRequestParameterException.class})
    protected ResponseEntity<Response<?>> badRequestHandler(Exception e){
        return newResponse(e, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<Response<?>> methodNotAllowedHandler(Exception e){
        return newResponse(e, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(ServiceRuntimeException.class)
    public ResponseEntity<?> handleServiceRuntimeException(ServiceRuntimeException e) {
        if (e instanceof NotFoundException)
            return newResponse(e, HttpStatus.NOT_FOUND);
        if (e instanceof UnauthorizedException)
            return newResponse(e, HttpStatus.UNAUTHORIZED);

        log.warn("Unexpected service exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    protected ResponseEntity<Response<?>> exceptionHandler(Exception e){
        log.error("Unexpected exception occurred: {}", e.getMessage(), e);
        return newResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
