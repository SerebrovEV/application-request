package com.task.application.request.dao.impl;

import com.task.application.request.dao.UserDao;
import com.task.application.request.entity.User;
import com.task.application.request.util.HibernateUtil;
import lombok.RequiredArgsConstructor;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    public Optional<User> getUserByName(String name) {
        String searchCondition = "'" + name + "'";
        return (Optional<User>) HibernateUtil.getSessionFactory().openSession().createQuery("FROM User WHERE name like " + searchCondition).stream().findFirst();
    }

    public List<User> findAllUser() throws HibernateException {
        return (List<User>) HibernateUtil.getSessionFactory().openSession().createQuery("From User ").list();
    }

    @Override
    public Optional<User> getUserByPartOfName(String name) {
        String searchCondition = "'%" + name + "%'";
        return HibernateUtil.getSessionFactory().openSession().createQuery("From User WHERE name ILIKE " + searchCondition).stream().findFirst();
    }

    @Override
    public void setUserRole(User user) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();
        session.merge(user);
        tx.commit();
        session.close();
    }

    @Override
    public Optional<User> getUserById(Integer userId) {
        return Optional.ofNullable(HibernateUtil.getSessionFactory().openSession().get(User.class, userId));
    }


}
