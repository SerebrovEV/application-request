package com.task.application.request.dao.impl;

import com.task.application.request.dao.RequestDao;
import com.task.application.request.entity.RequestEntity;
import com.task.application.request.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;


@Repository
public class RequestDaoImpl implements RequestDao {
    @Override
    public RequestEntity addRequest(RequestEntity requestEntity) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.persist(requestEntity);
        tx1.commit();
        RequestEntity saveRequest = session.find(RequestEntity.class, requestEntity);
        session.close();
        return saveRequest;
    }

    @Override
    public RequestEntity getRequest(Long reqId) {
        return HibernateSessionFactoryUtil.getSessionFactory().openSession().get(RequestEntity.class, reqId);
    }

    @Override
    public RequestEntity updateRequest(Long reqId, RequestEntity requestEntity) {
        return null;
    }
}
