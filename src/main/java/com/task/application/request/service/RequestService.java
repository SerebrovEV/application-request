package com.task.application.request.service;

import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface RequestService {
    RequestDto addRequest(CreateRequestDto createRequestDto,
                          Authentication authentication);

    RequestDto updateRequest(Integer reqId,
                             CreateRequestDto createRequestDto,
                             Authentication authentication);

    void setStatus(Integer reqId, String status, Authentication authentication);

    List<RequestDto> getAllUserRequests(Integer page, Authentication authentication, String sortBy, String orderBy);

    List<RequestDto> getAllSentRequests(Integer page, Authentication authentication, String sortBy, String orderBy);

    List<RequestDto> getAllSentRequestsByPartUserName(Integer page, String name, Authentication authentication, String sortBy, String orderBy);

    RequestDto getRequestById(Integer reqId, Authentication authentication);
}
