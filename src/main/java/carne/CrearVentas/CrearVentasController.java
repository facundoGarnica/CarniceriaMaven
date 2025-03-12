/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package carne.CrearVentas;

import CarpetaClases.DetalleVenta;
import CarpetaClases.Producto;
import CarpetaClases.Venta;
import DAO.DetalleVentaDAO;
import DAO.ProductoDAO;
import DAO.VentaDAO;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import static javafx.scene.input.KeyCode.F1;
import static javafx.scene.input.KeyCode.F3;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author garca
 */
public class CrearVentasController implements Initializable {

    private Stage menuStage;
    @FXML
    private TextField CodigoRecibir;

    private Venta venta = new Venta();
    @FXML
    private TableView<Producto> TablaProductos;  // El TableView
    @FXML
    private TableColumn colNombre;  // Columna para el nombre
    @FXML
    private TableColumn colPrecio;  // Columna para el precio
    @FXML
    private TableColumn colPeso;  // Columna para el peso
    @FXML
    private TableColumn colTotal;  // Columna para el total
    @FXML
    private Label MedioDePago;
    @FXML
    private Label TotalAPagar;

    ObservableList<Producto> productos;

    double sumar = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        //inicializar el observableList
        productos = FXCollections.observableArrayList();

        //asociar filas
        this.colNombre.setCellValueFactory(new PropertyValueFactory("Nombre"));
        this.colPrecio.setCellValueFactory(new PropertyValueFactory("Precio"));
        this.colPeso.setCellValueFactory(new PropertyValueFactory("Peso"));
        this.colTotal.setCellValueFactory(new PropertyValueFactory("Total"));

        // Detectar cambios en la escena del label MedioDePago (para F1 y F3)
        MedioDePago.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                newScene.setOnKeyPressed(event -> {
                    switch (event.getCode()) {
                        case F1 -> {
                            MedioDePago.setText("Efectivo");

                        }
                        case F3 -> {
                            MedioDePago.setText("Virtual");
                        }
                    }
                });
            }
        });

    }

    @FXML
    public void AgregarProductos(ActionEvent event) {

    }

    @FXML
    public void nada() {
        System.out.println("Nada");
    }

    @FXML
    public void BorrarTodo() {
        venta.getProductos().clear();
        sumar = 0;
        MedioDePago.setText("-----");
        TotalAPagar.setText("-----");
        TablaProductos.getItems().clear();
    }

    @FXML
    private void VolverAlMenu(ActionEvent event) {
        try {
            if (menuStage == null) {
                // Si no se ha cargado la ventana principal, la creamos
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/MenuPrincipal.fxml"));
                Parent root = loader.load();
                menuStage = new Stage();
                menuStage.setTitle("Menu Principal");
                menuStage.setScene(new Scene(root));
            }
            // Mostramos la ventana principal
            menuStage.show();

            // Cerrar la ventana actual
            Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            currentStage.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Metodo para separar el codigo de barra
    @FXML
    public void SepararCodigo() {
        String codigo = CodigoRecibir.getText();

        if (codigo.length() != 13) {
            System.out.println("El código ingresado no es válido. Debe tener 13 dígitos.");
        } else {
            try {
                // Extraer partes del código
                String tipoProducto = codigo.substring(0, 2);  // Primeros 2 dígitos
                String codigoProducto = codigo.substring(2, 6); // 4 dígitos siguientes
                String pesoImporte = codigo.substring(6, 12);
                int codigoInt = Integer.parseInt(codigoProducto);
                Double pesoImporteFloat = Double.valueOf(pesoImporte); // 5 dígitos siguientes
                String digitoControl = codigo.substring(13);   // Último dígito

                // Convertir peso a formato decimal usando float
                Double peso = pesoImporteFloat / 1000.0f; // Dividir para obtener kg
                // Mostrar el resultado
                System.out.println("Código del producto: " + codigoInt);
                System.out.println("Peso: " + peso + " kg");

                // Llamar a BuscarProducto si lo necesitas
                // BuscarProducto(Integer.parseInt(codigoProducto), peso, tipoProducto);
                System.out.println(pesoImporte);
                BuscarProducto(codigoInt, tipoProducto, peso);
            } catch (NumberFormatException e) {
                System.out.println("Error: El código contiene caracteres no numéricos.");
            }
        }

        // Limpiar siempre el TextField, sin importar si el código era válido o no
        CodigoRecibir.clear();
    }

    //Metodo para buscar producto en la DB
    @FXML
    public void BuscarProducto(int codigo, String tipoProducto, Double peso) {

        ProductoDAO productoDAO = new ProductoDAO();
        // Buscar el producto en la base de datos
        Producto ProductoEncontrado = productoDAO.buscarPorCodigo(codigo);
        ProductoEncontrado.setPeso(peso);

        if (ProductoEncontrado == null) {
            javax.swing.JOptionPane.showMessageDialog(null, "Producto no encontrado con el código: " + codigo,
                    "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            return;
        } else {
            System.out.println("el produto es: " + ProductoEncontrado.getNombre());
        }
        
        venta.getProductos().add(ProductoEncontrado);

        sumar = sumar + ProductoEncontrado.getTotal();
        double redondearSuma = Math.round(sumar / 10.0) * 10;
        String sumaStr = String.valueOf(redondearSuma);
        TotalAPagar.setText(sumaStr);
        
        System.out.println("Peso aaaa: " + ProductoEncontrado.getPeso());
        System.out.println("Dinero aaa: " + ProductoEncontrado.getPrecio());
        MostrarProductos(ProductoEncontrado);
    }

    public void MostrarProductos(Producto Producto) {
        ////////
        //Agregar el objeto producto al observableList productos
        this.productos.add(Producto);
        //Mostrar el observableList en la tableview 
        this.TablaProductos.setItems(productos);
        ////////
    }

    public void GenerarDetalleVenta() {
        DetalleVentaDAO detalleDAO = new DetalleVentaDAO();
        // Iterar por cada producto en la venta
        for (Producto producto : venta.getProductos()) {
            DetalleVenta nuevoDetalle = new DetalleVenta();
            nuevoDetalle.setVenta(venta);               // Asociar la venta
            nuevoDetalle.setProducto(producto);         // Asociar el producto
            nuevoDetalle.setPeso(producto.getPeso());   // Asignar el peso del producto
            nuevoDetalle.setPrecio(producto.getPrecio()); // Asignar el precio del producto

            // Guardar el detalle de la venta en la base de datos
            boolean agregado = detalleDAO.agregarDetalleVenta(nuevoDetalle);

            /*////////
            //Agregar el objeto producto al observableList productos
            this.productos.add(producto);
            //Mostrar el observableList en la tableview 
            this.TablaProductos.setItems(productos);
            ///////*/

            if (agregado) {
                System.out.println("Detalle de venta guardado: Producto ID " + producto.getId()
                        + ", Peso: " + producto.getPeso()
                        + ", Precio: " + producto.getPrecio());
            } else {
                System.out.println("Error al guardar detalle de venta para el producto ID " + producto.getId());
            }
            System.out.println(nuevoDetalle.getId());
        }

    }

    @FXML
    public void generarVenta() {
        venta.setMedioDePago(MedioDePago.getText()); // Establecer el medio de pago
        venta.setTotal(sumar); // Establecer el total de la venta
        venta.setFecha(LocalDate.now()); // Establecer la fecha de la venta

        // Guardar la venta en la base de datos
        VentaDAO ventaDAO = new VentaDAO();
        boolean GuardarVenta = ventaDAO.agregarVenta(venta);

        if (GuardarVenta) {
            GenerarDetalleVenta();
            System.out.println("Venta guardada con éxito");
            System.out.println(venta.toString()); // Mostrar detalles de la venta
        } else {
            System.out.println("Error al guardar la venta");
        }
        BorrarTodo();
    }

    @FXML
    public void mostrarProductos() {
        // Obtener la lista de productos de la venta
        ObservableList<Producto> listaProductos = FXCollections.observableArrayList(venta.getProductos());

        // Establecer los elementos en el TableView
        TablaProductos.setItems(listaProductos);
    }
}
