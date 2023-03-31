package com.task.application.request.utils;

import com.task.application.request.entity.RequestEntity;
import com.task.application.request.entity.UserEntity;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

public class HibernateSessionFactoryUtil {
    private static SessionFactory sessionFactory;


    private HibernateSessionFactoryUtil() {

    }

    public static SessionFactory getSessionFactory() {
            if (sessionFactory == null) {
                try {
                    Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
                    configuration.addAnnotatedClass(RequestEntity.class);
                    configuration.addAnnotatedClass(UserEntity.class);
                    StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder().applySettings(configuration.getProperties());
                    sessionFactory = configuration.buildSessionFactory(builder.build());

                } catch (Exception e) {
                    System.out.println("Исключение!" + e);
                }
            }
            return sessionFactory;
    }
}

