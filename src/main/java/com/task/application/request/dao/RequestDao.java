package com.task.application.request.dao;

import com.task.application.request.entity.Request;

import java.util.List;

public interface RequestDao {

    Request addRequest(Request request);

    List<Request> getAllActiveRequests();

    Request getRequestById(Integer reqId);

    Request updateRequest(Request request);

    List<Request> getAllUserRequest(Integer userId);
}
