/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.Cliente;
import ConexionDB.ConexionDB;
import java.sql.*;
import java.util.ArrayList;

public class ClienteDAO {

    private ConexionDB conexionDB;

    public ClienteDAO() {
        this.conexionDB = new ConexionDB();
    }

    public Cliente obtenerClientePorNombreApellido(String nombre, String apellido) {
        String sql = "SELECT * FROM Cliente WHERE nombre = ? AND apellido = ?";
        Cliente cliente = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre); // Establecer el valor del nombre.
            stmt.setString(2, apellido); // Establecer el valor del apellido.

            ResultSet rs = stmt.executeQuery(); // Ejecutar la consulta.

            if (rs.next()) {
                // Crear una instancia de Cliente y asignar los valores obtenidos de la base de datos.
                cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setTelefono(rs.getString("telefono"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cliente por nombre y apellido: " + e.getMessage());
        }

        return cliente; // Devuelve el cliente o null si no se encontró.
    }

    // Método para agregar un nuevo cliente
    public boolean agregarCliente(Cliente cliente) {
        String sql = "INSERT INTO Cliente (nombre, apellido, telefono) VALUES (?, ?, ?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar transacciones
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, cliente.getNombre());  // Nombre del cliente
                stmt.setString(2, cliente.getApellido());  // Apellido del cliente
                stmt.setString(3, cliente.getTelefono());  // Teléfono del cliente

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        cliente.setId(generatedKeys.getInt(1));  // Asignar el ID generado al cliente
                    }
                    conn.commit();  // Confirmar la transacción
                    System.out.println("Cliente agregado con éxito.");
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre un error
                System.out.println("Error al agregar cliente: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener un cliente por su ID
    public Cliente obtenerClientePorId(int id) {
        String sql = "SELECT * FROM Cliente WHERE id = ?";
        Cliente cliente = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setTelefono(rs.getString("telefono"));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener cliente: " + e.getMessage());
        }

        return cliente;
    }

    // Método para obtener todos los clientes
    public ArrayList<Cliente> obtenerTodosLosClientes() {
        String sql = "SELECT * FROM Cliente";
        ArrayList<Cliente> clientes = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setTelefono(rs.getString("telefono"));
                clientes.add(cliente);
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener todos los clientes: " + e.getMessage());
        }

        return clientes;
    }

    // Método para actualizar un cliente
    public boolean actualizarCliente(Cliente cliente) {
        String sql = "UPDATE Cliente SET nombre = ?, apellido = ?, telefono = ? WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, cliente.getNombre());
                stmt.setString(2, cliente.getApellido());
                stmt.setString(3, cliente.getTelefono());
                stmt.setInt(4, cliente.getId());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();  // Confirmar la transacción
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre un error
                System.out.println("Error al actualizar cliente: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para eliminar un cliente
    public boolean eliminarCliente(int id) {
        String sql = "DELETE FROM Cliente WHERE id = ?";
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
                System.out.println("Error al eliminar cliente: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }
    // Método para verificar si un cliente existe por nombre y apellido

    public boolean existeCliente(Cliente cliente) {
        String sql = "SELECT COUNT(*) FROM Cliente WHERE nombre = ? AND apellido = ?";
        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cliente.getNombre());
            stmt.setString(2, cliente.getApellido());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // Si el conteo es mayor que 0, el cliente ya existe
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar si el cliente existe: " + e.getMessage());
        }
        return false;
    }
// Método para obtener un cliente por su nombre y teléfono

    public Cliente obtenerClientePorNombreTelefono(String nombre, String telefono) {
        String sql = "SELECT * FROM Cliente WHERE nombre = ? AND telefono = ?";
        Cliente cliente = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            stmt.setString(2, telefono);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                cliente = new Cliente();
                cliente.setId(rs.getInt("id"));
                cliente.setNombre(rs.getString("nombre"));
                cliente.setApellido(rs.getString("apellido"));
                cliente.setTelefono(rs.getString("telefono"));
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener cliente por nombre y teléfono: " + e.getMessage());
        }

        return cliente;
    }
    // Método para eliminar un cliente por nombre y apellido

    public boolean eliminarClientePorNombreApellido(String nombre, String apellido) {
        String sqlBuscarCliente = "SELECT id FROM Cliente WHERE nombre = ? AND apellido = ?";
        String sqlEliminarCliente = "DELETE FROM Cliente WHERE id = ?";

        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false); // Desactivar auto-commit para manejar transacciones

            // Buscar el ID del cliente
            try (PreparedStatement stmtBuscar = conn.prepareStatement(sqlBuscarCliente)) {
                stmtBuscar.setString(1, nombre);
                stmtBuscar.setString(2, apellido);

                ResultSet rs = stmtBuscar.executeQuery();
                if (rs.next()) {
                    int clienteId = rs.getInt("id");

                    // Eliminar el cliente usando su ID
                    try (PreparedStatement stmtEliminar = conn.prepareStatement(sqlEliminarCliente)) {
                        stmtEliminar.setInt(1, clienteId);
                        int affectedRows = stmtEliminar.executeUpdate();

                        if (affectedRows > 0) {
                            conn.commit(); // Confirmar la transacción
                            return true;
                        } else {
                            System.out.println("No se pudo eliminar el cliente con nombre: " + nombre + " y apellido: " + apellido);
                        }
                    }
                } else {
                    System.out.println("Cliente no encontrado con nombre: " + nombre + " y apellido: " + apellido);
                }
            } catch (SQLException e) {
                conn.rollback(); // Revertir cambios si ocurre un error
                System.out.println("Error durante la eliminación del cliente: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

}
