package com.si2.segSocU.model;

public class ActualizarEstadoCita {

    private String estado;

    // T12: Implementar actualización del estado de una cita
    public boolean actualizarEstado(String nuevoEstado) {

        if (nuevoEstado == null || nuevoEstado.trim().isEmpty()) {
            System.out.println("El estado de la cita no puede estar vacío.");
            return false;
        }

        this.estado = nuevoEstado.trim().toUpperCase();

        System.out.println("Estado de la cita actualizado a: " + estado);
        return true;
    }

    public String getEstado() {
        return estado;
    }
}