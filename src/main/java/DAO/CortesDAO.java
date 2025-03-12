/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;


import CarpetaClases.Cortes;
import ConexionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author garca
 */
public class CortesDAO {
    private ConexionDB conexionDB;
    
    public CortesDAO() {
        this.conexionDB = new ConexionDB();
    }
    
    public boolean agregarCortes(Cortes corte) {
        String sql = "INSERT INTO Cortes (peso, NombreCorte) VALUES (?, ?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar transacciones
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, corte.getPeso()); 
                stmt.setString(2, corte.getNombreCorte()); 

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        corte.setId(generatedKeys.getInt(1));  // Asignar el ID generado
                    }
                    conn.commit();  // Confirmar la transacción
                    System.out.println("Corte agregado con éxito.");
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre un error
                System.out.println("Error al agregar corte: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }
}
