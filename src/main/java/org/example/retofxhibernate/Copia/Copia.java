package org.example.retofxhibernate.Copia;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "copias")
public class Copia implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "id_pelicula")
    private Integer id_pelicula;

    @Column(name = "id_usuario")
    private Integer id_usuario;

    @Column(name = "estado")
    private String estado;

    @Column(name = "soporte")
    private String soporte;

    public Copia() {}

    public Copia(Integer id, Integer id_pelicula, Integer id_usuario, String estado, String soporte) {
        this.id = id;
        this.id_pelicula = id_pelicula;
        this.id_usuario = id_usuario;
        this.estado = estado;
        this.soporte = soporte;
    }

    @Override
    public String toString() {
        return "Copia{" +
                "id=" + id +
                ", id_pelicula=" + id_pelicula +
                ", id_usuario=" + id_usuario +
                ", estado='" + estado + '\'' +
                ", soporte='" + soporte + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getId_pelicula() {
        return id_pelicula;
    }

    public void setId_pelicula(Integer id_pelicula) {
        this.id_pelicula = id_pelicula;
    }

    public Integer getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(Integer id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getSoporte() {
        return soporte;
    }

    public void setSoporte(String soporte) {
        this.soporte = soporte;
    }
}