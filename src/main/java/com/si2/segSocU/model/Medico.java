package com.si2.segsocu.model;

public class Medico {
    private int id;
    private String nombres;
    private int especialidadId;
    private String especialidadNombre; // para mostrar en tablas/combos sin otro query

    public Medico() {}

    public Medico(int id, String nombres, int especialidadId, String especialidadNombre) {
        this.id = id;
        this.nombres = nombres;
        this.especialidadId = especialidadId;
        this.especialidadNombre = especialidadNombre;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public int getEspecialidadId() { return especialidadId; }
    public void setEspecialidadId(int especialidadId) { this.especialidadId = especialidadId; }

    public String getEspecialidadNombre() { return especialidadNombre; }
    public void setEspecialidadNombre(String especialidadNombre) { this.especialidadNombre = especialidadNombre; }

    @Override
    public String toString() { return nombres + " (" + especialidadNombre + ")"; }
}
