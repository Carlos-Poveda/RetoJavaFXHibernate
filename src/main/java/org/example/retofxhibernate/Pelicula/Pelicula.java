package org.example.retofxhibernate.Pelicula;

import java.io.Serializable;

public class    Pelicula implements Serializable {
    private Integer id;
    private String titulo;
    private String genero;
    private Integer año;
    private String descripcion;
    private String director;

    public Pelicula(Integer id, String titulo, String genero, Integer año, String descripcion, String director) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.año = año;
        this.descripcion = descripcion;
        this.director = director;
    }

    public Pelicula() {
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", genero='" + genero + '\'' +
                ", año=" + año +
                ", descripcion='" + descripcion + '\'' +
                ", director='" + director + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public Integer getAño() {
        return año;
    }

    public void setAño(Integer año) {
        this.año = año;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }
}
