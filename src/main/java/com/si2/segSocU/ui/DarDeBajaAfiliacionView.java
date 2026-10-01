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

import java.time.LocalDate;
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
        public class DarBajaAfiliacionView {

            private AfiliadoDAO afiliadoDAO = new AfiliadoDAO();

            // TASK 05: Validar que la afiliación esté activa
            public boolean validarAfiliacionActiva(Afiliado afiliado) {
                if (afiliado == null || !"ACTIVO".equalsIgnoreCase(afiliado.getEstado())) {
                    System.out.println("Error: La afiliación no está activa.");
                    return false;
                }
                return true;
            }

            // TASK 06: Consultar las atenciones médicas pendientes
            public boolean tieneAtencionesPendientes(String codigoEstudiante) {
                int pendientes = afiliadoDAO.consultarAtencionesPendientes(codigoEstudiante);
                if (pendientes > 0) {
                    System.out.println("No se puede dar de baja. Tiene " + pendientes + " atenciones/citas pendientes.");
                    return true;
                }
                return false;
            }
        }
    }
}

