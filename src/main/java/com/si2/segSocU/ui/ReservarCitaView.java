package com.si2.segSocU.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.time.LocalDate;

public class ReservarCitaView {

    // TASK 3: Selección del servicio médico
    private ComboBox<String> cbServicios = new ComboBox<>();

    // TASK 4: Selección de fecha disponible
    private DatePicker dpFechaCita = new DatePicker();

    public Node build() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        // Cargar servicios médicos de ejemplo
        cbServicios.getItems().addAll("Medicina General", "Odontología", "Enfermería", "Laboratorio");
        cbServicios.setPromptText("Seleccione Servicio Médico");

        // Restringir fechas pasadas para la cita
        dpFechaCita.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                setDisable(empty || date.isBefore(LocalDate.now()));
            }
        });

        root.getChildren().addAll(
                new Label("1. Servicio Médico:"), cbServicios,
                new Label("2. Fecha Disponible:"), dpFechaCita
        );

        return root;
    }
}