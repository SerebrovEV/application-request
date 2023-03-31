package com.task.application.request.service.impl;

import com.task.application.request.dao.RequestDao;
import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.entity.RequestEntity;
import com.task.application.request.entity.UserEntity;
import com.task.application.request.mapper.RequestMapper;
import com.task.application.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {

    private final RequestMapper requestMapper;
    private final RequestDao requestDao;

    @Override
    public RequestDto addRequest(CreateRequestDto request) {
        RequestDto newRequest = new RequestDto();
        newRequest.setDescription(request.getDescription());
        newRequest.setTitle(request.getTitle());
        newRequest.setStatus("false");
        newRequest.setUserId(1L);
        RequestEntity saveRequest = requestMapper.dtoToEntity(newRequest);
        saveRequest.setCreatedAt(LocalDateTime.now());
        saveRequest.setUser(new UserEntity());
        return requestMapper.entityToDto(requestDao.addRequest(saveRequest));
    }

    @Override
    public RequestDto get(Long reqId) {
        return requestMapper.entityToDto(requestDao.getRequest(reqId));
    }
}
