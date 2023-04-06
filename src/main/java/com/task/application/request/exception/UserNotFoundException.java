package com.task.application.request.exception;

public class UserNotFoundException extends RuntimeException {
    private final String name;

    public UserNotFoundException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
