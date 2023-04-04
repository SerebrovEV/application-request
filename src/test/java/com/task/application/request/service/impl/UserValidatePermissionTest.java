package com.task.application.request.service.impl;

import com.task.application.request.dto.Role;
import com.task.application.request.entity.Request;
import com.task.application.request.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class UserValidatePermissionTest {

    UserValidatePermission out = new UserValidatePermission();
    User USER;
    User ADMIN;
    User OPERATOR;

    @BeforeEach
    public void setOut() {
        USER = new User();
        USER.setRole(Role.USER.name());

        ADMIN = new User();
        ADMIN.setRole(Role.ADMIN.name());

        OPERATOR = new User();
        OPERATOR.setRole(Role.OPERATOR.name());
    }

    @Test
    void isAdmin() {
        assertTrue(out.isAdmin(ADMIN));
        assertFalse(out.isAdmin(USER));
        assertFalse(out.isAdmin(OPERATOR));
    }

    @Test
    void isOperator() {
        assertFalse(out.isOperator(ADMIN));
        assertTrue(out.isOperator(OPERATOR));
        assertFalse(out.isOperator(USER));
    }

    @Test
    void isUser() {
        assertFalse(out.isUser(ADMIN));
        assertTrue(out.isUser(USER));
        assertFalse(out.isUser(OPERATOR));
    }

    @Test
    void isRequestOwner() {
        Request request = new Request();
        request.setUser(USER);
        assertTrue(out.isRequestOwner(USER, request));
        assertFalse(out.isRequestOwner(ADMIN, request));
        assertFalse(out.isRequestOwner(OPERATOR, request));
    }
}