package com.task.application.request.dto;

import lombok.Data;

@Data
public class CreateRequestDto {
    private Long id;
    private String title;
    private String description;
    private String status;
}
