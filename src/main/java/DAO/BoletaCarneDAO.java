/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

/**
 *
 * @author garca
 */
import CarpetaClases.BoletaCarne;
import ConexionDB.ConexionDB;

import java.sql.*;
import java.util.ArrayList;

public class BoletaCarneDAO {

    private ConexionDB conexionDB;

    public BoletaCarneDAO() {
        this.conexionDB = new ConexionDB();
    }

    // Método para agregar una nueva boleta de carne
    public boolean agregarBoletaCarne(BoletaCarne boleta) {
        String sql = "INSERT INTO BoletaCarne (Total, Proveedor) VALUES (?, ?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar manualmente las transacciones
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, boleta.getTotal());
                stmt.setString(2, boleta.getProveedor());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    conn.commit();  // Confirmar la transacción
                    System.out.println("Boleta agregada con éxito.");
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre algún error
                System.out.println("Error al agregar boleta: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener todas las boletas de carne
    public ArrayList<BoletaCarne> obtenerTodasBoletas() {
        ArrayList<BoletaCarne> boletas = new ArrayList<>();
        String sql = "SELECT * FROM BoletaCarne";
        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                BoletaCarne boleta = new BoletaCarne();
                boleta.setId(rs.getInt("id"));
                boleta.setTotal(rs.getInt("Total"));
                boleta.setProveedor(rs.getString("Proveedor"));
                boletas.add(boleta);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las boletas: " + e.getMessage());
        }
        return boletas;
    }

    // Método para eliminar una boleta por su ID
    public boolean eliminarBoleta(int id) {
        String sql = "DELETE FROM BoletaCarne WHERE id = ?";
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Boleta eliminada con éxito.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar la boleta: " + e.getMessage());
        }
        return false;
    }

    // Método para editar una boleta existente
    public boolean editarBoleta(BoletaCarne boleta) {
        String sql = "UPDATE BoletaCarne SET Total = ?, Proveedor = ? WHERE id = ?";
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, boleta.getTotal());
            stmt.setString(2, boleta.getProveedor());
            stmt.setInt(3, boleta.getId());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                System.out.println("Boleta actualizada con éxito.");
                return true;
            }
        } catch (SQLException e) {
            System.out.println("Error al editar la boleta: " + e.getMessage());
        }
        return false;
    }

}
