package com.si2.segSocU.model;

public class ValidarAfiliacion {

    // Task 8: Validar la consulta de afiliación
    public boolean validarConsulta(String registroUniversitario) {

        if (registroUniversitario == null) {
            System.out.println("El registro universitario es obligatorio.");
            return false;
        }

        if (registroUniversitario.trim().isEmpty()) {
            System.out.println("Debe ingresar el registro universitario.");
            return false;
        }

        System.out.println("Consulta de afiliación válida.");
        return true;
    }
}