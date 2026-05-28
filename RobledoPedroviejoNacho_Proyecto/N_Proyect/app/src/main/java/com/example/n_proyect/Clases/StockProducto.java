package com.example.n_proyect.Clases;

import java.util.List;

public class StockProducto {
    private final int id;
    private final String nombre;
    private final String descripcion;
    private final List<StockExistencias> stockItems;

    public StockProducto(int id, String nombre, String descripcion, List<StockExistencias> stockItems) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.stockItems = stockItems;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public List<StockExistencias> getStockItems() { return stockItems; }
}