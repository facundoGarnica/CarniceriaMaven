/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.Fecha;
import ConexionDB.ConexionDB;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author garca
 */
public class FechaDAO {

    private ConexionDB conexionDB;

    public FechaDAO() {
        this.conexionDB = new ConexionDB();
    }

    // Método para agregar una nueva venta
    public boolean agregarFecha(Fecha fecha) {
        String sql = "INSERT INTO Fecha (fecha) VALUES (?)"; // Insertar solo la fecha

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // Convertir LocalDate a String antes de insertarlo
            stmt.setString(1, fecha.getFecha().toString());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                ResultSet generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    fecha.setId(generatedKeys.getInt(1));  // Obtener el ID generado
                }
                System.out.println("Fecha agregada con éxito. ID: " + fecha.getId());
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar fecha: " + e.getMessage());
        }
        return false;
    }

    public Fecha obtenerFechaPorId(int id) {
        String sql = "SELECT * FROM Fecha WHERE id = ?";
        Fecha fecha = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int idFecha = rs.getInt("id"); // Obtener ID
                String fechaStr = rs.getString("fecha"); // Obtener fecha como String

                // Convertir String a LocalDate
                LocalDate fechaLocalDate = LocalDate.parse(fechaStr);

                // Crear objeto Fecha con ID y LocalDate
                fecha = new Fecha();
                fecha.setId(idFecha);
                fecha.setFecha(fechaLocalDate);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener fecha: " + e.getMessage());
        }

        return fecha; // Debe devolver un objeto Fecha, no "venta"
    }

    // Método para obtener todas las ventas
    public ArrayList<Fecha> obtenerTodasLasFechas() {
        String sql = "SELECT * FROM Fecha"; // Corregido: Ahora selecciona de la tabla correcta
        ArrayList<Fecha> fechas = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                int idFecha = rs.getInt("id"); // Obtener el ID
                String fechaStr = rs.getString("fecha"); // Obtener la fecha como String

                // Convertir String a LocalDate
                LocalDate fechaLocalDate = LocalDate.parse(fechaStr);

                // Crear objeto Fecha y agregarlo a la lista
                Fecha fecha = new Fecha();
                fecha.setId(idFecha);
                fecha.setFecha(fechaLocalDate);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener todas las fechas: " + e.getMessage());
        }

        return fechas;
    }

}
