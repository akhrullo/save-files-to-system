package uz.idev.simpleproject.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import uz.idev.simpleproject.dto.error.AppErrorDto;


@ControllerAdvice
@RestController
public class GlobalExceptionHandler {
    @ExceptionHandler(value = RuntimeException.class)
    public ResponseEntity<AppErrorDto> http(RuntimeException e, WebRequest request) {
        return new ResponseEntity<>(new AppErrorDto(e.getMessage(), request, HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = NotFoundException.class)
    public ResponseEntity<AppErrorDto> notFound(NotFoundException e, WebRequest request) {
        return new ResponseEntity<>(new AppErrorDto(e.getMessage(),request, HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }
}
