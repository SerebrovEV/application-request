package com.task.application.request.service;

import com.task.application.request.dto.UserDto;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface UserService {
    /**
     * Запрос списка всех пользователей
     * @param authentication - аутентификация пользователя;
     * @return Список пользователей
     */
    List<UserDto> getAllUser(Authentication authentication);


    /**
     * Поиск пользователя по имени
     * @param name - имя пользователя;
     * @param authentication - аутентификация пользователя;
     * @return найденный пользователь
     */
    UserDto getUserByName(String name, Authentication authentication);

    /**
     * Установка роли "Оператор" пользователю
     * @param userId - id пользователя;
     * @param role - устанавливаемая роль пользователя;
     * @param authentication - аутентификация пользователя
     */
    void setUserRole(Integer userId, String role, Authentication authentication);
}
