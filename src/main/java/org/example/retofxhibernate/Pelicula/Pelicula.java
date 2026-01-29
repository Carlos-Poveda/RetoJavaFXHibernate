package org.example.retofxhibernate.Pelicula;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "peliculas")
public class Pelicula implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "titulo")
    private String titulo;

    @Column(name = "genero")
    private String genero;

    @Column(name = "anio_lanzamiento")
    private Integer anio;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "director")
    private String director;

    public Pelicula(Integer id, String titulo, String genero, Integer anio, String descripcion, String director) {
        this.id = id;
        this.titulo = titulo;
        this.genero = genero;
        this.anio = anio;
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
                ", anio=" + anio +
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

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
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