package com.task.application.request.dao.impl;

import com.task.application.request.dao.RequestDao;
import com.task.application.request.entity.Request;
import com.task.application.request.util.HibernateUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
    public Optional<Request> getRequestById(Integer reqId) {
        return Optional.ofNullable(HibernateUtil.getSessionFactory().openSession().get(Request.class, reqId));
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
    public List<Request> getAllUserRequest(Integer page, Integer userId, String sortBy, String orderBy) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Request WHERE user = " + userId;
        hql = isOrderBy(hql, sortBy, orderBy);
        Query<Request> query = session.createQuery(hql);
        query.setFirstResult(5 * (page - 1));
        query.setMaxResults(5);
        return query.list();
    }

    @Override
    public List<Request> getAllSentRequestByUser(Integer userId, Integer page, String sortBy, String orderBy) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "FROM Request WHERE status like 'SENT' AND user = " + userId;
        hql = isOrderBy(hql, sortBy, orderBy);
        Query<Request> query = session.createQuery(hql);
        query.setFirstResult(5 * (page - 1));
        query.setMaxResults(5);
        return query.list();
    }


    @Override
    public List<Request> getAllSentRequests(Integer page, String sortBy, String orderBy) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "From Request WHERE status like 'SENT'";
        hql = isOrderBy(hql, sortBy, orderBy);
        Query<Request> query = session.createQuery(hql);
        query.setFirstResult(5 * (page - 1));
        query.setMaxResults(5);
        return query.list();
    }

    private String isOrderBy(String hql, String sortBy, String orderBy) {
        if (sortBy.equals("date")) {
            hql = hql + " ORDER BY createdAt";
        }
        if (sortBy.equals("date") && (orderBy.equals("desc") || orderBy.equals("asc"))) {
            hql = hql + " " + orderBy;
        }
        return hql;
    }

}
