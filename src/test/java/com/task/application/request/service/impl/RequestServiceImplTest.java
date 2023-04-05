package com.task.application.request.service.impl;

import com.task.application.request.dao.RequestDao;
import com.task.application.request.dao.UserDao;
import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.dto.Role;
import com.task.application.request.dto.Status;
import com.task.application.request.entity.Request;
import com.task.application.request.entity.User;
import com.task.application.request.exception.BadRequestException;
import com.task.application.request.exception.RequestNotFoundException;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
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
        Request newRequest = new Request();
        newRequest.setId(1);
        newRequest.setStatus(Status.DRAFT.name());
        newRequest.setTitle("Test3");
        newRequest.setDescription("Test4");
        newRequest.setUser(USER);
        CreateRequestDto createRequestDto = new CreateRequestDto();
        createRequestDto.setTitle("Test3");
        createRequestDto.setDescription("Test4");

        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));
        when(requestDao.getRequestById(1)).thenReturn(Optional.ofNullable(REQUEST));
        RequestDto expected = requestMapper.entityToDto(newRequest);
        RequestDto actual = out.updateRequest(1, createRequestDto, authentication);

        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());

    }
    @Test
    void updateRequestUserNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.empty());
        assertThatThrownBy(() -> out.updateRequest(1,new CreateRequestDto(), authentication))
                .isInstanceOf(UserNotFoundException.class);
    }

    @Test
    void updateRequestNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(Optional.ofNullable(USER));
        when(requestDao.getRequestById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> out.updateRequest(1,new CreateRequestDto(), authentication))
                .isInstanceOf(RequestNotFoundException.class);
    }
    @Test
    void updateRequestBadRequestException() {
        User user2 = new User();
        user2.setId(2);
        user2.setName("user2");
        user2.setRole(Role.USER.name());
        Request request2 = new Request();
        request2.setStatus(Status.SENT.name());

        when(authentication.getName()).thenReturn(String.valueOf(user2.getName()));
        when(userDao.getUserByName(user2.getName())).thenReturn(Optional.ofNullable(user2));
        when(requestDao.getRequestById(1)).thenReturn(Optional.ofNullable(REQUEST));
        assertThatThrownBy(() -> out.updateRequest(1,new CreateRequestDto(), authentication))
                .isInstanceOf(BadRequestException.class);

        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(Optional.ofNullable(USER));
        when(requestDao.getRequestById(1)).thenReturn(Optional.ofNullable(request2));
        assertThatThrownBy(() -> out.updateRequest(1,new CreateRequestDto(), authentication))
                .isInstanceOf(BadRequestException.class);

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
    void getRequestByIdForUser() {

        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));
        when(requestDao.getRequestById(1)).thenReturn(Optional.ofNullable(REQUEST));
        RequestDto expected = requestMapper.entityToDto(REQUEST);
        RequestDto actual = out.getRequestById(1, authentication);
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    void getRequestByIdForOperator() {
        REQUEST.setStatus(Status.SENT.name());
        Request sentRequest = new Request();
        sentRequest.setId(1);
        sentRequest.setStatus(Status.SENT.name());
        sentRequest.setTitle("T-e-s-t");
        sentRequest.setDescription("Test2");
        sentRequest.setUser(USER);
        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.ofNullable(OPERATOR));
        when(requestDao.getRequestById(1)).thenReturn(Optional.ofNullable(REQUEST));
        RequestDto expected = requestMapper.entityToDto(sentRequest);
        RequestDto actual = out.getRequestById(1, authentication);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getTitle()).isEqualTo(expected.getTitle());
        assertThat(actual.getDescription()).isEqualTo(expected.getDescription());
        assertThat(actual.getStatus()).isEqualTo(expected.getStatus());
        assertThat(actual.getUserId()).isEqualTo(expected.getUserId());
    }

    @Test
    void getRequestByIdRequestNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(Optional.ofNullable(USER));
        when(requestDao.getRequestById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> out.getRequestById(1, authentication))
                .isInstanceOf(RequestNotFoundException.class);
    }
    @Test
    void getRequestByIdBadRequestException() {
        User user2 = new User();
        user2.setId(2);
        user2.setName("user2");
        user2.setRole(Role.USER.name());

        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(Optional.ofNullable(OPERATOR));
        when(requestDao.getRequestById(1)).thenReturn(Optional.ofNullable(REQUEST));
        assertThatThrownBy(() -> out.getRequestById(1, authentication))
                .isInstanceOf(BadRequestException.class);

        when(authentication.getName()).thenReturn(String.valueOf(user2.getName()));
        when(userDao.getUserByName(user2.getName())).thenReturn(Optional.ofNullable(user2));
        when(requestDao.getRequestById(1)).thenReturn(Optional.ofNullable(REQUEST));
        assertThatThrownBy(() -> out.getRequestById(1, authentication))
                .isInstanceOf(BadRequestException.class);

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
    void setStatusForUser() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(java.util.Optional.ofNullable(USER));
        when(requestDao.getRequestById(1)).thenReturn(Optional.ofNullable(REQUEST));
        out.setStatus(1, Status.SENT.name(), authentication);
        verify(requestDao).updateRequest(REQUEST);

    }

    @Test
    void setStatusForOperator() {
        REQUEST.setStatus(Status.SENT.name());
        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(java.util.Optional.ofNullable(OPERATOR));
        when(requestDao.getRequestById(1)).thenReturn(Optional.ofNullable(REQUEST));
        out.setStatus(1, Status.ACCEPTED.name(),  authentication);
        verify(requestDao).updateRequest(REQUEST);

    }

    @Test
    void setStatusRequestNotFoundException() {
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(Optional.ofNullable(USER));
        when(requestDao.getRequestById(1)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> out.setStatus(1, "Status", authentication))
                .isInstanceOf(RequestNotFoundException.class);
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
        List<Request> requests = List.of(REQUEST);
        when(authentication.getName()).thenReturn(String.valueOf(USER.getName()));
        when(userDao.getUserByName(USER.getName())).thenReturn(Optional.ofNullable(USER));
        when(requestDao.getAllUserRequest(any(), any(), any(), any())).thenReturn(requests);
        List<RequestDto> expected = requestMapper.entityToDto(requests);
        List<RequestDto> actual = out.getAllUserRequests(1, authentication, null, null);
        assertThat(actual).isEqualTo(expected);
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
        REQUEST.setStatus(Status.SENT.name());
        List<Request> requests = List.of(REQUEST);
        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(Optional.ofNullable(OPERATOR));
        when(requestDao.getAllSentRequests(any(), any(), any())).thenReturn(requests);
        List<RequestDto> expected = requestMapper.entityToDto(requests);
        List<RequestDto> actual = out.getAllSentRequests(1, authentication, null, null);
        assertThat(actual).isEqualTo(expected);
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
        REQUEST.setStatus(Status.SENT.name());
        List<Request> requests = List.of(REQUEST);
        when(authentication.getName()).thenReturn(String.valueOf(OPERATOR.getName()));
        when(userDao.getUserByName(OPERATOR.getName())).thenReturn(Optional.ofNullable(OPERATOR));
        when(userDao.getUserByPartOfName(USER.getName())).thenReturn(Optional.ofNullable(USER));
        when(requestDao.getAllSentRequestByUser(any(), any(), any(), any())).thenReturn(requests);
        List<RequestDto> expected = requestMapper.entityToDto(requests);
        List<RequestDto> actual = out.getAllSentRequestsByPartUserName(1, USER.getName(), authentication, null, null);
        assertThat(actual).isEqualTo(expected);
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