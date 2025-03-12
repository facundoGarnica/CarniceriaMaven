/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CarpetaClases;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author garca
 */
public class Pedido {

    private int id;
    private ArrayList<DetallePedido> Detalle;
    private Cliente Cliente;
    private LocalDate FechaPedido;
    private String retirado;
    private double Senia;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<DetallePedido> getDetalle() {
        return Detalle;
    }

    public void setDetalle(DetallePedido Detalle) {
        this.Detalle.add(Detalle);
    }

    public Cliente getCliente() {
        return Cliente;
    }

    public void setCliente(Cliente Cliente) {
        this.Cliente = Cliente;
    }

    public LocalDate getFechaPedido() {
        return FechaPedido;
    }

    public void setFechaPedido(LocalDate FechaPedido) {
        this.FechaPedido = FechaPedido;
    }

    public String getRetiro() {
        return retirado;
    }

    public void setRetiro(String retiro) {
        this.retirado = retiro;
    }

    public double getSenia() {
        return Senia;
    }

    public void setSenia(Double Senia) {
        this.Senia = Senia;
    }

   
}
