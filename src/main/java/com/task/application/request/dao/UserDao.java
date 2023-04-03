package com.task.application.request.dao;

import com.task.application.request.entity.User;

import java.util.List;

public interface UserDao {
    User getUserByName(String name);
    List<User> findAllUser();

    User getUserByPartOfName(String name);

    void setUserRole(User user);

    User getUserById(Integer userId);
}
