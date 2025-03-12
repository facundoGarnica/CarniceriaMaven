/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package carne.ProductoController;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author garca
 */
public class ProductosController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private Pane PanelProductos;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public Pane getPanel(){
        return this.PanelProductos;
    }
    
    
    
}
