package org.example.retofxhibernate.Usuario;

import org.example.retofxhibernate.Common.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class UsuarioRepository implements Repository<Usuario> {
    SessionFactory sessionFactory;

    public UsuarioRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Usuario save(Usuario entity) {
        return null;
    }

    @Override
    public Optional<Usuario> delete(Usuario entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> deleteById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Usuario> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Usuario> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Usuario> query = session.createQuery("from Usuario", Usuario.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Long count() {
        return 0L;
    }
}
