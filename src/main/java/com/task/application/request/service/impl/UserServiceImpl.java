package com.task.application.request.service.impl;

import com.task.application.request.dao.UserDao;
import com.task.application.request.dto.Role;
import com.task.application.request.dto.UserDto;
import com.task.application.request.entity.User;
import com.task.application.request.exception.UserForbiddenException;
import com.task.application.request.mapper.UserMapper;
import com.task.application.request.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    private final UserMapper userMapper;
    private final UserValidatePermission userValidate;

    @Override
    public List<UserDto> getAllUser(Authentication authentication) {
        User user = userDao.getUserByName(authentication.getName());
        if (userValidate.isAdmin(user)) {
            List<User> users = userDao.findAllUser();
            return userMapper.entityToDto(users);
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public UserDto getUserByName(String name, Authentication authentication) {
        User user = userDao.getUserByName(authentication.getName());
        if (userValidate.isAdmin(user)) {
            User result = userDao.getUserByPartOfName(name);
            return userMapper.entityToDto(result);
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public void setUserStatus(Integer userId, String role, Authentication authentication) {
        User user = userDao.getUserByName(authentication.getName());
        if (userValidate.isAdmin(user) && role.equals(Role.OPERATOR.name())) {
            User result = userDao.getUserById(userId);
            result.setRole(role);
            userDao.setUserRole(result);
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }
}
