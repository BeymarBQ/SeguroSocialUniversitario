package com.si2.segsocu.ui;

import com.si2.segsocu.dao.AfiliadoDAO;
import com.si2.segsocu.dao.CitaDAO;
import com.si2.segsocu.db.Database;
import com.si2.segsocu.model.Afiliado;
import com.si2.segsocu.model.Cita;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.util.List;

public class App extends Application {

    private final CitaDAO citaDAO = new CitaDAO();
    private final AfiliadoDAO afiliadoDAO = new AfiliadoDAO();

    @Override
    public void start(Stage stage) {
        Database.initDatabase();

        TabPane tabPane = new TabPane();

        // Pestaña: Consultar Citas Programadas
        Tab tabCitasProgramadas = new Tab("Citas Programadas", crearVistaCitasProgramadas());

        tabPane.getTabs().add(tabCitasProgramadas);
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);

        Scene scene = new Scene(tabPane, 780, 500);
        stage.setTitle("Seguro Universitario - Consulta de Citas Programadas");
        stage.setScene(scene);
        stage.show();
    }

    // Diseñar la pantalla de citas programadas y asociar citas con el estudiante
    private VBox crearVistaCitasProgramadas() {
        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(15));

        Label lblTitulo = new Label("Consulta de Citas Programadas por Estudiante");
        lblTitulo.setFont(Font.font("System", FontWeight.BOLD, 16));

        // Formulario de Búsqueda
        HBox searchBox = new HBox(10);
        TextField txtRU = new TextField();
        txtRU.setPromptText("Ingrese Registro Universitario");
        Button btnBuscar = new Button("Consultar Citas");
        searchBox.getChildren().addAll(txtRU, btnBuscar);

        // Panel de información asociada del Estudiante
        GridPane infoEstudianteGrid = new GridPane();
        infoEstudianteGrid.setHgap(15);
        infoEstudianteGrid.setVgap(8);
        infoEstudianteGrid.setPadding(new Insets(10));
        infoEstudianteGrid.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 5;");

        Label lblNombreValor = new Label("-");
        Label lblRolValor = new Label("-");
        Label lblEstadoValor = new Label("-");

        infoEstudianteGrid.add(new Label("Estudiante:"), 0, 0);
        infoEstudianteGrid.add(lblNombreValor, 1, 0);
        infoEstudianteGrid.add(new Label("Rol:"), 0, 1);
        infoEstudianteGrid.add(lblRolValor, 1, 1);
        infoEstudianteGrid.add(new Label("Estado Afiliación:"), 2, 0);
        infoEstudianteGrid.add(lblEstadoValor, 3, 0);

        // Tabla de Citas Programadas
        TableView<Cita> tablaCitas = new TableView<>();

        TableColumn<Cita, String> colHorario = new TableColumn<>("Fecha, Hora y Médico");
        colHorario.setCellValueFactory(new PropertyValueFactory<>("detalleHorario"));
        colHorario.setPrefWidth(350);

        TableColumn<Cita, String> colMotivo = new TableColumn<>("Motivo");
        colMotivo.setCellValueFactory(new PropertyValueFactory<>("motivo"));
        colMotivo.setPrefWidth(220);

        TableColumn<Cita, String> colEstado = new TableColumn<>("Estado Cita");
        colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));
        colEstado.setPrefWidth(120);

        tablaCitas.getColumns().addAll(colHorario, colMotivo, colEstado);

        Label lblMensaje = new Label();

        // Acción al consultar
        btnBuscar.setOnAction(e -> {
            String ru = txtRU.getText().trim();
            if (ru.isEmpty()) {
                lblMensaje.setText("Por favor ingrese un Registro Universitario.");
                lblMensaje.setTextFill(Color.ORANGE);
                return;
            }

            // 1. Asociar información del estudiante
            Afiliado estudiante = afiliadoDAO.buscarPorRegistroUniversitario(ru);

            if (estudiante != null) {
                lblNombreValor.setText(estudiante.getNombreCompleto());
                lblRolValor.setText(estudiante.getRol());
                lblEstadoValor.setText(estudiante.getEstado());

                // 2. Cargar sus citas pendientes programadas
                List<Cita> citasProgramadas = citaDAO.buscarCitasProgramadasPorEstudiante(ru);
                tablaCitas.setItems(FXCollections.observableArrayList(citasProgramadas));

                if (citasProgramadas.isEmpty()) {
                    lblMensaje.setText("El estudiante no tiene citas pendientes programadas.");
                    lblMensaje.setTextFill(Color.BLUE);
                } else {
                    lblMensaje.setText("Se encontraron " + citasProgramadas.size() + " cita(s) programada(s).");
                    lblMensaje.setTextFill(Color.GREEN);
                }
            } else {
                lblNombreValor.setText("-");
                lblRolValor.setText("-");
                lblEstadoValor.setText("-");
                tablaCitas.getItems().clear();
                lblMensaje.setText("Estudiante no registrado en el sistema.");
                lblMensaje.setTextFill(Color.RED);
            }
        });

        mainLayout.getChildren().addAll(lblTitulo, searchBox, infoEstudianteGrid, tablaCitas, lblMensaje);
        return mainLayout;
    }
}