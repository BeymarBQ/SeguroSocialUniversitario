package com.si2.segsocu.ui;

import com.si2.segsocu.dao.DisponibilidadDAO;
import com.si2.segsocu.dao.MedicoDAO;
import com.si2.segsocu.model.Disponibilidad;
import com.si2.segsocu.model.Medico;
import com.si2.segsocu.util.AlertUtil;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;

import javafx.scene.layout.VBox;
public class GestionarDisponibilidadView {

    private final MedicoDAO medicoDAO = new MedicoDAO();
    private final DisponibilidadDAO disponibilidadDAO = new DisponibilidadDAO();

    public Node build() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));

        Label titulo = new Label("Gestionar citas y disponibilidad");
        titulo.getStyleClass().add("titulo-vista");

        TableView<Disponibilidad> tabla = new TableView<>();
        TableColumn<Disponibilidad, String> colMedico = new TableColumn<>("Médico");
        colMedico.setCellValueFactory(new PropertyValueFactory<>("medicoNombres"));
        TableColumn<Disponibilidad, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        TableColumn<Disponibilidad, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        TableColumn<Disponibilidad, Number> colCupos = new TableColumn<>("Cupos");
        colCupos.setCellValueFactory(new PropertyValueFactory<>("cupos"));
        tabla.getColumns().addAll(colMedico, colFecha, colHora, colCupos);
        tabla.setPrefHeight(240);
        tabla.getItems().setAll(disponibilidadDAO.listarTodos());

        GridPane form = new GridPane();
        form.setHgap(12);
        form.setVgap(12);
        form.setPadding(new Insets(16, 0, 0, 0));

        ComboBox<Medico> cbMedico = new ComboBox<>();
        cbMedico.getItems().addAll(medicoDAO.listarTodos());
        cbMedico.setPromptText("Médico");

        DatePicker dpFecha = new DatePicker();

        TextField txtHora = new TextField();
        txtHora.setPromptText("HH:mm, ej: 09:30");

        Spinner<Integer> spCupos = new Spinner<>(1, 20, 5);
        spCupos.setEditable(true);

        form.addRow(0, new Label("Médico *"), cbMedico);
        form.addRow(1, new Label("Fecha *"), dpFecha);
        form.addRow(2, new Label("Hora *"), txtHora);
        form.addRow(3, new Label("Cupos *"), spCupos);

        Button btnAgregar = new Button("Agregar disponibilidad");
        Button btnEliminar = new Button("Eliminar seleccionada");

        btnAgregar.setOnAction(e -> {
            Medico medico = cbMedico.getValue();
            String hora = txtHora.getText().trim();
            if (medico == null || dpFecha.getValue() == null || hora.isEmpty()) {
                AlertUtil.error("Datos incompletos", "Selecciona médico, fecha y hora.");
                return;
            }
            disponibilidadDAO.insertar(medico.getId(), dpFecha.getValue().toString(), hora, spCupos.getValue());
            tabla.getItems().setAll(disponibilidadDAO.listarTodos());
            AlertUtil.info("Disponibilidad creada", "Se agregó un nuevo horario para " + medico.getNombres() + ".");
            dpFecha.setValue(null);
            txtHora.clear();
        });

        btnEliminar.setOnAction(e -> {
            Disponibilidad seleccion = tabla.getSelectionModel().getSelectedItem();
            if (seleccion == null) {
                AlertUtil.error("Selecciona una fila", "Elige un horario de la tabla para eliminarlo.");
                return;
            }
            if (!AlertUtil.confirmar("Confirmar", "¿Eliminar este horario de disponibilidad?")) return;
            disponibilidadDAO.eliminar(seleccion.getId());
            tabla.getItems().setAll(disponibilidadDAO.listarTodos());
        });

        var botones = new javafx.scene.layout.HBox(10, btnAgregar, btnEliminar);

        root.getChildren().addAll(titulo, tabla, form, botones);
        return root;
    }
}
