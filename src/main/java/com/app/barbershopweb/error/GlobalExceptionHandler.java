package com.app.barbershopweb.error;

import com.app.barbershopweb.exception.NotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDto> onNotFound(Exception e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(List.of(e.getMessage())));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorDto> onControllerMethodParameterConstraintViolation(ConstraintViolationException e) {
        List<String> errors = e.getConstraintViolations()
                .stream()
                .map(
                        cv ->"'" + cv.getPropertyPath().toString().split("\\.")[1]
                                + "' " + cv.getMessage()
                )
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(errors));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        List<String> errors = ex.getFieldErrors()
                .stream()
                .map(
                        fe -> "'" + fe.getObjectName() + "." +
                                fe.getField() + "' " + fe.getDefaultMessage()
                )
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(errors));
    }
}
