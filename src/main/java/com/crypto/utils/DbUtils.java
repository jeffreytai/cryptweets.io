package com.crypto.utils;

import com.crypto.orm.HibernateUtils;
import org.hibernate.Session;

import javax.persistence.Query;
import java.util.List;
import java.util.Map;

public class DbUtils {

    /**
     * Given a list of entities, save them to the database
     * @param entities
     */
    public static void saveEntities(List<? extends Object> entities) {
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

    public static List<? extends Object> runMultipleResultQuery(String query) {
        List<? extends Object> result = null;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();

            Query q = session.createQuery(query);

            result = q.getResultList();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }

        session.close();
        return result;
    }

    public static List<? extends Object> runMultipleResultQuery(String query, Map<Object, Object> bindedParameters) {
        List<? extends Object> result = null;
        Session session = HibernateUtils.getSessionFactory().getCurrentSession();

        try {
            session.getTransaction().begin();

            Query bindedQuery = session.createQuery(query);
            for (Map.Entry<Object, Object> entry : bindedParameters.entrySet()) {
                bindedQuery.setParameter(entry.getKey().toString(), entry.getValue());
            }

            result = bindedQuery.getResultList();
            session.getTransaction().commit();
        } catch (Exception ex) {
            session.getTransaction().rollback();
            ex.printStackTrace();
        }

        session.close();
        return result;
    }
}
