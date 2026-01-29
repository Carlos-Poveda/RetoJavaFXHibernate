package org.example.retofxhibernate.Copia;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class CopiaRepository {

    private final EntityManagerFactory emf;

    public CopiaRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Guardar o Actualizar una copia
    public Copia save(Copia copia) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            if (copia.getId() == null) {
                em.persist(copia);
            } else {
                copia = em.merge(copia);
            }
            em.getTransaction().commit();
            return copia;
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return null;
        } finally {
            em.close();
        }
    }

    // Borrar copia por ID
    public Optional<Copia> deleteById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Primero hay que encontrar el objeto para poder borrarlo
            Copia copia = em.find(Copia.class, id.intValue()); // Cast a int si tu @Id es Integer

            if (copia != null) {
                em.remove(copia);
                em.getTransaction().commit();
                return Optional.of(copia);
            } else {
                em.getTransaction().rollback();
                return Optional.empty();
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            return Optional.empty();
        } finally {
            em.close();
        }
    }

    // Método personalizado: Encontrar copias de un usuario específico
    public List<Copia> findByUserId(Integer userId) {
        EntityManager em = emf.createEntityManager();
        try {
            String jpql = "SELECT c FROM Copia c WHERE c.id_usuario = :userId";
            TypedQuery<Copia> query = em.createQuery(jpql, Copia.class);
            query.setParameter("userId", userId);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    // Buscar copia por ID (Auxiliar)
    public Optional<Copia> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            Copia copia = em.find(Copia.class, id.intValue());
            return Optional.ofNullable(copia);
        } finally {
            em.close();
        }
    }
}