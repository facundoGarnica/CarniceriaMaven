/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ConexionDB;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionDB {

    private static final String URL = "jdbc:sqlite:" + getDatabasePath();
    private Connection connection;

    // Método para obtener la ruta correcta de la base de datos
    private static String getDatabasePath() {
        // Obtiene el directorio del proyecto
        String projectDir = System.getProperty("user.dir");

        // Define la ruta de la base de datos dentro del proyecto
        File dbFile = new File(projectDir, "src/main/resources/DB/DBCarneRemake.db.db");

        // Verifica si la base de datos ya existe
        if (!dbFile.exists()) {
            System.out.println("Base de datos no encontrada en: " + dbFile.getAbsolutePath());
            
            // Intenta cargarla desde los recursos del proyecto
            URL dbUrl = ConexionDB.class.getClassLoader().getResource("DBCarneRemake.db");

            if (dbUrl != null) {
                System.out.println("Intentando copiar la base de datos desde los recursos...");

                try (InputStream inputStream = dbUrl.openStream();
                     FileOutputStream outputStream = new FileOutputStream(dbFile)) {

                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, length);
                    }

                    System.out.println("Base de datos copiada a: " + dbFile.getAbsolutePath());
                } catch (IOException e) {
                    System.out.println("Error al extraer la base de datos: " + e.getMessage());
                    throw new RuntimeException("No se pudo copiar la base de datos.");
                }
            } else {
                throw new RuntimeException("El archivo de base de datos no se encuentra en los recursos.");
            }
        }

        // Retorna la ruta absoluta de la base de datos
        return dbFile.getAbsolutePath();
    }

    // Método para conectar a la base de datos
    public Connection conectar() {
        try {
            connection = DriverManager.getConnection(URL);
            connection.setAutoCommit(false);
            System.out.println("Conexión establecida con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos: " + e.getMessage());
        }
        return connection;
    }

    // Método para cerrar la conexión
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión cerrada con éxito.");
            }
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}