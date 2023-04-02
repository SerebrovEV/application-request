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

    List<RequestDto> getAllUserRequests(Authentication authentication);

    List<RequestDto> getAllRequests(Authentication authentication);

    List<RequestDto> getAllRequestsByUser(Authentication authentication);

}
