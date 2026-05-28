package com.example.n_proyect.Clases;

import java.io.Serializable;

public class Pedido implements Serializable {
    private int id;
    private int usuarioId;
    private String fechaPedido;
    private String estado;
    private String direccionEnvio;
    private String metodoPago;
    private double total;
    private String correoCliente;

    public Pedido(int id, int usuarioId, String fechaPedido, String estado,
                  String direccionEnvio, String metodoPago, double total,
                  String correoCliente) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.fechaPedido = fechaPedido;
        this.estado = estado;
        this.direccionEnvio = direccionEnvio;
        this.metodoPago = metodoPago;
        this.total = total;
        this.correoCliente = correoCliente;
    }


    public int getId() { return id; }
    public int getUsuarioId() { return usuarioId; }
    public String getFechaPedido() { return fechaPedido; }
    public String getEstado() { return estado; }
    public String getDireccionEnvio() { return direccionEnvio; }
    public String getMetodoPago() { return metodoPago; }
    public double getTotal() { return total; }
    public String getCorreoCliente() { return correoCliente; }

    public void setId(int id) { this.id = id; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setFechaPedido(String fechaPedido) { this.fechaPedido = fechaPedido; }
    public void setEstado(String estado) { this.estado = estado; }
    public void setDireccionEnvio(String direccionEnvio) { this.direccionEnvio = direccionEnvio; }
    public void setMetodoPago(String metodoPago) { this.metodoPago = metodoPago; }
    public void setTotal(double total) { this.total = total; }
    public void setCorreoCliente(String correoCliente) { this.correoCliente = correoCliente; }
}
