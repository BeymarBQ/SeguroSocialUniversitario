package com.si2.segsocu.ui;

import com.si2.segsocu.dao.AfiliadoDAO;
import com.si2.segsocu.model.Afiliado;
import com.si2.segsocu.util.AlertUtil;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;

/**
 * Historia 1: Registrar datos para la afiliacion (Estudiante).
 * Crea el perfil del afiliado e inicia su expediente en la UMSS.
 */
public class RegistrarAfiliacionView {

    private final AfiliadoDAO afiliadoDAO = new AfiliadoDAO();

    public Node build() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));

        Label titulo = new Label("Registrar datos para la afiliación");
        titulo.getStyleClass().add("titulo-vista");

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);

        TextField txtRegistro = new TextField();
        txtRegistro.setPromptText("Ej: 2023-12345");

        TextField txtNombres = new TextField();
        TextField txtApellidos = new TextField();

        ComboBox<String> cbRol = new ComboBox<>();
        cbRol.getItems().addAll("Estudiante", "Docente", "Extranjero");
        cbRol.getSelectionModel().selectFirst();

        DatePicker dpNacimiento = new DatePicker();

        TextField txtTelefono = new TextField();
        TextField txtEmail = new TextField();
        TextField txtDomicilio = new TextField();

        int row = 0;
        form.addRow(row++, new Label("Registro universitario *"), txtRegistro);
        form.addRow(row++, new Label("Nombres *"), txtNombres);
        form.addRow(row++, new Label("Apellidos *"), txtApellidos);
        form.addRow(row++, new Label("Rol *"), cbRol);
        form.addRow(row++, new Label("Fecha de nacimiento"), dpNacimiento);
        form.addRow(row++, new Label("Teléfono"), txtTelefono);
        form.addRow(row++, new Label("Email"), txtEmail);
        form.addRow(row++, new Label("Domicilio"), txtDomicilio);

        Button btnGuardar = new Button("Registrar afiliación");
        btnGuardar.setDefaultButton(true);

        btnGuardar.setOnAction(e -> {
            String registro = txtRegistro.getText().trim();
            String nombres = txtNombres.getText().trim();
            String apellidos = txtApellidos.getText().trim();

            if (registro.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()) {
                AlertUtil.error("Datos incompletos", "Registro universitario, nombres y apellidos son obligatorios.");
                return;
            }

            if (afiliadoDAO.existeRegistro(registro)) {
                AlertUtil.error("Registro duplicado", "Ya existe un afiliado con ese registro universitario.");
                return;
            }

            Afiliado a = new Afiliado();
            a.setRegistroUniversitario(registro);
            a.setNombres(nombres);
            a.setApellidos(apellidos);
            a.setRol(cbRol.getValue());
            a.setFechaNacimiento(dpNacimiento.getValue() != null ? dpNacimiento.getValue().toString() : null);
            a.setTelefono(txtTelefono.getText().trim());
            a.setEmail(txtEmail.getText().trim());
            a.setDomicilio(txtDomicilio.getText().trim());
            a.setEstado("ACTIVO");
            a.setFechaAfiliacion(LocalDate.now().toString());

            afiliadoDAO.insertar(a);
            AlertUtil.info("Afiliación registrada", "Se creó el expediente de " + a.getNombreCompleto() + " correctamente.");

            txtRegistro.clear();
            txtNombres.clear();
            txtApellidos.clear();
            txtTelefono.clear();
            txtEmail.clear();
            txtDomicilio.clear();
            dpNacimiento.setValue(null);
        });

        root.getChildren().addAll(titulo, form, btnGuardar);
        return root;
    }
}
