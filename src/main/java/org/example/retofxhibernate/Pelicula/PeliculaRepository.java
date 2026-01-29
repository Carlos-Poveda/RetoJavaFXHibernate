package org.example.retofxhibernate.Pelicula;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

// Nota: Ya no implementas Repository<Pelicula> si usaba Session,
// o debes cambiar la interfaz Repository para usar EntityManager.

public class PeliculaRepository {
    private EntityManagerFactory emf;

    public PeliculaRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    public Pelicula save(Pelicula pelicula) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // En JPA estándar, 'persist' guarda nuevos.
            // Si el objeto ya tiene ID y quieres actualizar, se usa 'merge'.
            if (pelicula.getId() == null) {
                em.persist(pelicula);
            } else {
                pelicula = em.merge(pelicula);
            }
            em.getTransaction().commit();
            return pelicula;
        } finally {
            em.close();
        }
    }

    public Optional<Pelicula> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            // find funciona igual
            Pelicula pelicula = em.find(Pelicula.class, id.intValue()); // Ojo al int/long
            return Optional.ofNullable(pelicula);
        } finally {
            em.close();
        }
    }

    public List<Pelicula> findAll() {
        // 1. Crear el EntityManager desde la factoría
        EntityManager em = emf.createEntityManager();
        try {
            // 2. Definir la consulta JPQL (Selecciona el objeto p de la clase Pelicula)
            String jpql = "SELECT p FROM Pelicula p";

            // 3. Crear la TypedQuery para asegurar que el resultado sea una lista de Peliculas
            TypedQuery<Pelicula> query = em.createQuery(jpql, Pelicula.class);

            // 4. Ejecutar y retornar la lista
            return query.getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            // Retornamos una lista vacía en caso de error para evitar NullPointerException en el Controller
            return List.of();
        } finally {
            // 5. IMPORTANTE: Cerrar siempre el EntityManager
            em.close();
        }
    }
}