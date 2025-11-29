package org.example.retofxhibernate.Pelicula;

import org.example.retofxhibernate.Common.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;
import java.util.Optional;

public class PeliculaRepository implements Repository<Pelicula> {

    private final SessionFactory sessionFactory;

    public PeliculaRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Pelicula save(Pelicula entity) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity); // Guardamos la entidad
            transaction.commit();
            return entity;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<Pelicula> findAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<Pelicula> query = session.createQuery("from Pelicula", Pelicula.class);
            return query.list();
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }

    // ... (El resto de métodos opcionales se quedan vacíos o return null/empty) ...
    @Override
    public Optional<Pelicula> delete(Pelicula entity) { return Optional.empty(); }
    @Override
    public Optional<Pelicula> deleteById(Long id) { return Optional.empty(); }

    @Override
    public Optional<Pelicula> findById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            Pelicula pelicula = session.find(Pelicula.class, id.intValue());
            return Optional.ofNullable(pelicula);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    @Override
    public Long count() { return 0L; }
}