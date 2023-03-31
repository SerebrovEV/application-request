package com.task.application.request.dao;

import com.task.application.request.entity.RequestEntity;


public interface RequestDao {

    RequestEntity addRequest(RequestEntity requestEntity);
    RequestEntity getRequest (Long reqId);
    RequestEntity updateRequest(Long reqId, RequestEntity requestEntity);

}
