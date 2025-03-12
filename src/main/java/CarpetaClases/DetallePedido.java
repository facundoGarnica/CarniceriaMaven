/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CarpetaClases;

/**
 *
 * @author garca
 */
public class DetallePedido {

    private int id;                  
    private Pedido pedido;        
    private Producto producto;      
    private String UnidadMedida;
    private double cantidad;

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public String getUnidadMedida() {
        return UnidadMedida;
    }

    public void setUnidadmedida(String unidad_medida) {
        this.UnidadMedida = unidad_medida;
    }
   
}

