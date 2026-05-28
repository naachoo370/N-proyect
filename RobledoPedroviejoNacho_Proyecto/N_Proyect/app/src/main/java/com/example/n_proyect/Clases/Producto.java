package com.example.n_proyect.Clases;

public class Producto {
    private int id;
    private String nombre;
    private String descripcion;
    private double precio;
    private String imagenPrincipal;
    private String imagenSecundaria;


    public Producto(int id, String nombre, String descripcion, double precio,
                    String imagenPrincipal, String imagenSecundaria) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
        this.imagenPrincipal = imagenPrincipal;
        this.imagenSecundaria = imagenSecundaria;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public double getPrecio() { return precio; }
    public String getImagenPrincipal() { return imagenPrincipal; }
    public String getImagenSecundaria() { return imagenSecundaria; }

    public void setId(int id) { this.id = id; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public void setPrecio(double precio) { this.precio = precio; }
    public void setImagenPrincipal(String imagenPrincipal) { this.imagenPrincipal = imagenPrincipal; }
    public void setImagenSecundaria(String imagenSecundaria) { this.imagenSecundaria = imagenSecundaria; }

}