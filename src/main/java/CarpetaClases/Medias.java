/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package CarpetaClases;

/**
 *
 * @author garca
 */
public class Medias{
    private Double PesoBalanza;
    private Double PesoBoleta;
    private Double PesoFinal;
    private BoletaCarne Boleta;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    
    public Double getPesoBalanza() {
        return PesoBalanza;
    }

    public void setPesoBalanza(Double PesoBalanza) {
        this.PesoBalanza = PesoBalanza;
    }

    public Double getPesoBoleta() {
        return PesoBoleta;
    }

    public void setPesoBoleta(Double PesoBoleta) {
        this.PesoBoleta = PesoBoleta;
    }

    public Double getPesoFinal() {
        return PesoFinal;
    }

    public void setPesoFinal(Double PesoFinal) {
        this.PesoFinal = PesoFinal;
    }

    public BoletaCarne getBoletaCarne() {
        return Boleta;
    }

    public void setBoletaCarne(BoletaCarne boleta) {
        this.Boleta = boleta;
    }
    
    
}
