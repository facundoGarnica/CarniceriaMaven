/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CarpetaClases;

/**
 *
 * @author garca
 */
public class DetalleVenta {

    Double Peso;
    Producto producto;
    Venta venta;
    Double Precio;
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPeso(Double Peso) {
        this.Peso = Peso;
    }

    public void setPrecio(Double Precio) {
        this.Precio = Precio;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Venta getVenta() {
        return venta;
    }

    public void setVenta(Venta venta) {
        this.venta = venta;
    }

    public Double getPrecio() {
        return this.Precio;
    }

    public Double getPeso() {
        return Peso;
    }

}

