public class Cita {

    private int estudianteId;
    private int servicioId;
    private String fecha;
    private String hora;
    private String estado;

    public Cita(int estudianteId, int servicioId,
                String fecha, String hora, String estado) {
        this.estudianteId = estudianteId;
        this.servicioId = servicioId;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

    public int getServicioId() {
        return servicioId;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }
}


// task 9 registrar una atencion medica
package model;

public class Atencion {
    private int id;
    private int citaId;
    private String motivo;
    private String diagnostico;
    private String indicaciones;
    private String fecha;

    public Atencion() {}

    public Atencion(int citaId, String motivo, String diagnostico, String indicaciones, String fecha) {
        this.citaId = citaId;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.indicaciones = indicaciones;
        this.fecha = fecha;
    }

    public Atencion(int id, int citaId, String motivo, String diagnostico, String indicaciones, String fecha) {
        this.id = id;
        this.citaId = citaId;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.indicaciones = indicaciones;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCitaId() { return citaId; }
    public void setCitaId(int citaId) { this.citaId = citaId; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}


//task dia lunes N° 10
package dao;

import db.Database;
import model.Atencion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AtencionDAO {

    /**
     * Tarea 10: Inserta el registro de atención médica en la base de datos.
     * Retorna el ID generado para la atención registrada.
     */
    public int insertar(Atencion atencion) throws SQLException {
        String sql = "INSERT INTO atencion (cita_id, motivo, diagnostico, indicaciones, fecha) VALUES (?, ?, ?, ?, ?)";

        Connection conn = Database.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, atencion.getCitaId());
            stmt.setString(2, atencion.getMotivo());
            stmt.setString(3, atencion.getDiagnostico());
            stmt.setString(4, atencion.getIndicaciones());
            stmt.setString(5, atencion.getFecha());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }
}