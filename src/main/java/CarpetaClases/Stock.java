/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CarpetaClases;

import java.time.LocalDate;

/**
 *
 * @author garca
 */
public class Stock {

    private int CantidadMinima;
    private Producto producto;
    private int Cantidad;
    private LocalDate FechaActualizacion;
    private int id;

    public int getCantidadMinima() {
        return CantidadMinima;
    }

    public void setCantidadMinima(int CantidadMinima) {
        this.CantidadMinima = CantidadMinima;
    }

    public int getCantidad() {
        return Cantidad;
    }

    public void setCantidad(int Cantidad) {
        this.Cantidad = Cantidad;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public LocalDate getFechaActualizacion() {
        return FechaActualizacion;
    }

    public void setFechaActualizacion(LocalDate FechaActualizacion) {
        this.FechaActualizacion = FechaActualizacion;
    }

}
