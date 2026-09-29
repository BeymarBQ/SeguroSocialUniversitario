package com.si2.segsocu.ui;

import com.si2.segsocu.dao.AfiliadoDAO;
import com.si2.segsocu.model.Afiliado;
import com.si2.segsocu.util.AlertUtil;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.Optional;

/**
 * Historia 16: Dar de baja la afiliación (Gestión Admin).
 * Proceso administrativo para cancelar la cobertura al egresar.
 * (También permite reactivar, útil para las pruebas de la demo.)
 */
public class DarDeBajaAfiliacionView {

    private final AfiliadoDAO afiliadoDAO = new AfiliadoDAO();
    private Afiliado actual;

    public Node build() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));

        Label titulo = new Label("Dar de baja la afiliación");
        titulo.getStyleClass().add("titulo-vista");

        TextField txtRegistro = new TextField();
        txtRegistro.setPromptText("Registro universitario (ej: 2019-09876)");
        Button btnBuscar = new Button("Buscar");
        HBox buscador = new HBox(8, txtRegistro, btnBuscar);

        GridPane info = new GridPane();
        info.setHgap(12);
        info.setVgap(10);
        info.setPadding(new Insets(16, 0, 16, 0));
        info.setVisible(false);

        Label lblNombreValor = new Label("-");
        Label lblEstadoValor = new Label("-");
        info.addRow(0, new Label("Afiliado:"), lblNombreValor);
        info.addRow(1, new Label("Estado actual:"), lblEstadoValor);

        Button btnCambiar = new Button();
        btnCambiar.setVisible(false);

        btnBuscar.setDefaultButton(true);
        btnBuscar.setOnAction(e -> {
            String registro = txtRegistro.getText().trim();
            if (registro.isEmpty()) return;
            Optional<Afiliado> encontrado = afiliadoDAO.buscarPorRegistro(registro);
            if (encontrado.isEmpty()) {
                AlertUtil.error("No encontrado", "No existe ningún afiliado con ese registro universitario.");
                info.setVisible(false);
                btnCambiar.setVisible(false);
                return;
            }
            actual = encontrado.get();
            actualizarVista(actual, lblNombreValor, lblEstadoValor, btnCambiar);
            info.setVisible(true);
        });

        btnCambiar.setOnAction(e -> {
            if (actual == null) return;
            boolean activo = "ACTIVO".equalsIgnoreCase(actual.getEstado());
            String nuevoEstado = activo ? "INACTIVO" : "ACTIVO";
            String accion = activo ? "dar de baja la afiliación de" : "reactivar la afiliación de";
            if (!AlertUtil.confirmar("Confirmar", "¿Deseas " + accion + " " + actual.getNombreCompleto() + "?")) {
                return;
            }
            afiliadoDAO.cambiarEstado(actual.getId(), nuevoEstado);
            actual.setEstado(nuevoEstado);
            actualizarVista(actual, lblNombreValor, lblEstadoValor, btnCambiar);
            AlertUtil.info("Listo", "El nuevo estado de la afiliación es: " + nuevoEstado);
        });

        root.getChildren().addAll(titulo, buscador, info, btnCambiar);
        return root;
    }

    private void actualizarVista(Afiliado a, Label lblNombreValor, Label lblEstadoValor, Button btnCambiar) {
        lblNombreValor.setText(a.getNombreCompleto() + " (" + a.getRol() + ")");
        boolean activo = "ACTIVO".equalsIgnoreCase(a.getEstado());
        lblEstadoValor.setText(activo ? "ACTIVO ✔" : "INACTIVO ✘");
        lblEstadoValor.setTextFill(activo ? Color.web("#1e8e3e") : Color.web("#d93025"));
        btnCambiar.setText(activo ? "Dar de baja" : "Reactivar afiliación");
        btnCambiar.setVisible(true);
    }
}
