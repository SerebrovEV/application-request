package com.task.application.request.service.impl;

import com.task.application.request.dto.Status;
import com.task.application.request.entity.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RequestCheckStatusTest {

    RequestCheckStatus out = new RequestCheckStatus();
    Request DRAFT;
    Request SENT;
    Request ACCEPTED;
    Request REJECTED;



    @BeforeEach
    public void setOut() {
        DRAFT = new Request();
        DRAFT.setStatus(Status.DRAFT.name());

        SENT = new Request();
        SENT.setStatus(Status.SENT.name());

        ACCEPTED = new Request();
        ACCEPTED.setStatus(Status.ACCEPTED.name());

        REJECTED = new Request();
        REJECTED.setStatus(Status.REJECTED.name());


    }

    @Test
    void isDraft() {
        assertTrue(out.isDraft(DRAFT));
        assertFalse(out.isDraft(SENT));
        assertFalse(out.isDraft(ACCEPTED));
        assertFalse(out.isDraft(REJECTED));
    }

    @Test
    void isSent() {
        assertFalse(out.isSent(DRAFT));
        assertTrue(out.isSent(SENT));
        assertFalse(out.isSent(ACCEPTED));
        assertFalse(out.isSent(REJECTED));
    }

    @Test
    void testIsSent() {
        assertFalse(out.isSent("DRAFT"));
        assertTrue(out.isSent("SENT"));
        assertFalse(out.isSent("ACCEPTED"));
        assertFalse(out.isSent("REJECTED"));
    }

    @Test
    void isAccepted() {
        assertFalse(out.isAccepted(DRAFT.getStatus()));
        assertFalse(out.isAccepted(SENT.getStatus()));
        assertTrue(out.isAccepted(ACCEPTED.getStatus()));
        assertFalse(out.isAccepted(REJECTED.getStatus()));
    }

    @Test
    void isRejected() {
        assertFalse(out.isRejected(DRAFT.getStatus()));
        assertFalse(out.isRejected(SENT.getStatus()));
        assertFalse(out.isRejected(ACCEPTED.getStatus()));
        assertTrue(out.isRejected(REJECTED.getStatus()));
    }
}