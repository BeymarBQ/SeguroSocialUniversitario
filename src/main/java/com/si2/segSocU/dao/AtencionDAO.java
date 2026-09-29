package com.si2.segsocu.dao;

import com.si2.segsocu.db.Database;
import com.si2.segsocu.model.Atencion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/** DAO de la tabla atenciones (historial clínico generado por historia 3). */
public class AtencionDAO {

    public int insertar(Atencion a) {
        String sql = """
                INSERT INTO atenciones (cita_id, motivo, diagnostico, indicaciones, fecha)
                VALUES (?, ?, ?, ?, ?)
                """;
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, a.getCitaId());
            ps.setString(2, a.getMotivo());
            ps.setString(3, a.getDiagnostico());
            ps.setString(4, a.getIndicaciones());
            ps.setString(5, a.getFecha());
            ps.executeUpdate();
            try (Statement st = con.createStatement();
                 ResultSet keys = st.executeQuery("SELECT last_insert_rowid()")) {
                keys.next();
                return keys.getInt(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error registrando atención médica", e);
        }
    }

    /** Historial de atenciones de un afiliado (join citas), para historia 13. */
    public List<Atencion> listarPorAfiliado(int afiliadoId) {
        String sql = """
                SELECT a.* FROM atenciones a
                JOIN citas c ON c.id = a.cita_id
                WHERE c.afiliado_id = ?
                ORDER BY a.fecha DESC
                """;
        return listar(sql, afiliadoId);
    }

    public List<Atencion> listarTodas() {
        String sql = "SELECT * FROM atenciones ORDER BY fecha DESC";
        List<Atencion> lista = new ArrayList<>();
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando atenciones", e);
        }
        return lista;
    }

    private List<Atencion> listar(String sql, int afiliadoId) {
        List<Atencion> lista = new ArrayList<>();
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, afiliadoId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapRow(rs));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando atenciones del afiliado", e);
        }
        return lista;
    }

    private Atencion mapRow(ResultSet rs) throws SQLException {
        Atencion a = new Atencion();
        a.setId(rs.getInt("id"));
        a.setCitaId(rs.getInt("cita_id"));
        a.setMotivo(rs.getString("motivo"));
        a.setDiagnostico(rs.getString("diagnostico"));
        a.setIndicaciones(rs.getString("indicaciones"));
        a.setFecha(rs.getString("fecha"));
        return a;
    }
}
