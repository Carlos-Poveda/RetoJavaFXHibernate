package org.example.retofxhibernate.Common;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class DataProvider {
    private static EntityManagerFactory emf;

    public static EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            emf = Persistence.createEntityManagerFactory("objectdb:db/peliculas.odb");
        }
        return emf;
    }
}
