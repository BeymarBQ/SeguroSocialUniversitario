package com.si2.segsocu.model;

public class Disponibilidad {
    private int id;
    private int medicoId;
    private String medicoNombres;
    private String fecha;
    private String hora;
    private int cupos;

    public Disponibilidad() {}

    public Disponibilidad(int id, int medicoId, String medicoNombres, String fecha, String hora, int cupos) {
        this.id = id;
        this.medicoId = medicoId;
        this.medicoNombres = medicoNombres;
        this.fecha = fecha;
        this.hora = hora;
        this.cupos = cupos;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getMedicoId() { return medicoId; }
    public void setMedicoId(int medicoId) { this.medicoId = medicoId; }

    public String getMedicoNombres() { return medicoNombres; }
    public void setMedicoNombres(String medicoNombres) { this.medicoNombres = medicoNombres; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }

    public int getCupos() { return cupos; }
    public void setCupos(int cupos) { this.cupos = cupos; }
}
