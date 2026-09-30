package com.si2.segsocu.model;

public class ServicioMedico {
    private int id;
    private String nombre;
    private String especialidad;
    private String descripcion;
    private String horarioAtencion;
    private String ubicacion; // ej. Bloque A - Consultorio 102

    public ServicioMedico(int id, String nombre, String especialidad, String descripcion, String horarioAtencion, String ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.especialidad = especialidad;
        this.descripcion = descripcion;
        this.horarioAtencion = horarioAtencion;
        this.ubicacion = ubicacion;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getEspecialidad() { return especialidad; }
    public String getDescripcion() { return descripcion; }
    public String getHorarioAtencion() { return horarioAtencion; }
    public String getUbicacion() { return ubicacion; }
}