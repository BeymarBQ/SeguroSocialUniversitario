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