package com.example.todos.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
public class ApiError{

    @JsonProperty("status")
    private final int httpStatus;
    private final String error;
    private final LocalDateTime timestamp;
    private final String message;

    public ApiError(HttpStatus status, String message) {
        timestamp = LocalDateTime.now();
        this.httpStatus = status.value();
        this.error = status.getReasonPhrase();
        this.message = message;
    }
}
