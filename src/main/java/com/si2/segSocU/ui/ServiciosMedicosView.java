package com.si2.segsocu.ui;

import com.si2.segsocu.dao.EspecialidadDAO;
import com.si2.segsocu.model.Especialidad;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

/**
 * Historia 14: Consulta de Servicios Médicos (Informativa).
 * Muestra el catálogo de especialidades y requisitos.
 */
public class ServiciosMedicosView {

    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();

    public Node build() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));

        Label titulo = new Label("Consulta de Servicios Médicos");
        titulo.getStyleClass().add("titulo-vista");

        TableView<Especialidad> tabla = new TableView<>();
        TableColumn<Especialidad, String> colNombre = new TableColumn<>("Especialidad");
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colNombre.setPrefWidth(200);
        TableColumn<Especialidad, String> colRequisitos = new TableColumn<>("Requisitos");
        colRequisitos.setCellValueFactory(new PropertyValueFactory<>("requisitos"));
        colRequisitos.setPrefWidth(400);
        tabla.getColumns().addAll(colNombre, colRequisitos);
        tabla.setPrefHeight(300);
        tabla.getItems().setAll(especialidadDAO.listarTodos());

        root.getChildren().addAll(titulo, tabla);
        return root;
    }
}
