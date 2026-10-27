package com.si2.segSocU.ui;

import com.si2.segSocU.model.Cita;

public class OpcionCancelacionCita {

    private Cita citaSeleccionada;
    private boolean cancelacionConfirmada;

    public OpcionCancelacionCita() {
        this.citaSeleccionada = null;
        this.cancelacionConfirmada = false;
    }

    /**
     * T05 - Implementar opción de cancelación.
     * Permite seleccionar la cita que se desea cancelar.
     */
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

    /**
     * Confirma la cancelación de la cita seleccionada.
     */
    public boolean confirmarCancelacion() {

        if (citaSeleccionada == null) {
            System.out.println("No existe una cita seleccionada.");
            return false;
        }

        this.cancelacionConfirmada = true;

        System.out.println("La cancelación de la cita fue confirmada.");
        return true;
    }

    /**
     * Permite cancelar la operación si el usuario
     * decide no continuar.
     */
    public void cancelarOperacion() {

        this.citaSeleccionada = null;
        this.cancelacionConfirmada = false;

        System.out.println("Operación de cancelación anulada.");
    }

    public Cita getCitaSeleccionada() {
        return citaSeleccionada;
    }

    public boolean isCancelacionConfirmada() {
        return cancelacionConfirmada;
    }
}