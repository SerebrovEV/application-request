package com.task.application.request.service.impl;

import com.task.application.request.dto.Role;
import com.task.application.request.entity.Request;
import com.task.application.request.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserValidatePermission {

    public boolean isAdmin(User user){
        System.out.println(user.getRole());
        return user.getRole().contains(Role.ADMIN.name());
    }

    public boolean isOperator(User user){
        return user.getRole().contains(Role.OPERATOR.name());
    }

    public boolean isUser(User user){
        return user.getRole().contains(Role.USER.name());
    }

    public boolean isRequestOwner(User user, Request request){
        return request.getUser().equals(user);
    }
}
