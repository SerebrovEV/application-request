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
        Transaction tx1 = session.beginTransaction();
        session.persist(request);
        tx1.commit();
        return request;
    }

    @Override
    public List<Request> getAllRequests() {
        return (List<Request>) HibernateUtil.getSessionFactory().openSession().createQuery("From Request ").list();
    }
}
