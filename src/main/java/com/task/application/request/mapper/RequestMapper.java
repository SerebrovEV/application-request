package com.task.application.request.mapper;

import com.task.application.request.dto.RequestDto;
import com.task.application.request.entity.Request;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface RequestMapper {
    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd-MM-yyyy HH:mm:ss")
    Request dtoToEntity(RequestDto requestDto);

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "createdAt", source = "createdAt", dateFormat = "dd-MM-yyyy HH:mm:ss")
    RequestDto entityToDto(Request request);

    List<RequestDto> entityToDto(List<Request> requests);
    List<Request> dtoToEntity(List<RequestDto> requests);
}
