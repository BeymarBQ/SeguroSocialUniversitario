package com.si2.segsocu.ui;

import com.si2.segsocu.dao.AfiliadoDAO;
import com.si2.segsocu.dao.CitaDAO;
import com.si2.segsocu.dao.DisponibilidadDAO;
import com.si2.segsocu.model.Afiliado;
import com.si2.segsocu.model.Cita;
import com.si2.segsocu.util.AlertUtil;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Historia 6: Cancelar una cita médica (Estudiante/Docente).
 * Libera el cupo en la agenda si el usuario no puede asistir.
 */
public class CancelarCitaView {

    private final AfiliadoDAO afiliadoDAO = new AfiliadoDAO();
    private final CitaDAO citaDAO = new CitaDAO();
    private final DisponibilidadDAO disponibilidadDAO = new DisponibilidadDAO();

    private Afiliado afiliadoActual;

    public Node build() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));

        Label titulo = new Label("Cancelar cita médica");
        titulo.getStyleClass().add("titulo-vista");

        TextField txtRegistro = new TextField();
        txtRegistro.setPromptText("Registro universitario (ej: 2021-11234)");
        Button btnBuscar = new Button("Ver mis citas programadas");
        HBox buscador = new HBox(8, txtRegistro, btnBuscar);

        TableView<Cita> tabla = new TableView<>();
        TableColumn<Cita, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        TableColumn<Cita, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(new PropertyValueFactory<>("hora"));
        TableColumn<Cita, String> colEspecialidad = new TableColumn<>("Especialidad");
        colEspecialidad.setCellValueFactory(new PropertyValueFactory<>("especialidadNombre"));
        TableColumn<Cita, String> colMedico = new TableColumn<>("Médico");
        colMedico.setCellValueFactory(new PropertyValueFactory<>("medicoNombre"));
        tabla.getColumns().addAll(colFecha, colHora, colEspecialidad, colMedico);
        tabla.setPlaceholder(new Label("Ingresa tu registro universitario y presiona el botón de arriba."));
        tabla.setPrefHeight(220);

        Button btnCancelar = new Button("Cancelar cita seleccionada");

        btnBuscar.setDefaultButton(true);
        btnBuscar.setOnAction(e -> {
            String registro = txtRegistro.getText().trim();
            Optional<Afiliado> encontrado = afiliadoDAO.buscarPorRegistro(registro);
            if (encontrado.isEmpty()) {
                AlertUtil.error("No encontrado", "No existe ningún afiliado con ese registro universitario.");
                return;
            }
            afiliadoActual = encontrado.get();
            var programadas = citaDAO.listarPorAfiliado(afiliadoActual.getId()).stream()
                    .filter(c -> "PROGRAMADA".equalsIgnoreCase(c.getEstado()))
                    .collect(Collectors.toList());
            tabla.getItems().setAll(programadas);
            if (programadas.isEmpty()) {
                AlertUtil.info("Sin citas", "No tienes citas programadas para cancelar.");
            }
        });

        btnCancelar.setOnAction(e -> {
            Cita seleccion = tabla.getSelectionModel().getSelectedItem();
            if (seleccion == null) {
                AlertUtil.error("Selecciona una cita", "Elige una fila de la tabla de citas programadas.");
                return;
            }
            if (!AlertUtil.confirmar("Confirmar cancelación",
                    "¿Cancelar la cita del " + seleccion.getFecha() + " a las " + seleccion.getHora() + "?")) {
                return;
            }
            citaDAO.cambiarEstado(seleccion.getId(), "CANCELADA");
            disponibilidadDAO.incrementarCupoPorMedicoFechaHora(seleccion.getMedicoId(), seleccion.getFecha(), seleccion.getHora());
            tabla.getItems().remove(seleccion);
            AlertUtil.info("Cita cancelada", "Se liberó el cupo correspondiente en la agenda del médico.");
        });

        root.getChildren().addAll(titulo, buscador, tabla, btnCancelar);
        return root;
    }
}
