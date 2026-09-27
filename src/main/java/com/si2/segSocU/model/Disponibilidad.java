package com.si2.segsocu.model;

public class Disponibilidad {
    private int id;
    private String medico;
    private String especialidad;
    private String fecha;
    private String hora;
    private boolean disponible;

    public Disponibilidad(int id, String medico, String especialidad, String fecha, String hora, boolean disponible) {
        this.id = id;
        this.medico = medico;
        this.especialidad = especialidad;
        this.fecha = fecha;
        this.hora = hora;
        this.disponible = disponible;
    }

    public int getId() { return id; }
    public String getMedico() { return medico; }
    public String getEspecialidad() { return especialidad; }
    public String getFecha() { return fecha; }
    public String getHora() { return hora; }
    public boolean isDisponible() { return disponible; }

    @Override
    public String toString() {
        return fecha + " " + hora + " - " + medico + " (" + especialidad + ")";
    }
}