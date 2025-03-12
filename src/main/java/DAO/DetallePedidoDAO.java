/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.DetallePedido;
import CarpetaClases.Pedido;
import CarpetaClases.Producto;
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
public class DetallePedidoDAO {

    private ConexionDB conexionDB;

    public DetallePedidoDAO() {
        this.conexionDB = new ConexionDB();
    }

    // Método para agregar un detalle de pedido
    public boolean agregarDetallePedido(DetallePedido detallePedido) {
        String sql = "INSERT INTO Detalle_Pedido (pedido_id, producto_id, cantidad, unidad_medida) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            conn.setAutoCommit(false); // Manejo de la transacción
            stmt.setInt(1, detallePedido.getPedido().getId());   // ID del pedido
            stmt.setInt(2, detallePedido.getProducto().getId()); // ID del producto
            stmt.setDouble(3, detallePedido.getCantidad());      // Cantidad (peso o unidades)
            stmt.setString(4, detallePedido.getUnidadMedida());  // Unidad de medida

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        detallePedido.setId(generatedKeys.getInt(1)); // Establecer el ID generado automáticamente
                    }
                }
                conn.commit(); // Confirmar la transacción
                return true;
            } else {
                conn.rollback(); // Revertir si no se insertaron filas
                System.out.println("No se insertó ningún registro en la tabla DetallePedido.");
            }
        } catch (SQLException e) {
            System.out.println("Error al agregar detalle de pedido: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener todos los detalles de un pedido
    public ArrayList<DetallePedido> obtenerDetallesPorPedido(int idPedido) {
        String sql = "SELECT * FROM Detalle_Pedido WHERE pedido_id = ?";
        ArrayList<DetallePedido> detallesPedido = new ArrayList<>(); // Lista para almacenar los detalles del pedido

        try (Connection conn = conexionDB.conectar(); // Conexión a la base de datos
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idPedido); // Establecer el ID del pedido en el SQL
            ResultSet rs = stmt.executeQuery(); // Ejecutar la consulta y obtener el resultado

            // Iterar sobre los resultados
            while (rs.next()) {
                DetallePedido detallePedido = mapResultSetToDetallePedido(rs); // Mapear el resultado a un objeto DetallePedido
                detallesPedido.add(detallePedido); // Agregar el detalle a la lista
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalles del pedido: " + e.getMessage());
        }

        return detallesPedido; // Devolver la lista con los detalles del pedido
    }

    // Método para eliminar un detalle de pedido por su ID
    public boolean eliminarDetallePedido(int id) {
        String sql = "DELETE FROM Detalle_Pedido WHERE id = ?";
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
                    System.out.println("No se eliminó ningún registro en la tabla DetallePedido.");
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error al eliminar detalle de pedido (rollback aplicado): " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminarDetallesPorPedidoId(int pedidoId) {
        String sql = "DELETE FROM Detalle_Pedido WHERE pedido_id = ?";
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, pedidoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.out.println("Error al eliminar detalles del pedido: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener un detalle por ID
    public DetallePedido obtenerDetallePorId(int id) {
        String sql = "SELECT * FROM Detalle_Pedido WHERE id = ?";
        DetallePedido detallePedido = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                detallePedido = mapResultSetToDetallePedido(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener detalle por ID: " + e.getMessage());
        }

        return detallePedido;
    }

    // Método auxiliar para mapear ResultSet a objeto DetallePedido
    private DetallePedido mapResultSetToDetallePedido(ResultSet rs) throws SQLException {
        DetallePedido detallePedido = new DetallePedido();
        detallePedido.setId(rs.getInt("id"));             // ID del detalle
        detallePedido.setCantidad(rs.getDouble("cantidad")); // Cantidad (peso o unidades)
        detallePedido.setUnidadmedida(rs.getString("unidad_medida")); // Unidad de medida

        Producto producto = new Producto();
        producto.setId(rs.getInt("producto_id"));
        detallePedido.setProducto(producto);

        Pedido pedido = new Pedido();
        pedido.setId(rs.getInt("pedido_id"));
        detallePedido.setPedido(pedido);

        return detallePedido;
    }

    public List<DetallePedido> obtenerTodosLosDetalles() {
        String sql = "SELECT id, cantidad, producto_id, unidad_medida FROM Detalle_Pedido";  // Seleccionar también la unidad de medida
        List<DetallePedido> detallesPedido = new ArrayList<>();
        ProductoDAO productoDAO = new ProductoDAO();  // Crear una instancia de ProductoDAO

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                DetallePedido detallePedido = new DetallePedido();
                detallePedido.setId(rs.getInt("id"));
                detallePedido.setCantidad(rs.getDouble("cantidad"));
                detallePedido.setUnidadmedida(rs.getString("unidad_medida"));  // Asignar la unidad de medida

                // Obtener el producto asociado a este detalle
                int productoId = rs.getInt("producto_id");
                Producto producto = productoDAO.buscarPorId(productoId);  // Usamos ProductoDAO para obtener el producto

                if (producto != null) {
                    detallePedido.setProducto(producto);  // Asignamos el producto al detalle
                } else {
                    System.out.println("Producto no encontrado para el detalle con ID: " + detallePedido.getId());
                }

                detallesPedido.add(detallePedido);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener todos los detalles de pedido: " + e.getMessage());
        }

        return detallesPedido;
    }

}

