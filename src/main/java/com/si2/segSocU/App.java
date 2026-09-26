package com.si2.segsocu.ui;

import com.si2.segsocu.dao.AfiliadoDAO;
import com.si2.segsocu.db.Database;
import com.si2.segsocu.model.Afiliado;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class App extends Application {

	private final AfiliadoDAO dao = new AfiliadoDAO();
	private final TableView<Afiliado> table = new TableView<>();
	private final ObservableList<Afiliado> data = FXCollections.observableArrayList();

	@Override
	public void start(Stage stage) {
		Database.initDatabase();

		BorderPane root = new BorderPane();
		root.setPadding(new Insets(10));

		// Formulario
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(10));

		TextField txtRU = new TextField();
		TextField txtNombres = new TextField();
		TextField txtApellidos = new TextField();
		ComboBox<String> cbRol = new ComboBox<>(FXCollections.observableArrayList("Estudiante", "Docente", "Extranjero"));
		cbRol.getSelectionModel().selectFirst();
		TextField txtTelefono = new TextField();
		TextField txtEmail = new TextField();
		ComboBox<String> cbEstado = new ComboBox<>(FXCollections.observableArrayList("ACTIVO", "INACTIVO"));
		cbEstado.getSelectionModel().selectFirst();

		grid.add(new Label("Registro Univ.:"), 0, 0); grid.add(txtRU, 1, 0);
		grid.add(new Label("Nombres:"), 0, 1);       grid.add(txtNombres, 1, 1);
		grid.add(new Label("Apellidos:"), 0, 2);     grid.add(txtApellidos, 1, 2);
		grid.add(new Label("Rol:"), 0, 3);           grid.add(cbRol, 1, 3);
		grid.add(new Label("Teléfono:"), 0, 4);      grid.add(txtTelefono, 1, 4);
		grid.add(new Label("Email:"), 0, 5);         grid.add(txtEmail, 1, 5);
		grid.add(new Label("Estado:"), 0, 6);        grid.add(cbEstado, 1, 6);

		Button btnGuardar = new Button("Registrar Afiliado");
		Button btnEliminar = new Button("Eliminar Seleccionado");
		HBox boxBotones = new HBox(10, btnGuardar, btnEliminar);
		grid.add(boxBotones, 1, 7);

		// Tabla
		TableColumn<Afiliado, String> colRU = new TableColumn<>("RU");
		colRU.setCellValueFactory(new PropertyValueFactory<>("registroUniversitario"));

		TableColumn<Afiliado, String> colNombre = new TableColumn<>("Nombre Completo");
		colNombre.setCellValueFactory(new PropertyValueFactory<>("nombreCompleto"));

		TableColumn<Afiliado, String> colRol = new TableColumn<>("Rol");
		colRol.setCellValueFactory(new PropertyValueFactory<>("rol"));

		TableColumn<Afiliado, String> colEstado = new TableColumn<>("Estado");
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

		table.getColumns().addAll(colRU, colNombre, colRol, colEstado);
		actualizarTabla();

		// Acciones
		btnGuardar.setOnAction(e -> {
			Afiliado nuevo = new Afiliado(
					0, txtRU.getText(), txtNombres.getText(), txtApellidos.getText(),
					cbRol.getValue(), "", txtTelefono.getText(), txtEmail.getText(),
					"", cbEstado.getValue(), LocalDate.now().toString()
			);
			if (dao.insertar(nuevo)) {
				actualizarTabla();
				txtRU.clear(); txtNombres.clear(); txtApellidos.clear();
			}
		});

		btnEliminar.setOnAction(e -> {
			Afiliado seleccionado = table.getSelectionModel().getSelectedItem();
			if (seleccionado != null) {
				dao.eliminar(seleccionado.getId());
				actualizarTabla();
			}
		});

		root.setLeft(grid);
		root.setCenter(table);

		Scene scene = new Scene(root, 800, 400);
		stage.setTitle("Seguro Social Universitario - Gestión de Afiliados");
		stage.setScene(scene);
		stage.show();
	}

	private void actualizarTabla() {
		data.setAll(dao.listarTodos());
		table.setItems(data);
	}
}