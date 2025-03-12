/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import CarpetaClases.BoletaCarne;
import CarpetaClases.Medias;
import ConexionDB.ConexionDB;
import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author garca
 */
public class MediaDAO {

    private ConexionDB conexionDB;

    public MediaDAO() {
        this.conexionDB = new ConexionDB();
    }

    // Método para agregar un nuevo MediaRes
    public boolean agregarMedias(Medias media) {
        String sql = "INSERT INTO MediaRes (pesobalanza, pesoboleta, pesofinal, boleta_id) VALUES (?, ?, ?, ?)";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar manualmente las transacciones
            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setDouble(1, media.getPesoBalanza());
                stmt.setDouble(2, media.getPesoBoleta());
                stmt.setDouble(3, media.getPesoFinal());
                stmt.setInt(4, media.getBoletaCarne().getId());

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    ResultSet generatedKeys = stmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        media.setId(generatedKeys.getInt(1));  // Asignar el ID generado al MediaRes
                    }
                    conn.commit();  // Confirmar la transacción
                    System.out.println("MediaRes agregado con éxito.");
                    return true;
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre algún error
                System.out.println("Error al agregar MediaRes: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para obtener todas las medias
    public ArrayList<Medias> obtenerTodasMedias() {
        String sql = "SELECT * FROM MediaRes";
        ArrayList<Medias> medias = new ArrayList<>();

        try (Connection conn = conexionDB.conectar(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Medias media = mapResultSetToMedia(rs);
                medias.add(media);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener todas las medias: " + e.getMessage());
        }

        return medias;
    }

    // Método para obtener una media por ID
    public Medias obtenerMediaPorId(int id) {
        String sql = "SELECT * FROM MediaRes WHERE id = ?";
        Medias media = null;

        try (Connection conn = conexionDB.conectar(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                media = mapResultSetToMedia(rs);
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener la media por ID: " + e.getMessage());
        }

        return media;
    }

    // Método para eliminar una media por ID
    public boolean eliminarMedia(int id) {
        String sql = "DELETE FROM MediaRes WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false); // Desactivar auto-commit para manejar transacciones manualmente

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    conn.commit(); // Confirmar la transacción
                    System.out.println("MediaRes eliminada con éxito.");
                    return true;
                } else {
                    conn.rollback(); // Revertir si no se eliminó ninguna fila
                    System.out.println("No se eliminó ninguna media.");
                }
            } catch (SQLException e) {
                conn.rollback(); // Revertir cambios si ocurre algún error
                System.out.println("Error al eliminar MediaRes: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método para editar una media
    public boolean editarMedia(Medias media) {
        String sql = "UPDATE MediaRes SET pesobalanza = ?, pesoboleta = ?, pesofinal = ?, boleta_id = ? WHERE id = ?";
        try (Connection conn = conexionDB.conectar()) {
            conn.setAutoCommit(false);  // Desactivar auto-commit para manejar manualmente las transacciones

            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setDouble(1, media.getPesoBalanza());
                stmt.setDouble(2, media.getPesoBoleta());
                stmt.setDouble(3, media.getPesoFinal());
                stmt.setInt(4, media.getBoletaCarne().getId());
                stmt.setInt(5, media.getId()); // El ID de la media a editar

                int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    conn.commit();  // Confirmar la transacción
                    System.out.println("MediaRes editada con éxito.");
                    return true;
                } else {
                    conn.rollback();  // Revertir si no se actualizó ninguna fila
                    System.out.println("No se editó ninguna media.");
                }
            } catch (SQLException e) {
                conn.rollback();  // Revertir cambios si ocurre algún error
                System.out.println("Error al editar MediaRes: " + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
        return false;
    }

    // Método auxiliar para mapear ResultSet a objeto Medias
    private Medias mapResultSetToMedia(ResultSet rs) throws SQLException {
        Medias media = new Medias();
        media.setId(rs.getInt("id"));
        media.setPesoBalanza(rs.getDouble("pesobalanza"));
        media.setPesoBoleta(rs.getDouble("pesoboleta"));
        media.setPesoFinal(rs.getDouble("pesofinal"));

        BoletaCarne boleta = new BoletaCarne();
        boleta.setId(rs.getInt("boleta_id"));
        media.setBoletaCarne(boleta);

        return media;
    }
}
