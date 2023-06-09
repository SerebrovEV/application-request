package com.task.application.request.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class ExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body((String.format("Пользователь c name = %s  не найден", e.getName())));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundByIdException.class)
    public ResponseEntity<String> handleUserNotFoundByIdException(UserNotFoundByIdException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body((String.format("Пользователь c ID = %d  не найден", e.getId())));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(RequestNotFoundException.class)
    public ResponseEntity<String> handleRequestNotFoundException(RequestNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body((String.format("Заявка c id = %d  не найдена", e.getId())));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(UserForbiddenException.class)
    public ResponseEntity<String> handleUserForbiddenException(UserForbiddenException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body((String.format("Пользователю c id = %d  доступ запрещен", e.getId())));
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(BadRequestException.class)
    public ResponseEntity<String> handleBadRequestException(BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body((("Некорректный запрос")));
    }
}
