package com.si2.segSocU.model;

public class MotivoConsulta {

    private String motivo;

    // Task 6: Implementar campo para el motivo de consulta
    public boolean registrarMotivo(String motivo) {

        if (motivo == null || motivo.trim().isEmpty()) {
            System.out.println("El motivo de consulta es obligatorio.");
            return false;
        }

        this.motivo = motivo.trim();

        System.out.println("Motivo de consulta registrado correctamente.");
        return true;
    }

    public String getMotivo() {
        return motivo;
    }
}