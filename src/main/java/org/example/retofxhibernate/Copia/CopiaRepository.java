package org.example.retofxhibernate.Copia;

import org.example.retofxhibernate.Common.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class CopiaRepository implements Repository<Copia> {
    SessionFactory sessionFactory;

    public CopiaRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public List<Copia> findByUserId(Integer userId) {
        try (Session session = sessionFactory.openSession()) {
            Query<Copia> query = session.createQuery(
                    "from Copia c where c.id_usuario = :userId", Copia.class);
            query.setParameter("userId", userId);

            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    @Override
    public Copia save(Copia entity) {
        return null;
    }

    @Override
    public Optional<Copia> delete(Copia entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Copia> deleteById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Copia> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Copia> findAll() {
        return List.of();
    }

    @Override
    public Long count() {
        return 0L;
    }
}
