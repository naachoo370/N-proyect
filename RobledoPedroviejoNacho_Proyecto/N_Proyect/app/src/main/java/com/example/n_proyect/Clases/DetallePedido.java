package com.example.n_proyect.Clases;

public class DetallePedido {
    private int productoId;
    private String talla;
    private int cantidad;

    public DetallePedido(int productoId, String talla, int cantidad) {
        this.productoId = productoId;
        this.talla = talla;
        this.cantidad = cantidad;
    }

    public int getProductoId() { return productoId; }
    public String getTalla() { return talla; }
    public int getCantidad() { return cantidad; }
}