package com.example.n_proyect.Clases;


public class StockExistencias {
    private String talla;
    private int cantidad;
    private boolean agotado;

    public StockExistencias(String talla, int cantidad) {
        this.talla = talla;
        this.cantidad = cantidad;
        this.agotado = cantidad <= 0;
    }

    public boolean isAgotado() {
        return agotado;
    }
    public String getTalla() { return talla; }
    public int getCantidad() { return cantidad; }
}