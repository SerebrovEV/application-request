package com.task.application.request.dao;

import com.task.application.request.entity.User;

import java.util.List;

public interface UserDao {
    User getByName(String name);
    List<User> findAll();
}
