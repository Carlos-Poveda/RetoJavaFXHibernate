package org.example.retofxhibernate.Pelicula;

import org.example.retofxhibernate.Common.Repository;
import org.hibernate.SessionFactory;

import java.util.List;
import java.util.Optional;

public class PeliculaRepository implements Repository<Pelicula> {

    SessionFactory sessionFactory;

    public PeliculaRepository(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Pelicula save(Pelicula entity) {
        return null;
    }

    @Override
    public Optional<Pelicula> delete(Pelicula entity) {
        return Optional.empty();
    }

    @Override
    public Optional<Pelicula> deleteById(Long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Pelicula> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<Pelicula> findAll() {
        return List.of();
    }

    @Override
    public Long count() {
        return 0L;
    }
}
