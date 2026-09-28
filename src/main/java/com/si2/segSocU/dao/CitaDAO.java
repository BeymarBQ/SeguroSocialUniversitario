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