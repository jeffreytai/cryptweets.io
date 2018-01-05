package com.crypto.builder;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import java.util.Properties;

public class SessionFactoryBuilder {

    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        Properties dbConnectionProperties = new Properties();
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            dbConnectionProperties.load(ClassLoader.getSystemClassLoader().getResourceAsStream("hibernate.properties"));
            return new Configuration().mergeProperties(dbConnectionProperties).configure().buildSessionFactory();
        } catch (Throwable ex) {
            System.err.println("SessionFactory creation failed: " + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }

    public static void shutdown() {
        // Close caches and connection pools
        getSessionFactory().close();
    }
}
