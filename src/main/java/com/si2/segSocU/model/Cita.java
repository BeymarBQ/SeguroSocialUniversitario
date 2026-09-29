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