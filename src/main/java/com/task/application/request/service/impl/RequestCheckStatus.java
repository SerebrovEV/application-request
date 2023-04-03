package com.task.application.request.service.impl;

import com.task.application.request.dto.Status;
import com.task.application.request.entity.Request;
import org.springframework.stereotype.Component;

@Component
public class RequestCheckStatus {
    public boolean isDraft(Request request){
        return request.getStatus().equalsIgnoreCase(Status.DRAFT.name());
    }

    public boolean isSent(Request request){
        return request.getStatus().equalsIgnoreCase(Status.DRAFT.name());
    }

    public boolean isSent(String status) {
        return status.equalsIgnoreCase(Status.SENT.name());
    }

    public boolean isAccepted(String status) {
        return status.equalsIgnoreCase(Status.ACCEPTED.name());
    }

    public boolean isRejected(String status) {
        return status.equalsIgnoreCase(Status.REJECTED.name());
    }
}
