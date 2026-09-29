package com.si2.segsocu.util;

import javafx.scene.control.Alert;

/**
 * Helper para no repetir el mismo boilerplate de Alert en cada pantalla.
 * Usar siempre esta clase para mensajes de error/exito/confirmacion.
 */
public class AlertUtil {

    private AlertUtil() {}

    public static void info(String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }

    public static void error(String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.ERROR);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        a.showAndWait();
    }

    public static boolean confirmar(String titulo, String mensaje) {
        Alert a = new Alert(Alert.AlertType.CONFIRMATION);
        a.setTitle(titulo);
        a.setHeaderText(null);
        a.setContentText(mensaje);
        return a.showAndWait().filter(b -> b.getButtonData().isDefaultButton()).isPresent();
    }
}
