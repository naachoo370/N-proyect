package com.example.n_proyect.Clases;

public class ProductoVideo {
    private int productoId;
    private String nombreVideo;

    public ProductoVideo(int productoId, String nombreVideo) {
        this.productoId = productoId;
        this.nombreVideo = nombreVideo;
    }

    public int getProductoId() { return productoId; }
    public String getNombreVideo() { return nombreVideo; }
}