/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.CatalogoProductos;
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
public class ProductoDAO {

    private ConexionDB conexionDB;

    public ProductoDAO() {
        this.conexionDB = new ConexionDB();
    }

    // Método para agregar un producto
    // Método para agregar un producto a la base de datos
    public boolean agregarProducto(Producto producto) {
        String sql = "INSERT INTO producto (nombre, precio, codigo, catalogo_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false); // Desactivar auto-commit para manejo manual de la transacción

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, producto.getNombre());
                stmt.setDouble(2, producto.getPrecio());
                stmt.setInt(3, producto.getCodigo());
                stmt.setInt(4, producto.getCatalogo().getId()); // Asignar el catalogo_id

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit(); // Confirmar la transacción
                    System.out.println("Producto agregado con éxito.");
                    return true;
                } else {
                    conn.rollback(); // Deshacer cambios si no se insertó ningún registro
                    System.out.println("No se pudo agregar el producto.");
                }
            } catch (SQLException e) {
                conn.rollback(); // Revertir cambios en caso de error
                System.out.println("Error al agregar producto: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }

        return false; // Si no se pudo realizar la inserción
    }

    // Método para borrar un producto por ID
    public boolean borrarProducto(int id) {
        String sql = "DELETE FROM producto WHERE id = ?";
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int filasAfectadas = stmt.executeUpdate(); // Obtener número de filas afectadas
            return filasAfectadas > 0;
        } catch (SQLException e) {
            System.out.println("Error al borrar producto: " + e.getMessage());
            return false;
        }
    }

    // Método para borrar un producto por su nombre
    public boolean borrarProductoPorNombre(String nombre) {
        String sql = "DELETE FROM producto WHERE nombre = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false); // Desactivar auto-commit para manejo manual de la transacción

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, nombre);

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit(); // Confirmar la transacción
                    System.out.println("Producto eliminado con éxito.");
                    return true;
                } else {
                    conn.rollback(); // Deshacer cambios si no se eliminó ningún registro
                    System.out.println("No se encontró un producto con ese nombre.");
                }
            } catch (SQLException e) {
                conn.rollback(); // Revertir cambios en caso de error
                System.out.println("Error al eliminar producto: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }

        return false; // Si no se pudo realizar la eliminación
    }

    // Método para modificar un producto
    public boolean modificarProducto(Producto producto) {
        String sql = "UPDATE producto SET nombre = ?, precio = ?, codigo = ?, catalogo_id = ? WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejo manual de la transacción

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, producto.getNombre());
                stmt.setDouble(2, producto.getPrecio());
                stmt.setInt(3, producto.getCodigo());
                stmt.setInt(4, producto.getCatalogo().getId());  // Usar el catalogo_id del producto
                stmt.setInt(5, producto.getId());  // Asegurarse de que el ID sea válido

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();  // Confirmar la transacción
                    System.out.println("Producto actualizado con éxito.");
                    return true;
                } else {
                    conn.rollback();  // Deshacer cambios si no se actualizó ninguna fila
                    System.out.println("No se encontró el producto con ID: " + producto.getId());
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios en caso de error
                System.out.println("Error al modificar producto: " + e.getMessage());
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }

        return false;  // Si no se pudo realizar la actualización
    }

    // Método para obtener todos los productos con su catalogo
    public List<Producto> obtenerTodos() {
        String sql = "SELECT p.id, p.nombre, p.precio, p.codigo, p.catalogo_id, c.tipo AS catalogo_tipo "
                + "FROM producto p "
                + "LEFT JOIN catalogo_producto c ON p.catalogo_id = c.id"; // Realizamos el JOIN entre las tablas
        List<Producto> productos = new ArrayList<>();
        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setCodigo(rs.getInt("codigo"));
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));

                // Obtener el catálogo del producto, si existe
                CatalogoProductos catalogo = new CatalogoProductos();
                catalogo.setId(rs.getInt("catalogo_id"));  // Usamos el catalogo_id de la tabla producto
                catalogo.setTipo(rs.getString("catalogo_tipo"));  // El tipo de catalogo viene del JOIN

                producto.setCatalogo(catalogo); // Asignamos el catálogo al producto
                productos.add(producto);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener todos los productos: " + e.getMessage());
        }
        return productos;
    }

    // Método para buscar un producto por código
    public Producto buscarPorCodigo(int codigo) {
        String sql = "SELECT p.id, p.nombre, p.precio, p.codigo, p.catalogo_id, c.tipo AS catalogo_tipo "
                + "FROM producto p "
                + "LEFT JOIN catalogo_producto c ON p.catalogo_id = c.id "
                + "WHERE p.codigo = ?";  // Consulta para obtener el producto por código
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, codigo);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Producto producto = new Producto();
                producto.setCodigo(rs.getInt("codigo"));
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));

                // Verificar si el producto tiene un catálogo asociado
                int catalogoId = rs.getInt("catalogo_id");
                if (!rs.wasNull()) { // Si catalogo_id no es NULL
                    CatalogoProductos catalogo = new CatalogoProductos();
                    catalogo.setId(catalogoId);
                    catalogo.setTipo(rs.getString("catalogo_tipo"));
                    producto.setCatalogo(catalogo);
                } else {
                    producto.setCatalogo(null); // Si no hay catálogo, establecer como null
                }

                return producto; // Devolver el producto encontrado
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto por código: " + e.getMessage());
        }
        return null;
    }

    // Método para buscar un producto por ID
    public Producto buscarPorId(int id) {
        String sql = "SELECT p.id, p.nombre, p.precio, p.codigo, p.catalogo_id, c.tipo AS catalogo_tipo "
                + "FROM producto p "
                + "LEFT JOIN catalogo_producto c ON p.catalogo_id = c.id "
                + "WHERE p.id = ?";  // Consulta para obtener el producto y su catálogo si existe
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id); // Establecemos el ID como parámetro
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setCodigo(rs.getInt("codigo"));

                // Verificar si el producto tiene un catálogo asociado
                int catalogoId = rs.getInt("catalogo_id");
                if (!rs.wasNull()) { // Si catalogo_id no es NULL
                    CatalogoProductos catalogo = new CatalogoProductos();
                    catalogo.setId(catalogoId);
                    catalogo.setTipo(rs.getString("catalogo_tipo"));
                    producto.setCatalogo(catalogo);
                } else {
                    producto.setCatalogo(null); // Si no hay catálogo, establecer como null
                }

                return producto; // Devolver el producto encontrado
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto por ID: " + e.getMessage());
        }

        return null; // Si no se encuentra, devolver null
    }

    // Método para buscar un producto por Nombre
    public Producto buscarPorNombre(String nombre) {
        String sql = "SELECT p.id, p.nombre, p.precio, p.codigo, p.catalogo_id, c.tipo AS catalogo_tipo "
                + "FROM producto p "
                + "LEFT JOIN catalogo_producto c ON p.catalogo_id = c.id "
                + "WHERE p.nombre = ?";  // Consulta para buscar el producto por nombre
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre); // Establecemos el nombre como parámetro
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setNombre(rs.getString("nombre"));
                producto.setPrecio(rs.getDouble("precio"));
                producto.setCodigo(rs.getInt("codigo"));

                // Verificar si el producto tiene un catálogo asociado
                int catalogoId = rs.getInt("catalogo_id");
                if (!rs.wasNull()) { // Si catalogo_id no es NULL
                    CatalogoProductos catalogo = new CatalogoProductos();
                    catalogo.setId(catalogoId);
                    catalogo.setTipo(rs.getString("catalogo_tipo"));
                    producto.setCatalogo(catalogo);
                } else {
                    producto.setCatalogo(null); // Si no hay catálogo, establecer como null
                }

                return producto; // Devolver el producto encontrado
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar producto por nombre: " + e.getMessage());
        }

        return null; // Si no se encuentra, devolver null
    }

    // Método para verificar si existe un producto por código o nombre
    public boolean existeProducto(int codigo, String nombre) {
        String sql = "SELECT 1 FROM producto WHERE codigo = ? OR nombre = ? LIMIT 1"; // Verifica si hay coincidencias
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, codigo);
            stmt.setString(2, nombre);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next(); // Si hay un registro, significa que existe
            }

        } catch (SQLException e) {
            System.out.println("Error al verificar si el producto existe: " + e.getMessage());
            return false; // En caso de error, asumimos que no existe
        }
    }
}
