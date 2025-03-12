/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package carne.MenuPrincipal;

import carne.ProductoController.ProductosController;
import carne.VerVentas.VerVentasController;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.animation.TranslateTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author garca
 */
public class MenuPrincipalController implements Initializable {

    @FXML
    private Pane PanelPrincipal;

    @FXML
    private Label LetrasTitulo;

    private boolean isVBoxVisible = false;
    private boolean isVBoxVisible1 = false;
    private boolean isVBoxVisible2 = false;

    @FXML
    private Button probar;

    @FXML
    private Button ButtonClick;

    @FXML
    private VBox vBoxOpciones;  // El VBox que se animará
    @FXML
    private VBox vBoxOpciones1;
    @FXML
    private VBox vBoxOpciones11;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Aquí puedes hacer configuraciones iniciales si es necesario
    }

    @FXML
    public void desplazarHaciaAbajo() {
        // Cerrar vBoxOpciones1 y vBoxOpciones11 si están abiertos
        if (isVBoxVisible1) {
            cerrarVBox(vBoxOpciones1);
            isVBoxVisible1 = false;
        }
        if (isVBoxVisible2) {
            cerrarVBox(vBoxOpciones11);
            isVBoxVisible2 = false;
        }

        TranslateTransition translate = new TranslateTransition(Duration.millis(400), vBoxOpciones);

        if (!isVBoxVisible) {
            vBoxOpciones.setVisible(true);
            translate.setToY(220);
            isVBoxVisible = true;
        } else {
            cerrarVBox(vBoxOpciones);
            isVBoxVisible = false;
        }

        translate.play();
    }

    @FXML
    public void desplazarHaciaAbajo2() {
        // Cerrar vBoxOpciones y vBoxOpciones11 si están abiertos
        if (isVBoxVisible) {
            cerrarVBox(vBoxOpciones);
            isVBoxVisible = false;
        }
        if (isVBoxVisible2) {
            cerrarVBox(vBoxOpciones11);
            isVBoxVisible2 = false;
        }

        TranslateTransition translate = new TranslateTransition(Duration.millis(400), vBoxOpciones1);

        if (!isVBoxVisible1) {
            vBoxOpciones1.setVisible(true);
            translate.setToY(170);
            isVBoxVisible1 = true;
        } else {
            cerrarVBox(vBoxOpciones1);
            isVBoxVisible1 = false;
        }

        translate.play();
    }

    @FXML
    public void desplazarHaciaAbajo3() {
        // Cerrar vBoxOpciones y vBoxOpciones1 si están abiertos
        if (isVBoxVisible) {
            cerrarVBox(vBoxOpciones);
            isVBoxVisible = false;
        }
        if (isVBoxVisible1) {
            cerrarVBox(vBoxOpciones1);
            isVBoxVisible1 = false;
        }

        TranslateTransition translate = new TranslateTransition(Duration.millis(400), vBoxOpciones11);

        if (!isVBoxVisible2) {
            vBoxOpciones11.setVisible(true);
            translate.setToY(260); // Mueve el VBox a la posición visible
            isVBoxVisible2 = true;
        } else {
            cerrarVBox(vBoxOpciones11);
            isVBoxVisible2 = false;
        }

        translate.play();
    }

// Método para cerrar un VBox con animación
    private void cerrarVBox(VBox vbox) {
        TranslateTransition translate = new TranslateTransition(Duration.millis(400), vbox);
        translate.setToY(-72); // Desplazar hacia arriba, fuera de la vista
        translate.setOnFinished(event -> vbox.setVisible(false)); // Ocultar el VBox después de la animación
        translate.play();
    }

    @FXML
    public void abrirCrearVentas(ActionEvent event) {
        try {
            // Cargar el archivo FXML de la nueva ventana
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/crearVentas.fxml"));
            Parent root = loader.load();

            // Crear una nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Crear Ventas"); // Título de la ventana
            stage.setScene(new Scene(root));
            stage.show(); // Mostrar la nueva ventana

            // Cerrar la ventana actual (la ventana de la que se hizo clic)
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close(); // Cerrar la ventana actual

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void invocarNuevaVentana() {
        try {
            PanelPrincipal.getChildren().clear();
            // Cargar el FXML de ventana2
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/VerVentas.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de ventana2 para manipularlo si es necesario
            VerVentasController ventasController = loader.getController();

            // Obtener el Pane de ventana2 que tiene el fx:id="contenedorPrincipal"
            Pane contenedorVentana2 = ventasController.getContenedorPrincipal();

            // Ahora agregar el Pane de ventana2 dentro del Pane de ventana1
            PanelPrincipal.getChildren().add(contenedorVentana2);
            cerrarVBox(vBoxOpciones11);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void invocarproductos() {
        try {
            PanelPrincipal.getChildren().clear();
            // Cargar el FXML de ventana2
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Productos.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de ventana2 para manipularlo si es necesario
            ProductosController productoscontroller = loader.getController();

            // Obtener el Pane de ventana2 que tiene el fx:id="contenedorPrincipal"
            Pane contenedorVentana2 = productoscontroller.getPanel();

            // Ahora agregar el Pane de ventana2 dentro del Pane de ventana1
            PanelPrincipal.getChildren().add(contenedorVentana2);
            cerrarVBox(vBoxOpciones11);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
