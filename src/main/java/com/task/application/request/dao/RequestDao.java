package com.task.application.request.dao;

import com.task.application.request.entity.Request;

import java.util.List;

public interface RequestDao {

    Request addRequest(Request request);

    List<Request> getAllSentRequests(Integer page, String sortBy, String orderBy);

    Request getRequestById(Integer reqId);

    Request updateRequest(Request request);

    List<Request> getAllUserRequest(Integer page, Integer userId, String sortBy, String orderBy);

    List<Request> getAllSentRequestByUser(Integer id, Integer userId, String sortBy, String orderBy);
}
