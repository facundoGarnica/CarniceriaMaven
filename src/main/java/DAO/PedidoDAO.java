/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.Cliente;
import CarpetaClases.Pedido;
import ConexionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class PedidoDAO {

    private ConexionDB conexionDB;

    public PedidoDAO() {
        this.conexionDB = new ConexionDB();
    }

    // Método para agregar un nuevo pedido
    public boolean agregarPedido(Pedido pedido) {
        String sql = "INSERT INTO Pedido (cliente_id, fecha, retiro, senia) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setInt(1, pedido.getCliente().getId());
                stmt.setString(2, pedido.getFechaPedido().toString());  // Convertir LocalDate a String
                stmt.setString(3, pedido.getRetiro());
                stmt.setDouble(4, pedido.getSenia());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        pedido.setId(generatedKeys.getInt(1));
                    }
                    conn.commit();
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error al agregar pedido: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener un pedido por su ID
    public Pedido obtenerPedidoPorId(int id) {
        String sql = "SELECT * FROM Pedido WHERE id = ?";
        Pedido pedido = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setFechaPedido(LocalDate.parse(rs.getString("fecha")));  // Convertir String a LocalDate
                pedido.setRetiro(rs.getString("retiro"));
                pedido.setSenia(rs.getDouble("senia"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("cliente_id"));
                pedido.setCliente(cliente);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pedido: " + e.getMessage());
        }

        return pedido;
    }

    // Método para obtener todos los pedidos
    public ArrayList<Pedido> obtenerTodosPedidos() {
        String sql = "SELECT * FROM Pedido";
        ArrayList<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setFechaPedido(LocalDate.parse(rs.getString("fecha")));  // Convertir String a LocalDate
                pedido.setRetiro(rs.getString("retiro"));
                pedido.setSenia(rs.getDouble("senia"));

                Cliente cliente = new Cliente();
                cliente.setId(rs.getInt("cliente_id"));
                pedido.setCliente(cliente);

                pedidos.add(pedido);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todos los pedidos: " + e.getMessage());
        }

        return pedidos;
    }

    // Método para actualizar un pedido
    public boolean actualizarPedido(Pedido pedido) {
        String sql = "UPDATE Pedido SET cliente_id = ?, fecha = ?, retiro = ?, senia = ? WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, pedido.getCliente().getId());
                stmt.setString(2, pedido.getFechaPedido().toString());  // Convertir LocalDate a String
                stmt.setString(3, pedido.getRetiro());
                stmt.setDouble(4, pedido.getSenia());
                stmt.setInt(5, pedido.getId());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error al actualizar pedido: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para eliminar un pedido
    public boolean eliminarPedido(int id) {
        String sql = "DELETE FROM Pedido WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error al eliminar pedido: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    public boolean eliminarPedidosPorNombreTelefono(String nombre, String apellido) {
        String sqlCliente = "SELECT id FROM Cliente WHERE nombre = ? AND telefono = ?"; // Consulta para obtener el ID del cliente
        String sqlEliminar = "DELETE FROM Pedido WHERE cliente_id = ?"; // Consulta para eliminar pedidos por cliente_id

        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);

            try (PreparedStatement stmtCliente = conn.prepareStatement(sqlCliente)) {
                stmtCliente.setString(1, nombre);
                stmtCliente.setString(2, apellido);
                ResultSet rs = stmtCliente.executeQuery();

                if (rs.next()) {
                    int clienteId = rs.getInt("id");

                    try (PreparedStatement stmtEliminar = conn.prepareStatement(sqlEliminar)) {
                        stmtEliminar.setInt(1, clienteId);
                        int affectedRows = stmtEliminar.executeUpdate();

                        if (affectedRows > 0) {
                            conn.commit();
                            return true;
                        } else {
                            System.out.println("No se encontraron pedidos para el cliente con nombre: " + nombre + " " + apellido);
                        }
                    }
                } else {
                    System.out.println("Cliente no encontrado con el nombre: " + nombre + " y apellido: " + apellido);
                }
            } catch (SQLException e) {
                conn.rollback();
                System.out.println("Error durante la eliminación de pedidos: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    public ArrayList<Pedido> obtenerPedidosPorClienteId(int clienteId) {
        String sql = "SELECT * FROM Pedido WHERE cliente_id = ?";
        ArrayList<Pedido> pedidos = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, clienteId); // Establecer el ID del cliente en la consulta.
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                // Crear un objeto Pedido y asignar valores obtenidos de la base de datos.
                Pedido pedido = new Pedido();
                pedido.setId(rs.getInt("id"));
                pedido.setFechaPedido(LocalDate.parse(rs.getString("fecha"))); // Convertir String a LocalDate.
                pedido.setRetiro(rs.getString("retiro"));
                pedido.setSenia(rs.getDouble("senia"));

                Cliente cliente = new Cliente();
                cliente.setId(clienteId); // Asignar el ID del cliente al objeto Cliente.
                pedido.setCliente(cliente);

                pedidos.add(pedido); // Agregar el pedido a la lista.
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener pedidos por cliente_id: " + e.getMessage());
        }

        return pedidos; // Retornar la lista de pedidos.
    }

}
