package com.task.application.request.mapper;

import com.task.application.request.dto.RequestDto;
import com.task.application.request.entity.RequestEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(target = "createAt", source = "createAt", dateFormat = "dd-MM-yyyy HH:mm:ss")
    RequestDto entityToDto(RequestEntity requestEntity);

    @Mapping(target = "user.id", source = "userId")
    @Mapping(target = "createAt", source = "createAt", dateFormat = "dd-MM-yyyy HH:mm:ss")
    RequestEntity dtoToEntity(RequestDto requestDto);
}
