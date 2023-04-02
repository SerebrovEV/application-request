package com.task.application.request.service.impl;

import com.task.application.request.dao.RequestDao;
import com.task.application.request.dao.UserDao;
import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.dto.Status;
import com.task.application.request.entity.Request;
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

    @Override
    public RequestDto addRequest(CreateRequestDto createRequestDto, Authentication authentication) {
        Request newRequest = new Request();
        newRequest.setTitle(createRequestDto.getTitle());
        newRequest.setDescription(createRequestDto.getTitle());
        newRequest.setCreatedAt(LocalDateTime.now());
        newRequest.setStatus(Status.DRAFT.name());
        newRequest.setUser(userDao.getByName(authentication.getName()));
        return requestMapper.entityToDto(requestDao.addRequest(newRequest));
    }

    @Override
    public RequestDto updateRequest(Integer reqId, CreateRequestDto createRequestDto, Authentication authentication) {
        return null;
    }

    @Override
    public void setStatus(Integer reqId, String status, Authentication authentication) {

    }

    @Override
    public List<RequestDto> getAllUserRequests(Authentication authentication) {
        return null;
    }

    @Override
    public List<RequestDto> getAllRequests(Authentication authentication) {
        List<Request> requests = requestDao.getAllRequests();
        return requestMapper.entityToDto(requests);
    }

    @Override
    public List<RequestDto> getAllRequestsByUser(Authentication authentication) {
        return null;
    }
}
