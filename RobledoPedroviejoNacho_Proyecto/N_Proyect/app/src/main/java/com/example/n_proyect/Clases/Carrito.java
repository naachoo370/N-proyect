package com.example.n_proyect.Clases;

public class Carrito {
    private int id;
    private int productoId;
    private String nombreProducto;
    private String imagenProducto;
    private String talla;
    private int cantidad;
    private double precioUnitario;

    public Carrito(int id, int productoId, String nombreProducto,
                   String imagenProducto, String talla, int cantidad,
                   double precioUnitario) {
        this.id = id;
        this.productoId = productoId;
        this.nombreProducto = nombreProducto;
        this.imagenProducto = imagenProducto;
        this.talla = talla;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public int getProductoId() {
        return productoId;
    }

    public int getId() { return id; }
    public String getNombreProducto() { return nombreProducto; }
    public String getImagenProducto() { return imagenProducto; }
    public String getTalla() { return talla; }
    public int getCantidad() { return cantidad; }
    public double getPrecioUnitario() { return precioUnitario; }
}