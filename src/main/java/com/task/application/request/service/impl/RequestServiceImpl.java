package com.task.application.request.service.impl;

import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import com.task.application.request.service.RequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestServiceImpl implements RequestService {
    @Override
    public RequestDto addRequest(CreateRequestDto createRequestDto, Authentication authentication) {
        return null;
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
        return null;
    }

    @Override
    public List<RequestDto> getAllRequestsByUser(Authentication authentication) {
        return null;
    }
}
