package com.task.application.request.service.impl;

import com.task.application.request.dto.UserDto;
import com.task.application.request.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService  {
    @Override
    public List<UserDto> getAllUser(Authentication authentication) {
        return null;
    }

    @Override
    public UserDto getUserByName(String name, Authentication authentication) {
        return null;
    }

    @Override
    public void setUserStatus(Integer userId, String status, Authentication authentication) {

    }
}
