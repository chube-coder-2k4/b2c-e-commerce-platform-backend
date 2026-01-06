package dev.commerce.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalHandlerException {

    ErrorResponse e = new ErrorResponse();

    @ExceptionHandler(Exception.class)
    @ResponseStatus(INTERNAL_SERVER_ERROR)
    public ErrorResponse handleAllExceptions(Exception ex, WebRequest request) {
        e.setMessage(ex.getMessage());
        e.setTimestamp(LocalDateTime.now());
        e.setStatus(INTERNAL_SERVER_ERROR.value());
        e.setError(INTERNAL_SERVER_ERROR.getReasonPhrase());
        e.setPath(request.getContextPath());
        return e;
    }

    @ExceptionHandler({ResourceNotFoundException.class, UserNotFoundException.class})
    @ResponseStatus(NOT_FOUND)
    public ErrorResponse handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        e.setMessage(ex.getMessage());
        e.setTimestamp(LocalDateTime.now());
        e.setStatus(NOT_FOUND.value());
        e.setPath(request.getContextPath());
        e.setError(NOT_FOUND.getReasonPhrase());
        return e;
    }

    @ExceptionHandler(InvalidDataException.class)
    @ResponseStatus(CONFLICT)
    public ErrorResponse handleInvalidDataException(InvalidDataException ex, WebRequest request) {
        e.setMessage(ex.getMessage());
        e.setTimestamp(LocalDateTime.now());
        e.setStatus(CONFLICT.value());
        e.setPath(request.getContextPath());
        e.setError(CONFLICT.getReasonPhrase());
        return e;
    }

}
