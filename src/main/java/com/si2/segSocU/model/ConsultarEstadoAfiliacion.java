package com.si2.segSocU.model;

public class ConsultarEstadoAfiliacion {

    // Task 04: Consultar el estado de afiliación del estudiante
    public String consultarEstado(Afiliado afiliado) {

        if (afiliado == null) {
            return "AFILIADO NO ENCONTRADO";
        }

        String estado = afiliado.getEstado();

        if (estado == null || estado.trim().isEmpty()) {
            return "ESTADO NO REGISTRADO";
        }

        return estado;
    }

    public boolean estaActivo(Afiliado afiliado) {

        if (afiliado == null || afiliado.getEstado() == null) {
            return false;
        }

        return afiliado.getEstado().equalsIgnoreCase("ACTIVO");
    }
}