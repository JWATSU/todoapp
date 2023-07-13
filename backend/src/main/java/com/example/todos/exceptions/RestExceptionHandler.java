package com.example.todos.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler{

    @ExceptionHandler(value = {TodoNotFoundException.class})
    protected ResponseEntity<Object> handleNotFound(TodoNotFoundException ex) {

        ApiError error = new ApiError(HttpStatus.NOT_FOUND, String.format("Todo with id %s not found.", ex.getId()));

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);

    }
}
