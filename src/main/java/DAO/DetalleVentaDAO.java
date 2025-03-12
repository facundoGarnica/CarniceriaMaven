/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.DetalleVenta;
import CarpetaClases.Producto;
import CarpetaClases.Venta;
import ConexionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author garca
 */
public class DetalleVentaDAO {

    private ConexionDB conexionDB;

    public DetalleVentaDAO() {
        this.conexionDB = new ConexionDB();
    }

    // Método para agregar un detalle de venta
    public boolean agregarDetalleVenta(DetalleVenta detalleVenta) {
        String sql = "INSERT INTO Detalleventa (venta_id, producto_id, peso, precio) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false); // Desactivar auto-commit para manejar transacciones manualmente

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, detalleVenta.getVenta().getId());   // ID de la venta
                stmt.setInt(2, detalleVenta.getProducto().getId()); // ID del producto
                stmt.setDouble(3, detalleVenta.getPeso());          // Peso (DOUBLE en base de datos)
                stmt.setDouble(4, detalleVenta.getPrecio());         // Precio (FLOAT en base de datos)

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            detalleVenta.setId(generatedKeys.getInt(1));  // Establecer el ID generado automáticamente
                            System.out.println("ID generado: " + detalleVenta.getId()); // Debugging
                        }
                    }
                    conn.commit();  // Confirmar la transacción
                    return true;
                } else {
                    conn.rollback();  // Revertir si no se insertaron filas
                    System.out.println("No se insertó ningún registro en la tabla detalle_venta.");
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir en caso de error
                System.out.println("Error al agregar detalle de venta: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener todos los detalles de una venta
    public ArrayList<DetalleVenta> obtenerDetallesPorVenta(int idVenta) {
        String sql = "SELECT * FROM Detalleventa WHERE venta_id = ?";
        ArrayList<DetalleVenta> detallesVenta = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idVenta);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                DetalleVenta detalleVenta = mapResultSetToDetalleVenta(rs);
                detallesVenta.add(detalleVenta);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalles de la venta: " + e.getMessage());
        }

        return detallesVenta;
    }

    // Método para eliminar un detalle de venta por su ID
    public boolean eliminarDetalleVenta(int id) {
        String sql = "DELETE FROM Detalleventa WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();
                    return true;
                } else {
                    conn.rollback();
                    System.out.println("No se eliminó ningún registro en la tabla detalle_venta.");
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error al eliminar detalle de venta (rollback aplicado): " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener un detalle por ID
    public DetalleVenta obtenerDetallePorId(int id) {
        String sql = "SELECT * FROM Detalleventa WHERE id = ?";
        DetalleVenta detalleVenta = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                detalleVenta = mapResultSetToDetalleVenta(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalle por ID: " + e.getMessage());
        }

        return detalleVenta;
    }

    // Método para obtener todos los detalles
    public List<DetalleVenta> obtenerTodosDetalles() {
        String sql = "SELECT * FROM Detalleventa";
        List<DetalleVenta> detallesVenta = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                DetalleVenta detalleVenta = mapResultSetToDetalleVenta(rs);
                detallesVenta.add(detalleVenta);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los detalles de venta: " + e.getMessage());
        }

        return detallesVenta;
    }

    // Método auxiliar para mapear ResultSet a objeto DetalleVenta
    private DetalleVenta mapResultSetToDetalleVenta(ResultSet rs) throws SQLException {
        DetalleVenta detalleVenta = new DetalleVenta();
        detalleVenta.setId(rs.getInt("id"));         // ID del detalle
        detalleVenta.setPeso(rs.getDouble("peso")); // Peso como DOUBLE
        detalleVenta.setPrecio(rs.getDouble("precio")); // Precio como FLOAT

        Producto producto = new Producto();
        producto.setId(rs.getInt("producto_id"));
        detalleVenta.setProducto(producto);

        Venta venta = new Venta();
        venta.setId(rs.getInt("venta_id"));
        detalleVenta.setVenta(venta);

        return detalleVenta;
    }
}
