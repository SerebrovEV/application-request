package com.task.application.request.service;

import com.task.application.request.dto.CreateRequestDto;
import com.task.application.request.dto.RequestDto;

public interface RequestService {
    RequestDto addRequest(CreateRequestDto requestDto);
}
