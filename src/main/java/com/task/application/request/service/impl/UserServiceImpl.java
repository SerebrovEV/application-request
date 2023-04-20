package com.task.application.request.service.impl;

import com.task.application.request.dto.Role;
import com.task.application.request.dto.UserDto;
import com.task.application.request.entity.User;
import com.task.application.request.exception.BadRequestException;
import com.task.application.request.exception.UserNotFoundByIdException;
import com.task.application.request.exception.UserNotFoundException;
import com.task.application.request.mapper.UserMapper;
import com.task.application.request.repository.UserRepository;
import com.task.application.request.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public List<UserDto> getAllUser(Authentication authentication) {
        List<User> users = userRepository.findAll();
        return userMapper.entityToDto(users);
    }

    @Override
    public UserDto getUserByName(String name, Authentication authentication) {
        User result = userRepository.getUserByPartOfName(name)
                .orElseThrow(() -> new UserNotFoundException(name));
        return userMapper.entityToDto(result);
    }

    @Override
    public void setUserRole(Integer userId, String role, Authentication authentication) {
        if (role.equals(Role.OPERATOR.name())) {
            User result = userRepository.findById(userId)
                    .orElseThrow(() -> new UserNotFoundByIdException(userId));
            result.setRole(result.getRole() + ", " + role);
//            u.setUserRole(result);
        } else {
            throw new BadRequestException();
        }
    }

    @Override
    public User findUserByName(String name) {
        return userRepository.findByName(name)
                .orElseThrow(() -> new UserNotFoundException(name));
    }
}
