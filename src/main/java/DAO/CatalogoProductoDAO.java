/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.CatalogoProductos;
import ConexionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author garca
 */
public class CatalogoProductoDAO {

    private ConexionDB conexionDB;

    public CatalogoProductoDAO() {
        this.conexionDB = new ConexionDB();
    }

    // Método para agregar un nuevo producto
    public boolean agregarProducto(CatalogoProductos producto) {
        String sql = "INSERT INTO catalogo_producto (tipo) VALUES (?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false); // Desactivar auto-commit para manejar manualmente las transacciones
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, producto.getTipo());  // Tipo de producto

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        producto.setId(generatedKeys.getInt(1));  // Asignar el ID generado al producto
                    }
                    conn.commit();  // Confirmar la transacción
                    System.out.println("Producto agregado con éxito.");
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre algún error
                System.out.println("Error al agregar producto: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener todos los productos
    public ArrayList<CatalogoProductos> obtenerTodosLosProductos() {
        String sql = "SELECT * FROM catalogo_producto";
        ArrayList<CatalogoProductos> productos = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                CatalogoProductos producto = new CatalogoProductos();
                producto.setId(rs.getInt("id"));
                producto.setTipo(rs.getString("tipo"));
                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener todos los productos: " + e.getMessage());
        }

        return productos;
    }

    public CatalogoProductos obtenerCatalogoPorNombre(String nombre) {
        String sql = "SELECT * FROM catalogo_producto WHERE tipo = ?";
        CatalogoProductos producto = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);  // Establecer el nombre del catálogo como parámetro
            ResultSet rs = stmt.executeQuery();

            // Verificar si se obtuvo un resultado
            if (rs.next()) {
                producto = new CatalogoProductos();
                producto.setId(rs.getInt("id"));
                producto.setTipo(rs.getString("tipo"));
            } else {
                // Log si no se encuentra el producto
                System.out.println("No se encontró un catálogo con el nombre: " + nombre);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener catalogo por nombre: " + e.getMessage());
        }

        return producto;  // Si no se encuentra, devuelve null
    }

    // Método para eliminar un producto
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM catalogo_producto WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();  // Confirmar la transacción
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre un error
                System.out.println("Error al eliminar producto: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    public CatalogoProductos obtenerCatalogoPorId(int id) {
        String sql = "SELECT * FROM catalogo_producto WHERE id = ?";
        CatalogoProductos producto = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);  // Establecer el id como parámetro
            ResultSet rs = stmt.executeQuery();

            // Verificar si se obtuvo un resultado
            if (rs.next()) {
                producto = new CatalogoProductos();
                producto.setId(rs.getInt("id"));
                producto.setTipo(rs.getString("tipo"));
            } else {
                // Log si no se encuentra el producto
                System.out.println("No se encontró un catálogo con el ID: " + id);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener catalogo por ID: " + e.getMessage());
        }

        return producto;  // Si no se encuentra, devuelve null
    }

}
