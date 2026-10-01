
package com.si2.segSocU.model;

public class ActualizarEstadoCita {

    // T12: Implementar actualización del estado de una cita
    public static void actualizarEstado(Cita cita, String nuevoEstado) {

        if (cita != null && nuevoEstado != null && !nuevoEstado.trim().isEmpty()) {
            cita.setEstado(nuevoEstado);
        }
    }
}