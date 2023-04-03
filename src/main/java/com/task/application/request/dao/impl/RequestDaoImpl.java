package com.task.application.request.dao.impl;

import com.task.application.request.dao.RequestDao;
import com.task.application.request.entity.Request;
import com.task.application.request.util.HibernateUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class RequestDaoImpl implements RequestDao {


    @Override
    public Request addRequest(Request request) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.persist(request);
        tx.commit();
        session.close();
        return request;
    }

    @Override
    public Request getRequestById(Integer reqId) {
        return HibernateUtil.getSessionFactory().openSession().get(Request.class, reqId);
    }

    @Override
    public Request updateRequest(Request request) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        Request result = session.merge(request);
        tx.commit();
        session.close();
        return result;
    }

    @Override
    public List<Request> getAllUserRequest(Integer userId) {
        return (List<Request>) HibernateUtil.getSessionFactory()
                .openSession()
                .createQuery("FROM Request WHERE user = " + userId)
                .list();
    }


    @Override
    public List<Request> getAllActiveRequests() {
        return (List<Request>) HibernateUtil.getSessionFactory().openSession().createQuery("From Request WHERE status like 'SENT'").list();
    }


}
