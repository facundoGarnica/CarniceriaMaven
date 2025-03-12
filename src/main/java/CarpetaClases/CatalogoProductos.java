/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CarpetaClases;

import java.util.ArrayList;

/**
 *
 * @author garca
 */
public class CatalogoProductos {

    private int id;
    private String tipo;
    private ArrayList<Producto> productos;

    public CatalogoProductos() {
        productos = new ArrayList();
    }

    public ArrayList<Producto> getProductos() {
        return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "CatalogoProducto{id=" + id + ", tipo='" + tipo + "'}";
    }
    
}
