package dev.commerce.exception;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
public class ErrorResponse implements Serializable {
    private LocalDateTime timestamp;
    private int status;
    private String path;
    private String error;
    private String message;
}
