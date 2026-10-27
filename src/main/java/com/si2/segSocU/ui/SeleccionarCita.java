package com.si2.segSocU.ui;

import com.si2.segSocU.model.Cita;
import java.util.List;

public class SeleccionarCita {

    private Cita citaSeleccionada;

    // Implementar selección de la cita
    public boolean seleccionarCita(List<Cita> citas, int posicion) {

        if (citas == null || posicion < 0 || posicion >= citas.size()) {
            citaSeleccionada = null;
            return false;
        }

        citaSeleccionada = citas.get(posicion);
        return true;
    }

    public Cita getCitaSeleccionada() {
        return citaSeleccionada;
    }
}
