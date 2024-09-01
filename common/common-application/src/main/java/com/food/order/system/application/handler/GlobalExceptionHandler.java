package com.food.order.system.application.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.ValidationException;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDTO handleException(Exception exception) {
        return buildErrorDTO(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected Error!", exception);
    }

    @ResponseBody
    @ExceptionHandler(value = {ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO handleException(ValidationException exception) {
        if (exception instanceof ConstraintViolationException) {
            String violations = extractViolationsFromException((ConstraintViolationException) exception);
            return buildErrorDTO(HttpStatus.BAD_REQUEST, violations, null);
        } else {
            return buildErrorDTO(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    private String extractViolationsFromException(ConstraintViolationException exception) {
        return exception.getConstraintViolations()
                .stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("--"));
    }

    private ErrorDTO buildErrorDTO(HttpStatus status, String message, Exception exception) {
        if (exception != null) {
            log.error(message, exception);
        } else {
            log.error(message);
        }
        return ErrorDTO.builder()
                .code(status.getReasonPhrase())
                .message(message)
                .build();
    }
}
