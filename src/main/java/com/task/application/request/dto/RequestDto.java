package com.task.application.request.dto;

import com.task.application.request.entity.UserEntity;
import lombok.Data;

@Data
public class RequestDto {
    private Long id;

    private String title;

    private String description;

    private String createdAt;

    private String status;
    private Long userId;

}
