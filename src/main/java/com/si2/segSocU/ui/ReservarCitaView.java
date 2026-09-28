package com.si2.segsocu.ui;

import com.si2.segsocu.dao.AfiliadoDAO;
import com.si2.segsocu.dao.CitaDAO;
import com.si2.segsocu.dao.DisponibilidadDAO;

public class ReservarCitaView {

    private final AfiliadoDAO afiliadoDAO = new AfiliadoDAO();
    private final EspecialidadDAO especialidadDAO = new EspecialidadDAO();
    private final DisponibilidadDAO disponibilidadDAO = new DisponibilidadDAO();
    private final CitaDAO citaDAO = new CitaDAO();

    private Afiliado afiliadoActual;

    public Node build() {
        VBox root = new VBox(16);
        root.setPadding(new Insets(24));

        Label titulo = new Label("Reservar cita médica");
        titulo.getStyleClass().add("titulo-vista");

        TextField txtRegistro = new TextField();
        txtRegistro.setPromptText("Tu registro universitario (ej: 2021-11234)");
        Button btnBuscarAfiliado = new Button("Verificar");
        Label lblAfiliado = new Label();
        HBox filaAfiliado = new HBox(8, txtRegistro, btnBuscarAfiliado, lblAfiliado);

        ComboBox<Especialidad> cbEspecialidad = new ComboBox<>();
        cbEspecialidad.getItems().addAll(especialidadDAO.listarTodos());
        cbEspecialidad.setPromptText("Selecciona una especialidad");

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
        tabla.setPlaceholder(new Label("Selecciona una especialidad para ver horarios disponibles."));
        tabla.setPrefHeight(220);

        Button btnReservar = new Button("Reservar cita seleccionada");

        cbEspecialidad.setOnAction(e -> {
            Especialidad esp = cbEspecialidad.getValue();
            if (esp == null) return;
            tabla.getItems().setAll(disponibilidadDAO.listarDisponiblesPorEspecialidad(esp.getId()));
        });

        btnBuscarAfiliado.setDefaultButton(true);
        btnBuscarAfiliado.setOnAction(e -> {
            String registro = txtRegistro.getText().trim();
            Optional<Afiliado> encontrado = afiliadoDAO.buscarPorRegistro(registro);
            if (encontrado.isEmpty()) {
                afiliadoActual = null;
                lblAfiliado.setText("No se encontró ese registro.");
                return;
            }
            afiliadoActual = encontrado.get();
            if (!"ACTIVO".equalsIgnoreCase(afiliadoActual.getEstado())) {
                lblAfiliado.setText("⚠ Tu afiliación está INACTIVA, no puedes reservar citas.");
                afiliadoActual = null;
                return;
            }
            lblAfiliado.setText("✔ " + encontrado.get().getNombreCompleto());
        });

        btnReservar.setOnAction(e -> {
            if (afiliadoActual == null) {
                AlertUtil.error("Falta verificar afiliado", "Primero ingresa y verifica tu registro universitario.");
                return;
            }
            Disponibilidad seleccion = tabla.getSelectionModel().getSelectedItem();
            if (seleccion == null) {
                AlertUtil.error("Selecciona un horario", "Elige una fila de la tabla de horarios disponibles.");
                return;
            }
            Especialidad esp = cbEspecialidad.getValue();
            citaDAO.insertar(afiliadoActual.getId(), seleccion.getMedicoId(), esp.getId(),
                    seleccion.getFecha(), seleccion.getHora());
            disponibilidadDAO.decrementarCupo(seleccion.getId());
            AlertUtil.info("Cita reservada", "Tu cita con " + seleccion.getMedicoNombres() + " el " +
                    seleccion.getFecha() + " a las " + seleccion.getHora() + " quedó programada.");
            tabla.getItems().setAll(disponibilidadDAO.listarDisponiblesPorEspecialidad(esp.getId()));
        });

        root.getChildren().addAll(titulo, filaAfiliado, new Label("Especialidad"), cbEspecialidad,
                tabla, btnReservar);
        return root;
    }
}
