package org.example.retofxhibernate.Usuario;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

public class UsuarioRepository {

    private final EntityManagerFactory emf;

    public UsuarioRepository(EntityManagerFactory emf) {
        this.emf = emf;
    }

    // Guardar o Actualizar un usuario
    public Usuario save(Usuario usuario) {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            // Si el ID es nulo, es nuevo -> persist
            // Si tiene ID, probablemente existe -> merge
            if (usuario.getId() == null) {
                em.persist(usuario);
            } else {
                usuario = em.merge(usuario);
            }
            em.getTransaction().commit();
            return usuario;
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

    // Buscar todos los usuarios (Usado en el Login)
    public List<Usuario> findAll() {
        EntityManager em = emf.createEntityManager();
        try {
            // JPQL es idéntico a HQL en este caso
            TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u", Usuario.class);
            return query.getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        } finally {
            em.close();
        }
    }

    // Buscar por ID
    public Optional<Usuario> findById(Long id) {
        EntityManager em = emf.createEntityManager();
        try {
            // Asumo que tu ID en la clase Usuario es Integer, por eso el cast.
            // Si es Long, quita el .intValue()
            Usuario usuario = em.find(Usuario.class, id.intValue());
            return Optional.ofNullable(usuario);
        } finally {
            em.close();
        }
    }
}