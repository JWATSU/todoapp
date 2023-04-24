package com.example.azurespringboottutorial.exceptions;

public class TodoNotFoundException extends RuntimeException {

    private final Long id;

    public TodoNotFoundException(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
