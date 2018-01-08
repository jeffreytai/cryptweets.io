package com.crypto.builder;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class PersistenceManager {

    private static final EntityManagerFactory emFactory = buildPersistenceManager();

    private static EntityManagerFactory buildPersistenceManager() {
        EntityManagerFactory entityManagerFactory = null;
        Properties props = new Properties();
        Map<Object, Object> propsMap = new HashMap<>();

        try {
            props.load(PersistenceManager.class.getClassLoader().getResourceAsStream("application.properties"));
            for (Map.Entry<Object, Object> entry : props.entrySet()) {
                propsMap.put(entry.getKey(), entry.getValue());
            }

            entityManagerFactory = Persistence.createEntityManagerFactory("cryptweets-manager", propsMap);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return entityManagerFactory;
    }

    public static EntityManager getEntityManager() {
        return emFactory.createEntityManager();
    }

    public static void shutdown() {
        // Close caches and connection pools
        emFactory.close();
    }
}
