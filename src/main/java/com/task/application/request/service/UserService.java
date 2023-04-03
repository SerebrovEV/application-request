package com.task.application.request.service;

import com.task.application.request.dto.UserDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    List<UserDto> getAllUser(Authentication authentication);

    UserDto getUserByName(String name, Authentication authentication);

    void setUserStatus(Integer userId, String role, Authentication authentication);
}
