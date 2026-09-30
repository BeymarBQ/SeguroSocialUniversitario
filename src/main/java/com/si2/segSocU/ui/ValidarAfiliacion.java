package com.si2.segSocU.ui;

import javafx.scene.control.Alert;

public class ValidarAfiliacion {

    public static boolean validarConsulta(String registroUniversitario) {

        // 1. Verificar que el registro no sea nulo
        if (registroUniversitario == null) {
            mostrarError("Debe ingresar un registro universitario.");
            return false;
        }

        // Quitar espacios al inicio y al final
        String registro = registroUniversitario.trim();

        // 2. Verificar que no esté vacío
        if (registro.isEmpty()) {
            mostrarError("El campo de registro universitario es obligatorio.");
            return false;
        }

        // 3. Verificar una longitud mínima
        if (registro.length() < 4) {
            mostrarError("El registro universitario ingresado no es válido.");
            return false;
        }

        // 4. Si pasó todas las validaciones
        return true;
    }

    /**
     * Muestra el mensaje cuando la validación falla.
     */
    private static void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle("Validar consulta de afiliación");
        alerta.setHeaderText("Datos incorrectos");
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }
}
