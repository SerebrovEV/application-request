package com.task.application.request.dto;

import lombok.Data;

@Data
public class RequestDto {
    private Integer id;
    private String title;
    private String description;
    private String createdAt;
    private String status;
    private Integer userId;
}
