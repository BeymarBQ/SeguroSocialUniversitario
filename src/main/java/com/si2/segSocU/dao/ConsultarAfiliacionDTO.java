package com.si2.segSocU.model;

// TASK 1: Estructura de datos identificados para la consulta
public class ConsultaAfiliacionDTO {

    private String nombreCompleto;
    private String documentoIdentidad;
    private String codigoUniversitario;
    private String carrera;
    private String estadoAfiliacion; // Ej: ACTIVO, INACTIVO, PENDIENTE
    private boolean habilitadoAtencion; // true: Sí tiene seguro activo / false: No
    private String vigencia; // Ej: Gestión 2/2026

    // Constructor vacío
    public ConsultaAfiliacionDTO() {}

    // Getters y Setters para transportar los datos a la vista
    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getDocumentoIdentidad() { return documentoIdentidad; }
    public void setDocumentoIdentidad(String documentoIdentidad) { this.documentoIdentidad = documentoIdentidad; }

    public String getCodigoUniversitario() { return codigoUniversitario; }
    public void setCodigoUniversitario(String codigoUniversitario) { this.codigoUniversitario = codigoUniversitario; }

    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getEstadoAfiliacion() { return estadoAfiliacion; }
    public void setEstadoAfiliacion(String estadoAfiliacion) { this.estadoAfiliacion = estadoAfiliacion; }

    public boolean isHabilitadoAtencion() { return habilitadoAtencion; }
    public void setHabilitadoAtencion(boolean habilitadoAtencion) { this.habilitadoAtencion = habilitadoAtencion; }

    public String getVigencia() { return vigencia; }
    public void setVigencia(String vigencia) { this.vigencia = vigencia;
    }
    package com.si2.segSocU.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

    public class ConsultarAfiliacionView {

        // Controles para el Task 2 (Diseño UI)
        private TextField txtBuscarCodigo = new TextField();
        private Button btnBuscar = new Button("Consultar");

        // Etiquetas para desplegar los datos identificados en el Task 1
        private Label lblNombre = new Label("-");
        private Label lblDocumento = new Label("-");
        private Label lblCodigoUniv = new Label("-");
        private Label lblCarrera = new Label("-");
        private Label lblEstadoAfiliacion = new Label("-");
        private Label lblHabilitadoAtencion = new Label("-");
        private Label lblVigencia = new Label("-");

        public Node build() {
            VBox root = new VBox(15);
            root.setPadding(new Insets(20));
            root.setAlignment(Pos.TOP_CENTER);

            Label titulo = new Label("Consulta de Afiliación - SSU UMSS");
            titulo.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

            // --- 1. SECCIÓN DE BÚSQUEDA ---
            HBox boxBusqueda = new HBox(10);
            boxBusqueda.setAlignment(Pos.CENTER);

            txtBuscarCodigo.setPromptText("Ingrese Matrícula o C.I.");
            txtBuscarCodigo.setPrefWidth(220);
            btnBuscar.setStyle("-fx-background-color: #0056b3; -fx-text-fill: white; -fx-font-weight: bold;");

            boxBusqueda.getChildren().addAll(new Label("Matrícula / C.I.:"), txtBuscarCodigo, btnBuscar);

            // --- 2. SECCIÓN DE RESULTADOS DE AFILIACIÓN ---
            GridPane formResultados = new GridPane();
            formResultados.setHgap(15);
            formResultados.setVgap(12);
            formResultados.setAlignment(Pos.CENTER);
            formResultados.setStyle("-fx-background-color: #f8f9fa; -fx-padding: 20; -fx-border-color: #dddddd; -fx-border-radius: 5;");

            // Fila 0: Nombre
            formResultados.add(new Label("Estudiante:"), 0, 0);
            formResultados.add(lblNombre, 1, 0);

            // Fila 1: Documento
            formResultados.add(new Label("N° Documento / Pasaporte:"), 0, 1);
            formResultados.add(lblDocumento, 1, 1);

            // Fila 2: Código / Matrícula
            formResultados.add(new Label("Código Universitario:"), 0, 2);
            formResultados.add(lblCodigoUniv, 1, 2);

            // Fila 3: Carrera
            formResultados.add(new Label("Carrera / Facultad:"), 0, 3);
            formResultados.add(lblCarrera, 1, 3);

            // Fila 4: Estado
            formResultados.add(new Label("Estado de Afiliación:"), 0, 4);
            formResultados.add(lblEstadoAfiliacion, 1, 4);

            // Fila 5: Habilitación Médica
            formResultados.add(new Label("¿Habilitado para Atención Médica?:"), 0, 5);
            formResultados.add(lblHabilitadoAtencion, 1, 5);

            // Fila 6: Vigencia
            formResultados.add(new Label("Vigencia de Cobertura:"), 0, 6);
            formResultados.add(lblVigencia, 1, 6);

            root.getChildren().addAll(titulo, boxBusqueda, new Separator(), formResultados);
            return root;
        }
    }
}