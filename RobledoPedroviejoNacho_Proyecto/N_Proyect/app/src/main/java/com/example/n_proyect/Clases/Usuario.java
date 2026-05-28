package com.example.n_proyect.Clases;

public class Usuario {
    private int id;
    private String correo;
    private String rol;

    public Usuario(int id, String correo, String rol) {
        this.id = id;
        this.correo = correo;
        this.rol = rol;
    }

    public int getId() {
        return id;
    }

    public String getCorreo() {
        return correo;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}