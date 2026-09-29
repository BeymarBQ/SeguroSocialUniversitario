package com.si2.segsocu.dao;

import com.si2.segsocu.db.Database;
import com.si2.segsocu.model.Disponibilidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO de la tabla disponibilidad: horarios y cupos que la administración
 * habilita para cada médico (historia 4), y que se consumen al reservar
 * una cita (historia 2) o se liberan al cancelarla (historia 6).
 */
public class DisponibilidadDAO {

    private static final String SELECT_BASE = """
            SELECT d.id, d.medico_id, m.nombres AS medico_nombres, d.fecha, d.hora, d.cupos
            FROM disponibilidad d
            JOIN medicos m ON m.id = d.medico_id
            """;

    public List<Disponibilidad> listarTodos() {
        String sql = SELECT_BASE + " ORDER BY d.fecha, d.hora";
        return ejecutarListado(sql, ps -> {});
    }

    /** Slots con cupos disponibles para una especialidad, usados al reservar una cita. */
    public List<Disponibilidad> listarDisponiblesPorEspecialidad(int especialidadId) {
        String sql = SELECT_BASE + """
                 JOIN especialidades e ON e.id = m.especialidad_id
                 WHERE e.id = ? AND d.cupos > 0
                 ORDER BY d.fecha, d.hora
                """;
        return ejecutarListado(sql, ps -> ps.setInt(1, especialidadId));
    }

    public void insertar(int medicoId, String fecha, String hora, int cupos) {
        String sql = "INSERT INTO disponibilidad (medico_id, fecha, hora, cupos) VALUES (?, ?, ?, ?)";
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setString(2, fecha);
            ps.setString(3, hora);
            ps.setInt(4, cupos);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error creando disponibilidad", e);
        }
    }

    public void eliminar(int id) {
        String sql = "DELETE FROM disponibilidad WHERE id = ?";
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando disponibilidad", e);
        }
    }

    public void decrementarCupo(int id) {
        String sql = "UPDATE disponibilidad SET cupos = cupos - 1 WHERE id = ? AND cupos > 0";
        ejecutarUpdate(sql, id);
    }

    /** Se usa al cancelar una cita: libera un cupo en el slot médico+fecha+hora correspondiente. */
    public void incrementarCupoPorMedicoFechaHora(int medicoId, String fecha, String hora) {
        String sql = "UPDATE disponibilidad SET cupos = cupos + 1 WHERE medico_id = ? AND fecha = ? AND hora = ?";
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, medicoId);
            ps.setString(2, fecha);
            ps.setString(3, hora);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error liberando cupo", e);
        }
    }

    private void ejecutarUpdate(String sql, int id) {
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando disponibilidad", e);
        }
    }

    private interface Binder {
        void bind(PreparedStatement ps) throws SQLException;
    }

    private List<Disponibilidad> ejecutarListado(String sql, Binder binder) {
        List<Disponibilidad> lista = new ArrayList<>();
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            binder.bind(ps);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new Disponibilidad(
                            rs.getInt("id"),
                            rs.getInt("medico_id"),
                            rs.getString("medico_nombres"),
                            rs.getString("fecha"),
                            rs.getString("hora"),
                            rs.getInt("cupos")));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando disponibilidad", e);
        }
        return lista;
    }
}
