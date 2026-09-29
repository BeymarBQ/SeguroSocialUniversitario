package com.si2.segSocU.dao;

import com.si2.segSocU.db.Database;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CitaDAO {

    // TASK 01: Identificar las citas que pueden ser canceladas (Estado PENDIENTE/RESERVADA)
    public List<String> obtenerCitasCancelables(String codigoEstudiante) {
        List<String> citasCancelables = new ArrayList<>();
        String sql = "SELECT id, servicio, fecha FROM citas WHERE estudiante_codigo = ? AND estado IN ('PENDIENTE', 'RESERVADA')";

        try (Connection conn = Database.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, codigoEstudiante);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                citasCancelables.add("Cita #" + rs.getInt("id") + " - " + rs.getString("servicio") + " (" + rs.getDate("fecha") + ")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return citasCancelables;
    }
}