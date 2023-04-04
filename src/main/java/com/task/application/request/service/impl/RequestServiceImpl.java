package com.task.application.request.service.impl;

import com.task.application.request.dao.RequestDao;
import com.task.application.request.dao.UserDao;
import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.dto.Status;
import com.task.application.request.entity.Request;
import com.task.application.request.entity.User;
import com.task.application.request.exception.UserForbiddenException;
import com.task.application.request.mapper.RequestMapper;
import com.task.application.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    private final RequestDao requestDao;
    private final UserDao userDao;
    private final RequestMapper requestMapper;
    private final UserValidatePermission userValidate;
    private final RequestCheckStatus checkStatus;

    @Override
    public RequestDto addRequest(CreateRequestDto createRequestDto, Authentication authentication) {
        User user = userDao.getUserByName(authentication.getName());
        if (userValidate.isUser(user)) {
            Request newRequest = new Request();
            newRequest.setTitle(createRequestDto.getTitle());
            newRequest.setDescription(createRequestDto.getTitle());
            newRequest.setCreatedAt(LocalDateTime.now());
            newRequest.setStatus(Status.DRAFT.name());
            newRequest.setUser(user);
            return requestMapper.entityToDto(requestDao.addRequest(newRequest));
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public RequestDto updateRequest(Integer reqId, CreateRequestDto createRequestDto, Authentication authentication) {
        User user = userDao.getUserByName(authentication.getName());
        if (userValidate.isUser(user)) {
            Request changeRequest = requestDao.getRequestById(reqId);
            if (checkStatus.isDraft(changeRequest) && userValidate.isRequestOwner(user, changeRequest)) {
                changeRequest.setTitle(createRequestDto.getTitle());
                changeRequest.setDescription(createRequestDto.getDescription());
                requestDao.updateRequest(changeRequest);
                return requestMapper.entityToDto(changeRequest);
            } else {
                throw new UserForbiddenException(user.getId());
            }
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public RequestDto getRequestById(Integer reqId, Authentication authentication) {
        User user = userDao.getUserByName(authentication.getName());
        if (userValidate.isAdmin(user)) {
            throw new UserForbiddenException(user.getId());
        } else {
            Request findRequest = requestDao.getRequestById(reqId);
            if (userValidate.isOperator(user) && checkStatus.isSent(findRequest)) {
                String newTitle = findRequest.getTitle().replace("", "-");
                newTitle = newTitle.substring(1, newTitle.length() - 1);
                findRequest.setTitle(newTitle);
                return requestMapper.entityToDto(findRequest);
            } else if (userValidate.isUser(user)) {
                return requestMapper.entityToDto(findRequest);
            } else {
                throw new UserForbiddenException(user.getId());
            }
        }
    }

    @Override
    public void setStatus(Integer reqId, String status, Authentication authentication) {
        User user = userDao.getUserByName(authentication.getName());
        Request changeRequest = requestDao.getRequestById(reqId);

        if (userValidate.isOperator(user)
                && checkStatus.isSent(changeRequest)
                && (checkStatus.isAccepted(status)) || checkStatus.isRejected(status)) {

            changeRequest.setStatus(status.toUpperCase());
            requestDao.updateRequest(changeRequest);

        } else if (userValidate.isUser(user) && checkStatus.isSent(status) && checkStatus.isDraft(changeRequest)) {

            changeRequest.setStatus(status.toUpperCase());
            requestDao.updateRequest(changeRequest);
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public List<RequestDto> getAllUserRequests(Integer page, Authentication authentication, String sortBy, String
            orderBy) {
        User user = userDao.getUserByName(authentication.getName());
        if (userValidate.isUser(user)) {
            List<Request> allRequests = requestDao.getAllUserRequest(page, user.getId(), sortBy, orderBy);
            return requestMapper.entityToDto(allRequests);
        } else {
            throw new UserForbiddenException(user.getId());
        }

    }

    @Override
    public List<RequestDto> getAllSentRequests(Integer page,
                                               Authentication authentication,
                                               String sortBy,
                                               String orderBy) {
        User user = userDao.getUserByName(authentication.getName());
        if (userValidate.isOperator(user)) {
            List<Request> requests = requestDao.getAllSentRequests(page, sortBy, orderBy);
            return requestMapper.entityToDto(requests);
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }

    @Override
    public List<RequestDto> getAllSentRequestsByPartUserName(Integer page, String name, Authentication
            authentication, String sortBy, String orderBy) {
        User user = userDao.getUserByName(authentication.getName());
        if (userValidate.isOperator(user)) {
            User reqUser = userDao.getUserByPartOfName(name);
            List<Request> requests = requestDao.getAllSentRequestByUser(reqUser.getId(), page, sortBy, orderBy);
            return requestMapper.entityToDto(requests);
        } else {
            throw new UserForbiddenException(user.getId());
        }
    }


}
