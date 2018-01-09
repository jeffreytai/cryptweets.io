package com.crypto.utils;

import com.crypto.hibernate.HibernateUtils;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;

public class DbUtils {

    /**
     * Given a list of entities, save them to the database
     * @param entities
     */
    public static void saveEntities(List<?> entities) {
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();
            entities.forEach(entity -> session.save(entity));
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }

        session.close();
    }



    public static Object runSingularResultQuery(String query) {
        Object result = null;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();
            Query q = session.createQuery(query);
            result = q.getSingleResult();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }

        session.close();
        return result;
    }
}
