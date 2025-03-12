/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.Fecha;
import CarpetaClases.Venta;
import ConexionDB.ConexionDB;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import DAO.FechaDAO;
import java.time.LocalDate;

/**
 *
 * @author garca
 */
public class VentaDAO {

    private ConexionDB conexionDB;

    public VentaDAO() {
        this.conexionDB = new ConexionDB();
    }

    public boolean agregarVenta(Venta venta) {
        String sql = "INSERT INTO venta (fecha, medio_pago, total) VALUES (?, ?, ?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false); // Desactivar auto-commit para manejar manualmente las transacciones
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                // Insertar la fecha como String
                stmt.setString(1, venta.getFecha().toString());  // Convertir LocalDate a String
                stmt.setString(2, venta.getMedioDePago());  // Medio de pago
                stmt.setDouble(3, venta.getTotal());  // Total de la venta

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        venta.setId(generatedKeys.getInt(1));  // Asignar el ID generado a la venta
                    }
                    conn.commit();  // Confirmar la transacción
                    System.out.println("Venta agregada con éxito.");
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre algún error
                System.out.println("Error al agregar venta: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener una venta por su ID
    public Venta obtenerVentaPorId(int id) {
        String sql = "SELECT * FROM venta WHERE id = ?";
        Venta venta = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                venta = new Venta();
                // Obtener la fecha como String
                String fechaString = rs.getString("fecha");
                LocalDate fecha = LocalDate.parse(fechaString);  // Convertir el String a LocalDate

                // Rellenar los atributos de la venta
                venta.setId(rs.getInt("id"));
                venta.setFecha(fecha);  // Asignar la fecha
                venta.setMedioDePago(rs.getString("medio_pago"));
                venta.setTotal(rs.getDouble("total"));
            }

        } catch (SQLException e) {
            System.out.println("Error al obtener venta: " + e.getMessage());
        }

        return venta;
    }

    // Método para obtener todas las ventas
    public ArrayList<Venta> obtenerTodasLasVentas() {
        String sql = "SELECT * FROM venta";
        ArrayList<Venta> ventas = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));

                // Obtener la fecha como String
                String fechaString = rs.getString("fecha");
                LocalDate fecha = LocalDate.parse(fechaString);  // Convertir el String a LocalDate
                venta.setFecha(fecha);  // Asignar la fecha

                venta.setMedioDePago(rs.getString("medio_pago"));
                venta.setTotal(rs.getDouble("total"));
                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todas las ventas: " + e.getMessage());
        }

        return ventas;
    }

    // Método para actualizar una venta
    public boolean actualizarVenta(Venta venta) {
        String sql = "UPDATE venta SET fecha = ?, medio_pago = ?, total = ? WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                // Convertir LocalDate a String para la base de datos
                stmt.setString(1, venta.getFecha().toString());  // Convertir LocalDate a String
                stmt.setString(2, venta.getMedioDePago());
                stmt.setDouble(3, venta.getTotal());
                stmt.setInt(4, venta.getId());

                int affectedRows = stmt.executeUpdate();
                if (affectedRows > 0) {
                    conn.commit();  // Confirmar la transacción
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre un error
                System.out.println("Error al actualizar venta: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para eliminar una venta
    public boolean eliminarVenta(int id) {
        String sql = "DELETE FROM venta WHERE id = ?";
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
                System.out.println("Error al eliminar venta: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener las ventas por fecha (en formato yyyy-MM-dd)
    public ArrayList<Venta> obtenerVentasPorFecha(int anio, int mes, int dia) {
        ArrayList<Venta> ventas = new ArrayList<>();
        // Formatear la fecha en formato YYYY-MM-DD
        String fecha = String.format("%04d-%02d-%02d", anio, mes, dia);

        // Consulta directa por fecha usando la cadena de fecha
        String sql = "SELECT v.id, v.fecha, v.medio_pago, v.total "
                + "FROM venta v "
                + "WHERE v.fecha = ?";  // Comparar la fecha como String

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            // Establecer el parámetro con la fecha formateada
            stmt.setString(1, fecha);

            ResultSet rs = stmt.executeQuery();

            // Iterar sobre los resultados de la consulta
            while (rs.next()) {
                Venta venta = new Venta();
                venta.setId(rs.getInt("id"));

                // Obtener la fecha como String
                String fechaString = rs.getString("fecha");
                LocalDate fechaObj = LocalDate.parse(fechaString);  // Convertir el String a LocalDate

                venta.setFecha(fechaObj);  // Asignar la fecha completa a la venta
                venta.setMedioDePago(rs.getString("medio_pago"));
                venta.setTotal(rs.getDouble("total"));

                ventas.add(venta);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener las ventas por fecha: " + e.getMessage());
        }

        return ventas;
    }

}
