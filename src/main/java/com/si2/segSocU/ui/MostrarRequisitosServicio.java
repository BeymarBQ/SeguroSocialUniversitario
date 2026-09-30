package com.si2.segSocU.ui;

import com.si2.segSocU.model.ServicioMedico;

public class MostrarRequisitosServicio {

    // Task 06: Mostrar los requisitos para acceder a cada servicio
    public String mostrarRequisitos(ServicioMedico servicio) {

        if (servicio == null) {
            return "No se encontró el servicio.";
        }

        String requisitos = servicio.getRequisitos();

        if (requisitos == null || requisitos.trim().isEmpty()) {
            return "Este servicio no tiene requisitos registrados.";
        }

        return requisitos;
    }
}