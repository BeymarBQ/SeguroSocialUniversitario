public boolean horarioOcupado(String fecha, String hora) {

    String sql = """
        SELECT COUNT(*)
        FROM citas
        WHERE fecha = ?
        AND hora = ?
        AND estado != 'CANCELADA'
        """;

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setString(1, fecha);
        ps.setString(2, hora);

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            return rs.getInt(1) > 0;
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }

    return false;
}

ç

public boolean registrarCita(Cita cita) {

    String sql = """
        INSERT INTO citas
        (estudiante_id, servicio_id, fecha, hora, estado)
        VALUES (?, ?, ?, ?, ?)
        """;

    try (Connection conn = Database.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        ps.setInt(1, cita.getEstudianteId());
        ps.setInt(2, cita.getServicioId());
        ps.setString(3, cita.getFecha());
        ps.setString(4, cita.getHora());
        ps.setString(5, cita.getEstado());

        return ps.executeUpdate() > 0;

    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}
 boolean actualizarEstadoCita(int citaId, String nuevoEstado, String observacion) {
    String sql = "UPDATE citas SET estado = ? WHERE id = ?";
    try (Connection conn = Database.getConnection()) {
        conn.setAutoCommit(false);
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nuevoEstado);
            pstmt.setInt(2, citaId);
            pstmt.executeUpdate();

            registrarCambio(conn, citaId, nuevoEstado, "Estado actualizado a: " + nuevoEstado + ". " + observacion);

            conn.commit();
            return true;
        } catch (SQLException e) {
            conn.rollback();
            e.printStackTrace();
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

private void registrarCambio(Connection conn, int citaId, String accion, String detalles) throws SQLException {
    String sql = "INSERT INTO historial_citas(cita_id, accion, detalles, fecha_registro) VALUES(?,?,?,?)";
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, citaId);
        pstmt.setString(2, accion);
        pstmt.setString(3, detalles);
        pstmt.setString(4, LocalDateTime.now().toString());
        pstmt.executeUpdate();
    }
}

public List<Cita> listarCitas() {
    List<Cita> lista = new ArrayList<>();
    String sql = """
            SELECT c.id, c.afiliado_id, c.disponibilidad_id, c.estado, c.motivo,
                   (a.nombres || ' ' || a.apellidos) AS estudiante,
                   (d.fecha || ' ' || d.hora || ' - ' || d.medico) AS horario
            FROM citas c
            JOIN afiliados a ON c.afiliado_id = a.id
            JOIN disponibilidad d ON c.disponibilidad_id = d.id
        """;
    try (Connection conn = Database.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {
        while (rs.next()) {
            Cita c = new Cita(
                    rs.getInt("id"), rs.getInt("afiliado_id"),
                    rs.getInt("disponibilidad_id"), rs.getString("estado"),
                    rs.getString("motivo")
            );
            c.setNombreAfiliado(rs.getString("estudiante"));
            c.setDetalleHorario(rs.getString("horario"));
            lista.add(c);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}
}