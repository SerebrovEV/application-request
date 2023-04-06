package com.task.application.request.exception;

public class RequestNotFoundException extends RuntimeException{
    private final int id;

    public RequestNotFoundException(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
