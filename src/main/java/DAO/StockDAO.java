/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.Producto;
import CarpetaClases.Stock;
import ConexionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author garca
 */
public class StockDAO {

    private ConexionDB conexionDB;

    public StockDAO() {
        this.conexionDB = new ConexionDB();
    }

    public boolean agregarStock(Stock stock) {
        String sql = "INSERT INTO Stock (cantidadminima, cantidad, fechaactualizacion, producto_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar transacciones
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, stock.getCantidadMinima());
                stmt.setInt(2, stock.getCantidad());
                stmt.setString(3, stock.getFechaActualizacion().toString());
                stmt.setInt(4, stock.getProducto().getId());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        stock.setId(generatedKeys.getInt(1));  // Asignar el ID generado
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

    // Método para obtener todos los stocks
    public ArrayList<Stock> obtenerTodosStocks() {
        String sql = "SELECT * FROM Stock";
        ArrayList<Stock> stocks = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Stock stock = mapResultSetToStock(rs);
                stocks.add(stock);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los stocks: " + e.getMessage());
        }

        return stocks;
    }

    // Método para obtener un stock por ID
    public Stock obtenerStockPorId(int id) {
        String sql = "SELECT * FROM Stock WHERE id = ?";
        Stock stock = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                stock = mapResultSetToStock(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener el stock por ID: " + e.getMessage());
        }

        return stock;
    }

    // Método para eliminar un stock por ID
    public boolean eliminarStock(int id) {
        String sql = "DELETE FROM Stock WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false); // Desactivar auto-commit para manejar transacciones

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    conn.commit(); // Confirmar la transacción
                    System.out.println("Stock eliminado con éxito.");
                    return true;
                } else {
                    conn.rollback(); // Revertir si no se eliminó ninguna fila
                    System.out.println("No se eliminó ningún stock.");
                }
            } catch (SQLException e) {
                conn.rollback(); // Revertir cambios si ocurre algún error
                System.out.println("Error al eliminar Stock: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para editar un stock
    public boolean editarStock(Stock stock) {
        String sql = "UPDATE Stock SET cantidadminima = ?, cantidad = ?, fechaactualizacion = ?, producto_id = ? WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar transacciones

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, stock.getCantidadMinima());
                stmt.setInt(2, stock.getCantidad());
                stmt.setString(3, stock.getFechaActualizacion().toString());
                stmt.setInt(4, stock.getProducto().getId());
                stmt.setInt(5, stock.getId()); // El ID de la stock a editar

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    conn.commit();  // Confirmar la transacción
                    System.out.println("Stock editado con éxito.");
                    return true;
                } else {
                    conn.rollback();  // Revertir si no se actualizó ninguna fila
                    System.out.println("No se editó ningún stock.");
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre algún error
                System.out.println("Error al editar Stock: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método auxiliar para mapear ResultSet a objeto Stock
    private Stock mapResultSetToStock(ResultSet rs) throws SQLException {
        Stock stock = new Stock();
        stock.setId(rs.getInt("id"));
        stock.setCantidadMinima(rs.getInt("cantidadminima"));
        stock.setCantidad(rs.getInt("cantidad"));
        stock.setFechaActualizacion(LocalDate.parse(rs.getString("fechaactualizacion")));

        Producto producto = new Producto();
        producto.setId(rs.getInt("producto_id"));
        stock.setProducto(producto);

        return stock;
    }
}
