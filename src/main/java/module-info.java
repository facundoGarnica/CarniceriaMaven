module carne.carniceriamaven {
    requires javafx.controls;
    requires javafx.fxml;  // Corregido de javafx.fxmlEmpty a javafx.fxml
    requires java.sql;
    requires java.base;
    requires java.desktop;  // Necesario para usar javax.swing

    exports carne.carniceriamaven;  // Exporta tu paquete principal si es necesario
    opens carne.MenuPrincipal to javafx.fxml;
    opens carne.CrearVentas to javafx.fxml;
    opens carne.ProductoController to javafx.fxml;
    opens carne.VerVentas to javafx.fxml;
    opens CarpetaClases to javafx.base;

}
