package com.task.application.request.dao.impl;

import com.task.application.request.dao.UserDao;
import com.task.application.request.entity.User;
import com.task.application.request.util.HibernateUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    public User getByName(String findName) {
        String searchCondition = "'" + findName + "'";
        return (User) HibernateUtil.getSessionFactory().openSession().createQuery("FROM User WHERE name like " + searchCondition).getSingleResult();
    }
    public List<User> findAll() throws HibernateException {
        return (List<User>) HibernateUtil.getSessionFactory().openSession().createQuery("From User ").list();
    }

    @Override
    public User findAllByName(String name) {
        String searchCondition = "'%" + name + "%'";
        return (User) HibernateUtil.getSessionFactory().openSession().createQuery("From User WHERE name ILIKE " + searchCondition).getSingleResult();
    }
}
