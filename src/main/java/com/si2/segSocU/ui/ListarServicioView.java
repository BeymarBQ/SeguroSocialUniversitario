package com.si2.segSocU.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.Arrays;

public class ListarServiciosView {

    // TASK 04: Implementar el listado de servicios médicos
    private ListView<String> lvServicios = new ListView<>();

    public Node build() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titulo = new Label("Catálogo de Servicios Médicos SSU");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        // Carga de la lista de servicios disponibles
        lvServicios.getItems().addAll(Arrays.asList(
                "Medicina General - Edificio Central",
                "Odontología - Planta Baja",
                "Enfermería y Vacunación",
                "Laboratorio Clínico",
                "Farmacia Univalle/SSU"
        ));

        root.getChildren().addAll(titulo, lvServicios);
        return root;
    }
}