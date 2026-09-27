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
	private VBox crearVistaDisponibilidad() {
		GridPane grid = new GridPane();
		grid.setHgap(10); grid.setVgap(10); grid.setPadding(new Insets(10));

		TextField txtMedico = new TextField();
		TextField txtEspecialidad = new TextField();
		DatePicker dpFecha = new DatePicker();
		TextField txtHora = new TextField(); // ej. 09:00

		grid.add(new Label("Médico:"), 0, 0); grid.add(txtMedico, 1, 0);
		grid.add(new Label("Especialidad:"), 0, 1); grid.add(txtEspecialidad, 1, 1);
		grid.add(new Label("Fecha:"), 0, 2); grid.add(dpFecha, 1, 2);
		grid.add(new Label("Hora:"), 0, 3); grid.add(txtHora, 1, 3);

		Button btnGuardar = new Button("Registrar Horario Libre");
		grid.add(btnGuardar, 1, 4);

		btnGuardar.setOnAction(e -> {
			if (dpFecha.getValue() != null) {
				Disponibilidad d = new Disponibilidad(0, txtMedico.getText(), txtEspecialidad.getText(),
						dpFecha.getValue().toString(), txtHora.getText(), true);
				citaDAO.guardarDisponibilidad(d);
				txtMedico.clear(); txtEspecialidad.clear(); txtHora.clear();
			}
		});

		return new VBox(10, grid);
	}

	private VBox crearVistaCitas() {
		VBox layout = new VBox(10);
		layout.setPadding(new Insets(10));

		ComboBox<Afiliado> cbAfiliados = new ComboBox<>(FXCollections.observableArrayList(afiliadoDAO.listarTodos()));
		ComboBox<Disponibilidad> cbHorarios = new ComboBox<>(FXCollections.observableArrayList(citaDAO.listarDisponibilidadLibre()));
		TextField txtMotivo = new TextField();
		Button btnAgendar = new Button("Agendar Cita");

		GridPane form = new GridPane();
		form.setHgap(10); form.setVgap(10);
		form.add(new Label("Estudiante:"), 0, 0); form.add(cbAfiliados, 1, 0);
		form.add(new Label("Horario:"), 0, 1); form.add(cbHorarios, 1, 1);
		form.add(new Label("Motivo:"), 0, 2); form.add(txtMotivo, 1, 2);
		form.add(btnAgendar, 1, 3);

		TableView<Cita> tablaCitas = new TableView<>();
		TableColumn<Cita, String> colEstudiante = new TableColumn<>("Estudiante");
		colEstudiante.setCellValueFactory(new PropertyValueFactory<>("nombreAfiliado"));
		TableColumn<Cita, String> colHorario = new TableColumn<>("Horario / Médico");
		colHorario.setCellValueFactory(new PropertyValueFactory<>("detalleHorario"));
		TableColumn<Cita, String> colEstado = new TableColumn<>("Estado");
		colEstado.setCellValueFactory(new PropertyValueFactory<>("estado"));

		tablaCitas.getColumns().addAll(colEstudiante, colHorario, colEstado);
		tablaCitas.setItems(FXCollections.observableArrayList(citaDAO.listarCitas()));

		btnAgendar.setOnAction(e -> {
			Afiliado af = cbAfiliados.getValue();
			Disponibilidad disp = cbHorarios.getValue();
			if (af != null && disp != null) {
				citaDAO.agendarCita(af.getId(), disp.getId(), txtMotivo.getText());
				tablaCitas.setItems(FXCollections.observableArrayList(citaDAO.listarCitas()));
				cbHorarios.setItems(FXCollections.observableArrayList(citaDAO.listarDisponibilidadLibre()));
			}
		});

		// Cambio de estado
		Button btnAtender = new Button("Marcar como ATENDIDA");
		btnAtender.setOnAction(e -> {
			Cita seleccionada = tablaCitas.getSelectionModel().getSelectedItem();
			if (seleccionada != null) {
				citaDAO.actualizarEstadoCita(seleccionada.getId(), "ATENDIDA", "Atención finalizada con éxito");
				tablaCitas.setItems(FXCollections.observableArrayList(citaDAO.listarCitas()));
			}
		});

		layout.getChildren().addAll(form, tablaCitas, btnAtender);
		return layout;
	}
}