package dev.commerce.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {
    @ExceptionHandler(Exception.class)
    public String handleAllExceptions(Exception ex) {
        return "An error occurred: " + ex.getMessage();
    }
}
