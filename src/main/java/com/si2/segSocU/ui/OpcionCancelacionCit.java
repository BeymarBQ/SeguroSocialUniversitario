package com.si2.segSocU.ui;

import com.si2.segSocU.model.Cita;

public class OpcionCancelacionCit {

    private Cita citaSeleccionada;
    private boolean cancelacionConfirmada;

    public OpcionCancelacionCit() {
        this.citaSeleccionada = null;
        this.cancelacionConfirmada = false;
    }

    // Task 05: Implementar opción de cancelación
    public boolean seleccionarCita(Cita cita) {

        if (cita == null) {
            System.out.println("Debe seleccionar una cita.");
            return false;
        }

        this.citaSeleccionada = cita;
        this.cancelacionConfirmada = false;

        System.out.println("Cita seleccionada para cancelación.");
        return true;
    }

    public boolean confirmarCancelacion() {

        if (citaSeleccionada == null) {
            System.out.println("No existe una cita seleccionada.");
            return false;
        }

        this.cancelacionConfirmada = true;

        System.out.println("La cancelación de la cita fue confirmada.");
        return true;
    }

    public void cancelarOperacion() {
        this.citaSeleccionada = null;
        this.cancelacionConfirmada = false;
    }

    public Cita getCitaSeleccionada() {
        return citaSeleccionada;
    }

    public boolean isCancelacionConfirmada() {
        return cancelacionConfirmada;
    }
}