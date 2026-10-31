package com.si2.segSocU.model;

import java.time.LocalDate;

public class RegistroAtencion {

    private LocalDate fechaAtencion;

    // T5 - Implementar registro de la fecha de atención
    public void registrarFechaAtencion(LocalDate fechaAtencion) {
        if (fechaAtencion != null) {
            this.fechaAtencion = fechaAtencion;
        }
    }

    public LocalDate getFechaAtencion() {
        return fechaAtencion;
    }
}
