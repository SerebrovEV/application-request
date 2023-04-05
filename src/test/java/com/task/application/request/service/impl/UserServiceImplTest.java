package com.task.application.request.service.impl;

import com.task.application.request.dao.UserDao;
import com.task.application.request.dto.Role;
import com.task.application.request.dto.UserDto;
import com.task.application.request.entity.User;
import com.task.application.request.exception.UserForbiddenException;
import com.task.application.request.exception.UserNotFoundByIdException;
import com.task.application.request.exception.UserNotFoundException;
import com.task.application.request.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl out;

    @Mock
    UserDao userDao;

    @Mock
    Authentication authentication;

    @Spy
    UserMapper userMapper = Mappers.getMapper(UserMapper.class);

    @Spy
    UserValidatePermission userValidatePermission;

    User USER;
    User ADMIN;
    User OPERATOR;

    @BeforeEach
    public void setOut() {
        USER = new User();
        USER.setId(1);
        USER.setName("user");
        USER.setRole(Role.USER.name());
        ADMIN = new User();
        ADMIN.setId(2);
        ADMIN.setName("admin");
        ADMIN.setRole(Role.ADMIN.name());
        OPERATOR = new User();
        OPERATOR.setId(3);
        OPERATOR.setName("operator");
        OPERATOR.setRole(Role.OPERATOR.name());
    }

    @Test
    void getAllUser() {
        User user2 = new User();
        List<User> users = List.of(USER, ADMIN, user2);
        List<UserDto> expected = userMapper.entityToDto(users);

        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        when(userDao.findAllUser()).thenReturn(users);
        List<UserDto> actual = out.getAllUser(authentication);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getAllUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.getAllUser(authentication)).isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getAllUserForbiddenException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));
        assertThatThrownBy(() -> out.getAllUser(authentication)).isInstanceOf(UserForbiddenException.class);

        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.ofNullable(OPERATOR));
        assertThatThrownBy(() -> out.getAllUser(authentication)).isInstanceOf(UserForbiddenException.class);
    }

    @Test
    void getUserByName() {
        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        when(userDao.getUserByPartOfName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));

        UserDto expected = userMapper.entityToDto(USER);
        UserDto actual = out.getUserByName(USER.getName(), authentication);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getUserByNameUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.getUserByName(USER.getName(), authentication))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getUserByNameUserNotFoundExceptionSecondTest() {
        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        when(userDao.getUserByPartOfName(USER.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.getUserByName(USER.getName(), authentication))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getUserByNameUserForbiddenException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));
        assertThatThrownBy(() -> out.getUserByName(USER.getName(), authentication))
                .isInstanceOf(UserForbiddenException.class);

        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.ofNullable(OPERATOR));
        assertThatThrownBy(() -> out.getUserByName(USER.getName(), authentication))
                .isInstanceOf(UserForbiddenException.class);
    }

    @Test
    void setUserStatus() {
        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        when(userDao.getUserById(USER.getId())).thenReturn(java.util.Optional.ofNullable(USER));

        out.setUserRole(1, "OPERATOR", authentication);
        verify(userDao).setUserRole(USER);
    }

    @Test
    void setUserStatusUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.setUserRole(1, "OPERATOR", authentication))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void setUserStatusUserNotFoundExceptionSecondTest() {
        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        when(userDao.getUserById(USER.getId())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.setUserRole(1, "OPERATOR", authentication))
                .isInstanceOf(UserNotFoundByIdException.class);
    }

    @Test
    void setUserStatusUserForbiddenException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));
        assertThatThrownBy(() -> out.setUserRole(USER.getId(), "OPERATOR", authentication))
                .isInstanceOf(UserForbiddenException.class);

        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.ofNullable(OPERATOR));
        assertThatThrownBy(() -> out.setUserRole(USER.getId(), "OPERATOR", authentication))
                .isInstanceOf(UserForbiddenException.class);
    }
}