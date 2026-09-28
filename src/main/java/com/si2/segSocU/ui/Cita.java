@FXML
private void reservarCita() {

    String fechaSeleccionada =
            fecha.getValue().toString();

    String horaSeleccionada =
            cbHorario.getValue();

    if (fecha.getValue() == null ||
            horaSeleccionada == null ||
            cbServicio.getValue() == null) {

        mostrarAlerta(
                "Error",
                "Debe completar todos los campos."
        );

        return;
    }

    CitaDAO dao = new CitaDAO();

    // T09: comprobar si el horario está ocupado
    if (dao.horarioOcupado(
            fechaSeleccionada,
            horaSeleccionada)) {

        mostrarAlerta(
                "Horario ocupado",
                "El horario seleccionado ya está ocupado."
        );

        return;
    }

    // Obtener el servicio seleccionado
    int servicioId = obtenerServicioId(
            cbServicio.getValue()
    );

    // T10: crear y registrar la cita
    Cita cita = new Cita(
            estudianteId,
            servicioId,
            fechaSeleccionada,
            horaSeleccionada,
            "PENDIENTE"
    );

    boolean registrada = dao.registrarCita(cita);

    // T11: mostrar confirmación
    if (registrada) {

        mostrarAlerta(
                "Cita registrada",
                "Su cita médica fue reservada correctamente."
        );

        limpiarFormulario();

    } else {

        mostrarAlerta(
                "Error",
                "No se pudo registrar la cita."
        );
    }
}