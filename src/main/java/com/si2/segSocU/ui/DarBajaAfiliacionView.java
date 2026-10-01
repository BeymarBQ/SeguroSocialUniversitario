package com.si2.segsocu.ui;

import com.si2.segsocu.dao.AfiliadoDAO;
import com.si2.segsocu.model.Afiliado;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.time.LocalDate;

public class DarDeBajaAfiliacionView {

    private final AfiliadoDAO afiliadoDAO = new AfiliadoDAO();
    private Afiliado afiliadoSeleccionado = null;

    public VBox getVista() {
        VBox layout = new VBox(15);
        layout.setPadding(new Insets(15));

        Label lblTitulo = new Label("Proceso de Baja de Afiliación");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 16));

        // 1. Buscador de Afiliado
        HBox searchBox = new HBox(10);
        TextField txtCI = new TextField();
        txtCI.setPromptText("Ingrese CI o Registro Univ.");
        Button btnBuscar = new Button("Buscar Afiliado");
        searchBox.getChildren().addAll(txtCI, btnBuscar);

        // 2. Detalle del Afiliado
        GridPane gridInfo = new GridPane();
        gridInfo.setHgap(15);
        gridInfo.setVgap(8);
        gridInfo.setPadding(new Insets(10));
        gridInfo.setStyle("-fx-background-color: #f8f9fa; -fx-background-radius: 5;");

        Label lblNombre = new Label("-");
        Label lblRol = new Label("-");
        Label lblEstado = new Label("-");

        gridInfo.add(new Label("Nombre Completo:"), 0, 0);
        gridInfo.add(lblNombre, 1, 0);
        gridInfo.add(new Label("Rol:"), 0, 1);
        gridInfo.add(lblRol, 1, 1);
        gridInfo.add(new Label("Estado Actual:"), 2, 0);
        gridInfo.add(lblEstado, 3, 0);

        // 3. Formulario para la Baja
        Label lblMotivo = new Label("Motivo de la baja:");
        TextArea txtMotivo = new TextArea();
        txtMotivo.setPromptText("Escriba la razón de la baja de afiliación...");
        txtMotivo.setPrefRowCount(3);

        Button btnConfirmarBaja = new Button("Confirmar Baja");
        btnConfirmarBaja.setDisable(true); // Se activa al seleccionar afiliado válido

        Label lblMensaje = new Label();

        // Lógica de búsqueda
        btnBuscar.setOnAction(e -> {
            String criterio = txtCI.getText().trim();
            if (criterio.isEmpty()) {
                lblMensaje.setText("Ingrese un número de documento.");
                lblMensaje.setTextFill(Color.ORANGE);
                return;
            }

            // Asumiendo que tienes un método de búsqueda en tu DAO
            afiliadoSeleccionado = afiliadoDAO.buscarPorDocumento(criterio);

            if (afiliadoSeleccionado != null) {
                lblNombre.setText(afiliadoSeleccionado.getNombreCompleto());
                lblRol.setText(afiliadoSeleccionado.getRol());
                lblEstado.setText(afiliadoSeleccionado.getEstado());

                if ("INACTIVO".equalsIgnoreCase(afiliadoSeleccionado.getEstado())) {
                    lblMensaje.setText("El afiliado ya se encuentra dado de baja.");
                    lblMensaje.setTextFill(Color.RED);
                    btnConfirmarBaja.setDisable(true);
                } else {
                    lblMensaje.setText("Afiliado encontrado. Puede proceder con la evaluación.");
                    lblMensaje.setTextFill(Color.BLUE);
                    btnConfirmarBaja.setDisable(false);
                }
            } else {
                lblNombre.setText("-");
                lblRol.setText("-");
                lblEstado.setText("-");
                btnConfirmarBaja.setDisable(true);
                lblMensaje.setText("No se encontró ningún afiliado con esa identificación.");
                lblMensaje.setTextFill(Color.RED);
            }
        });

        // Lógica de validación y registro de la baja
        btnConfirmarBaja.setOnAction(e -> {
            if (afiliadoSeleccionado == null) return;

            // Tarea 1: Validar que no existan procesos médicos abiertos
            boolean tieneProcesos = afiliadoDAO.tieneProcesosMedicosAbiertos(afiliadoSeleccionado.getId());

            if (tieneProcesos) {
                lblMensaje.setText("No se puede dar de baja: El afiliado tiene citas o procesos médicos pendientes.");
                lblMensaje.setTextFill(Color.RED);
                return;
            }

            String motivo = txtMotivo.getText().trim();
            if (motivo.isEmpty()) {
                lblMensaje.setText("Debe ingresar un motivo para procesar la baja.");
                lblMensaje.setTextFill(Color.ORANGE);
                return;
            }

            // Tarea 08: Registrar la fecha efectiva de la baja y actualizar estado
            boolean ok = afiliadoDAO.darDeBajaAfiliado(afiliadoSeleccionado.getId(), motivo);

            if (ok) {
                String fechaHoy = LocalDate.now().toString();
                lblMensaje.setText("Baja procesada exitosamente. Fecha efectiva registrada: " + fechaHoy);
                lblMensaje.setTextFill(Color.GREEN);
                lblEstado.setText("INACTIVO");
                btnConfirmarBaja.setDisable(true);
                txtMotivo.clear();
            } else {
                lblMensaje.setText("Ocurrió un error al registrar la baja en la base de datos.");
                lblMensaje.setTextFill(Color.RED);
            }
        });

        layout.getChildren().addAll(lblTitulo, searchBox, gridInfo, lblMotivo, txtMotivo, btnConfirmarBaja, lblMensaje);
        return layout;
    }
}