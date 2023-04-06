package com.task.application.request.exception;

public class UserNotFoundByIdException extends RuntimeException{
    private final int id;

    public UserNotFoundByIdException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
