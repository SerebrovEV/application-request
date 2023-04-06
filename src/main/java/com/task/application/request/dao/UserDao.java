package com.task.application.request.dao;

import com.task.application.request.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    Optional <User> getUserByName(String name);
    List<User> findAllUser();

    Optional<User> getUserByPartOfName(String name);

    void setUserRole(User user);

    Optional<User> getUserById(Integer userId);
}
