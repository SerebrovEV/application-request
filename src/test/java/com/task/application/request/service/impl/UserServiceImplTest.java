package com.task.application.request.service.impl;

import com.task.application.request.dao.UserDao;
import com.task.application.request.mapper.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl out;

    @Mock
    UserDao userDao;
    @Spy
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Spy
    UserValidatePermission userValidatePermission = new UserValidatePermission();

    @Test
    void getAllUser() {
    }

    @Test
    void getUserByName() {
    }

    @Test
    void setUserStatus() {
    }
}