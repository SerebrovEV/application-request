package com.task.application.request.service.impl;

import com.task.application.request.dao.RequestDao;
import com.task.application.request.dao.UserDao;
import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.dto.Role;
import com.task.application.request.dto.Status;
import com.task.application.request.entity.Request;
import com.task.application.request.entity.User;
import com.task.application.request.exception.UserForbiddenException;
import com.task.application.request.exception.UserNotFoundException;
import com.task.application.request.mapper.RequestMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RequestServiceImplTest {
    @InjectMocks
    RequestServiceImpl out;

    @Mock
    RequestDao requestDao;

    @Mock
    UserDao userDao;

    @Mock
    Authentication authentication;

    @Spy
    RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);

    @Spy
    UserValidatePermission userValidatePermission;

    @Spy
    RequestCheckStatus requestCheckStatus;

    User USER;
    User ADMIN;
    User OPERATOR;
    Request REQUEST;

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

        REQUEST = new Request();
        REQUEST.setId(1);
        REQUEST.setStatus(Status.DRAFT.name());
        REQUEST.setTitle("Test");
        REQUEST.setDescription("Test2");
        REQUEST.setCreatedAt(LocalDateTime.now());
        REQUEST.setUser(USER);
    }

    @Test
    void addRequest() {
        CreateRequestDto createRequestDto = new CreateRequestDto();
        createRequestDto.setTitle("Test");
        createRequestDto.setDescription("Test2");
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));
        when(requestDao.addRequest(any())).thenReturn(REQUEST);
        RequestDto expected = requestMapper.entityToDto(REQUEST);
        RequestDto actual = out.addRequest(createRequestDto, authentication);

        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void addRequestUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.addRequest(new CreateRequestDto(), authentication))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void addRequestUserForbiddenException() {
        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        assertThatThrownBy(() -> out.addRequest(new CreateRequestDto(), authentication))
                .isInstanceOf(UserForbiddenException.class);

        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.ofNullable(OPERATOR));
        assertThatThrownBy(() -> out.addRequest(new CreateRequestDto(), authentication))
                .isInstanceOf(UserForbiddenException.class);
    }

    @Test
    void updateRequest() {
    }

    @Test
    void updateRequestUserForbiddenException() {
        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        assertThatThrownBy(() -> out.updateRequest(1, new CreateRequestDto(), authentication))
                .isInstanceOf(UserForbiddenException.class);

        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.ofNullable(OPERATOR));
        assertThatThrownBy(() -> out.updateRequest(1, new CreateRequestDto(), authentication))
                .isInstanceOf(UserForbiddenException.class);
    }

    @Test
    void getRequestById() {
    }

    @Test
    void getRequestByIdUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.getRequestById(1, authentication))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getRequestByIdUserForbiddenException() {

        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        assertThatThrownBy(() -> out.getRequestById(1, authentication))
                .isInstanceOf(UserForbiddenException.class);
    }

    @Test
    void setStatus() {
    }

    @Test
    void setStatusUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.setStatus(1, "Test", authentication))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void setStatusUserForbiddenException() {
        when(requestDao.getRequestById(any())).thenReturn(java.util.Optional.ofNullable(REQUEST));
        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        assertThatThrownBy(() -> out.setStatus(1, "Test", authentication))
                .isInstanceOf(UserForbiddenException.class);
    }

    @Test
    void getAllUserRequests() {
    }

    @Test
    void getAllUserRequestsUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.getAllUserRequests(1, authentication, null, null))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getAllUserRequestsUserForbiddenException() {
        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.ofNullable(OPERATOR));
        assertThatThrownBy(() -> out.getAllUserRequests(1, authentication, null, null))
                .isInstanceOf(UserForbiddenException.class);

        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        assertThatThrownBy(() -> out.getAllUserRequests(1, authentication, null, null))
                .isInstanceOf(UserForbiddenException.class);
    }

    @Test
    void getAllSentRequests() {
    }

    @Test
    void getAllSentRequestsUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.getAllSentRequests(1, authentication, null, null))
                .isInstanceOf(UserNotFoundException.class);
    }


    @Test
    void getAllSentRequestsUserForbiddenException() {
        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        assertThatThrownBy(() -> out.getAllSentRequests(1, authentication, null, null))
                .isInstanceOf(UserForbiddenException.class);

        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));
        assertThatThrownBy(() -> out.getAllSentRequests(1, authentication, null, null))
                .isInstanceOf(UserForbiddenException.class);
    }

    @Test
    void getAllSentRequestsByPartUserName() {
    }

    @Test
    void getAllSentRequestsByPartUserNameUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.getAllSentRequestsByPartUserName(1, USER.getName(), authentication, null, null))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void getAllSentRequestsByPartUserNameUserForbiddenException() {
        when(authentication.getName()).thenReturn(String.valueOf(ADMIN.getName()));
        when(userDao.getUserByName(ADMIN.getName())).thenReturn(java.util.Optional.ofNullable(ADMIN));
        assertThatThrownBy(() -> out.getAllSentRequestsByPartUserName(1, USER.getName(), authentication, null, null))
                .isInstanceOf(UserForbiddenException.class);

        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));
        assertThatThrownBy(() -> out.getAllSentRequestsByPartUserName(1, USER.getName(), authentication, null, null))
                .isInstanceOf(UserForbiddenException.class);
    }
}