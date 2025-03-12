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
public class BoletaCarne {

    private ArrayList<Medias> Medias;
    private int Total;
    private String Proveedor;
    private int id;

    public void setMediasArray(ArrayList<Medias> Medias) {
        this.Medias = Medias;
    }

    
    public BoletaCarne() {
        Medias = new ArrayList();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<Medias> getMedias() {
        return Medias;
    }

    public void setMedias(Medias medias) {
        Medias.add(medias);
    }

    public int getTotal() {
        return Total;
    }

    public void setTotal(int Total) {
        this.Total = Total;
    }

    public String getProveedor() {
        return Proveedor;
    }

    public void setProveedor(String Proveedor) {
        this.Proveedor = Proveedor;
    }

}
