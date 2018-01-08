package com.crypto.utils;

import com.crypto.builder.PersistenceManager;
import org.hibernate.Session;

import javax.persistence.EntityManager;
import java.util.List;

public class DbUtils {

    /**
     * Given a list of entities, save them to the database
     * @param entities
     */
    public static void saveEntities(List<?> entities) {
        EntityManager em = PersistenceManager.getEntityManager();

        try {
            em.getTransaction().begin();
            entities.forEach(entity -> em.persist(entity));
            em.getTransaction().commit();
        } catch (Exception ex) {
            em.getTransaction().rollback();
        }

        em.close();
        PersistenceManager.shutdown();
    }
}
