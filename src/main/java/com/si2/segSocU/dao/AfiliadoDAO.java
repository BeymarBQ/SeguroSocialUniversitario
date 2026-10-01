package com.si2.segsocu.dao;

import com.si2.segsocu.db.Database;
import com.si2.segsocu.model.Afiliado;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
""
/**
 * DAO de la tabla afiliados.
 *
 * PATRON A SEGUIR: cada entidad (Cita, Atencion, Solicitud, etc.) debe
 * tener su propio DAO en este mismo paquete, siguiendo esta misma
 * estructura (metodos insertar/buscar/listar/actualizar segun lo que
 * necesite su historia de usuario).
 */
public class AfiliadoDAO {

    public boolean existeRegistro(String registroUniversitario) {
        String sql = "SELECT 1 FROM afiliados WHERE registro_universitario = ?";
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, registroUniversitario);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error verificando registro universitario", e);
        }
    }

    public void insertar(Afiliado a) {
        String sql = """
            INSERT INTO afiliados
                (registro_universitario, nombres, apellidos, rol, fecha_nacimiento,
                 telefono, email, domicilio, estado, fecha_afiliacion)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, a.getRegistroUniversitario());
            ps.setString(2, a.getNombres());
            ps.setString(3, a.getApellidos());
            ps.setString(4, a.getRol());
            ps.setString(5, a.getFechaNacimiento());
            ps.setString(6, a.getTelefono());
            ps.setString(7, a.getEmail());
            ps.setString(8, a.getDomicilio());
            ps.setString(9, a.getEstado());
            ps.setString(10, a.getFechaAfiliacion());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error registrando afiliado", e);
        }
    }

    public void actualizarDatosPersonales(int id, String telefono, String email, String domicilio) {
        String sql = "UPDATE afiliados SET telefono = ?, email = ?, domicilio = ? WHERE id = ?";
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, telefono);
            ps.setString(2, email);
            ps.setString(3, domicilio);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error actualizando datos personales", e);
        }
    }

    public void cambiarEstado(int id, String nuevoEstado) {
        String sql = "UPDATE afiliados SET estado = ? WHERE id = ?";
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nuevoEstado);
            ps.setInt(2, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error cambiando estado del afiliado", e);
        }
    }

    public Optional<Afiliado> buscarPorRegistro(String registroUniversitario) {
        String sql = "SELECT * FROM afiliados WHERE registro_universitario = ?";
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, registroUniversitario);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(mapRow(rs));
                }
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error buscando afiliado", e);
        }
    }

    public List<Afiliado> listarTodos() {
        String sql = "SELECT * FROM afiliados ORDER BY apellidos, nombres";
        List<Afiliado> lista = new ArrayList<>();
        try (Connection con = Database.get().getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                lista.add(mapRow(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listando afiliados", e);
        }
        return lista;
    }

    private Afiliado mapRow(ResultSet rs) throws SQLException {
        return new Afiliado(
                rs.getInt("id"),
                rs.getString("registro_universitario"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("rol"),
                rs.getString("fecha_nacimiento"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("domicilio"),
                rs.getString("estado"),
                rs.getString("fecha_afiliacion")
        );
    }
    public boolean tieneProcesosMedicosAbiertos(int afiliadoId) {
        String sql = """
            SELECT COUNT(*) AS total 
            FROM citas 
            WHERE afiliado_id = ? AND estado IN ('PENDIENTE', 'EN_PROCESO')
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, afiliadoId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 08. Registrar la fecha efectiva de la baja y actualizar estado
    public boolean darDeBajaAfiliado(int afiliadoId, String motivo) {
        String sql = """
            UPDATE afiliados 
            SET estado = 'INACTIVO', 
                fecha_baja = ?, 
                motivo_baja = ? 
            WHERE id = ?
        """;

        try (Connection conn = Database.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Registrar la fecha actual como fecha efectiva de baja
            pstmt.setString(1, LocalDate.now().toString());
            pstmt.setString(2, motivo);
            pstmt.setInt(3, afiliadoId);

            int filasAfectadas = pstmt.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

}

