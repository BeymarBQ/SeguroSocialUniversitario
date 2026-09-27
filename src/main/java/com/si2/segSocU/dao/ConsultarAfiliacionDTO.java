package com.si2.segSocU.model;

// TASK 1: Estructura de datos identificados para la consulta
public class ConsultaAfiliacionDTO {

    private String nombreCompleto;
    private String documentoIdentidad;
    private String codigoUniversitario;
    private String carrera;
    private String estadoAfiliacion; // Ej: ACTIVO, INACTIVO, PENDIENTE
    private boolean habilitadoAtencion; // true: Sí tiene seguro activo / false: No
    private String vigencia; // Ej: Gestión 2/2026

    // Constructor vacío
    public ConsultaAfiliacionDTO() {}

    // Getters y Setters para transportar los datos a la vista
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public String getCodigoUniversitario() { return codigoUniversitario; }
    public void setCodigoUniversitario(String codigoUniversitario) { this.codigoUniversitario = codigoUniversitario; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getEstadoAfiliacion() { return estadoAfiliacion; }
    public void setEstadoAfiliacion(String estadoAfiliacion) { this.estadoAfiliacion = estadoAfiliacion; }

    public boolean isHabilitadoAtencion() { return habilitadoAtencion; }
    public void setHabilitadoAtencion(boolean habilitadoAtencion) { this.habilitadoAtencion = habilitadoAtencion; }

    public String getVigencia() { return vigencia; }
    public void setVigencia(String vigencia) { this.vigencia = vigencia;
    }
    
}