package com.task.application.request.mapper;

import com.task.application.request.dto.UserDto;
import com.task.application.request.entity.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto entityToDto(User user);

    User dtoToUser(UserDto userDto);

    List<UserDto> entityToDto(List<User> users);
}
