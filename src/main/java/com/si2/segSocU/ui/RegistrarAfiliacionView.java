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
    // TASK 3: CREAR CAMPOS DEL FORMULARIO

    private TextField txtNombre = new TextField();
    private TextField txtDocumento = new TextField();
    private ComboBox<String> cbNacionalidad = new ComboBox<>();
    private TextField txtCodigoUniv = new TextField();
    private TextField txtContacto = new TextField(); // Teléfono / WhatsApp

    public Node build() {
        VBox root = new VBox(15);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Label titulo = new Label("Formulario de Afiliación - SSU UMSS");
        titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Opciones para el ComboBox de Nacionalidad
        cbNacionalidad.getItems().addAll("Argentina", "Brasil", "Chile", "Colombia", "Perú", "Otra");
        cbNacionalidad.setPromptText("Seleccione nacionalidad");

        // Placeholders (mensajes de ayuda dentro del campo)
        txtNombre.setPromptText("Ej. Juan Pérez");
        txtDocumento.setPromptText("Ej. E-12345678");
        txtCodigoUniv.setPromptText("Ej. 202401234");
        txtContacto.setPromptText("Ej. 71234567");

        // Diseño en cuadrícula (GridPane) para organizar los controles
        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setAlignment(Pos.CENTER);

        form.add(new Label("Nombre Completo *:"), 0, 0);
        form.add(txtNombre, 1, 0);

        form.add(new Label("N° Documento / Pasaporte *:"), 0, 1);
        form.add(txtDocumento, 1, 1);

        form.add(new Label("Nacionalidad *:"), 0, 2);
        form.add(cbNacionalidad, 1, 2);

        form.add(new Label("Código Universitario / Matrícula *:"), 0, 3);
        form.add(txtCodigoUniv, 1, 3);

        form.add(new Label("Contacto / Teléfono *:"), 0, 4);
        form.add(txtContacto, 1, 4);

        Button btnGuardar = new Button("Guardar Afiliación");
        btnGuardar.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-weight: bold;");

        // Evento al presionar el botón
        btnGuardar.setOnAction(e -> {
            // Executar Task 4 antes de guardar
            if (!validarCampos()) {
                return; // Si no pasa las validaciones, se detiene
            }
            // Si pasa la validación, procedes a guardar
            Afiliado a = new Afiliado();
            a.setNombres(txtNombre.getText().trim());
            a.setRegistro(txtDocumento.getText().trim());
            a.setTelefono(txtContacto.getText().trim());
            a.setFechaAfiliacion(LocalDate.now());
            limpiarCampos();
        });

        root.getChildren().addAll(titulo, form, btnGuardar);
        return root;
    }
    // TASK 4: IMPLEMENTAR VALIDACIÓN DE DATOS
    private boolean validarCampos() {
        // 1. Validar Nombre (No vacío y al menos 3 caracteres)
        if (txtNombre.getText().trim().isEmpty() || txtNombre.getText().trim().length() < 3) {
            mostrarError("El campo 'Nombre Completo' es obligatorio y debe tener al menos 3 letras.");
            txtNombre.requestFocus();
            return false;
        }
        // 2. Validar Documento / Pasaporte
        if (txtDocumento.getText().trim().isEmpty()) {
            mostrarError("El 'N° Documento / Pasaporte' es obligatorio.");
            txtDocumento.requestFocus();
            return false;
        }
        // 3. Validar Selección de Nacionalidad
        if (cbNacionalidad.getValue() == null) {
            mostrarError("Debe seleccionar una 'Nacionalidad'.");
            cbNacionalidad.requestFocus();
            return false;
        }
        // 4. Validar Código Universitario (solo números, por ejemplo 9 dígitos)
        String codigo = txtCodigoUniv.getText().trim();
        if (codigo.isEmpty() || !codigo.matches("^[0-9]{5,10}$")) {
            mostrarError("El 'Código Universitario' debe contener entre 5 y 10 dígitos numéricos.");
            txtCodigoUniv.requestFocus();
            return false;
        }
        // 5. Validar Contacto (solo números, entre 7 y 12 dígitos)
        String contacto = txtContacto.getText().trim();
        if (contacto.isEmpty() || !contacto.matches("^[0-9]{7,12}$")) {
            mostrarError("El número de 'Contacto' debe contener entre 7 y 12 dígitos numéricos.");
            txtContacto.requestFocus();
            return false;
        }
        return true; // Todos los campos son válidos
    }
    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error de Validación");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
    private void limpiarCampos() {
        txtNombre.clear();
        txtDocumento.clear();
        cbNacionalidad.setValue(null);
        txtCodigoUniv.clear();
        txtContacto.clear();
    }
}
}
