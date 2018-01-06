package com.crypto.utils;

import com.crypto.builder.SessionFactoryBuilder;
import org.hibernate.Session;

import java.util.List;

public class DbUtils {

    /**
     * Given a list of entities, save them to the database
     * @param entities
     */
    public static void saveEntities(List<?> entities) {
        Session session = SessionFactoryBuilder.getSessionFactory().openSession();

        session.beginTransaction();
        entities.forEach(entity -> session.save(entity));
        session.getTransaction().commit();

        SessionFactoryBuilder.shutdown();
    }
}
