package org.example.retofxhibernate.Usuario;

import java.io.Serializable;

public class Usuario implements Serializable {
    private Integer id;
    private String nombre_usuario;
    private String contraseña;

    public Usuario(Integer id, String nombre_usuario, String contraseña) {
        this.id = id;
        this.nombre_usuario = nombre_usuario;
        this.contraseña = contraseña;
    }

    public Usuario() {
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nombre_usuario='" + nombre_usuario + '\'' +
                ", contraseña='" + contraseña + '\'' +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre_usuario() {
        return nombre_usuario;
    }

    public void setNombre_usuario(String nombre_usuario) {
        this.nombre_usuario = nombre_usuario;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }
}
