
package com.si2.segSocU.model;

public class RegistroMotivoBaja {

    private String motivoBaja;

    // Task 03: Implementar registro del motivo de la baja
    public boolean registrarMotivoBaja(String motivo) {

        if (motivo == null || motivo.trim().isEmpty()) {
            System.out.println("El motivo de la baja es obligatorio.");
            return false;
        }

        this.motivoBaja = motivo.trim();

        System.out.println("Motivo de baja registrado correctamente.");
        return true;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }
}