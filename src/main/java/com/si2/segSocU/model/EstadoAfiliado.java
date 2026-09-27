package com.si2.segsocu.model;

public class VerificarEstado {
    private boolean habilitado;
    private String mensaje;

    public VerificarEstado(Afiliado afiliado) {
        if (afiliado == null) {
            this.habilitado = false;
            this.mensaje = "Afiliado no encontrado.";
        } else if ("ACTIVO".equalsIgnoreCase(afiliado.getEstado())) {
            this.habilitado = true;
            this.mensaje = "HABILITADO PARA ATENCIÓN MÉDICA";
        } else {
            this.habilitado = false;
            this.mensaje = "NO HABILITADO (Estado actual: " + afiliado.getEstado() + ")";
        }
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public String getMensaje() {
        return mensaje;
    }
}