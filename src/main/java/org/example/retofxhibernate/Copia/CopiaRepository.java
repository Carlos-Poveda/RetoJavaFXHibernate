package org.example.retofxhibernate.Copia;

import org.example.retofxhibernate.Common.Repository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
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
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.persist(entity);
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
    public Optional<Copia> delete(Copia entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Copia> deleteById(Long id) {
        Transaction transaction = null;
        Optional<Copia> deletedCopy = Optional.empty();

        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();

            Copia copia = session.find(Copia.class, id.intValue());

            if (copia != null) {
                deletedCopy = Optional.of(copia);
                session.remove(copia);
                transaction.commit();
                return deletedCopy;
            } else {
                transaction.rollback();
                return Optional.empty();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return Optional.empty();
        }
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
