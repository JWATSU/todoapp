package com.example.azurespringboottutorial.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {TodoNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(TodoNotFoundException ex) {

        ApiError error = new ApiError(HttpStatus.NOT_FOUND, String.format("Todo with id %s not found.", ex.getId()));

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }
}
