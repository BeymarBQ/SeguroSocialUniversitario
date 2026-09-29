package com.si2.segSocU.ui;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ConsultarCitasView {

    // TASK 02: Diseñar la pantalla de citas programadas
    private ListView<String> lvCitasProgramadas = new ListView<>();

    public Node build() {
        VBox root = new VBox(10);
        root.setPadding(new Insets(15));

        Label titulo = new Label("Mis Citas Médicas Programadas");
        titulo.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");

        lvCitasProgramadas.setPlaceholder(new Label("No tiene citas programadas actualmente."));

        root.getChildren().addAll(titulo, lvCitasProgramadas);
        return root;
    }
}