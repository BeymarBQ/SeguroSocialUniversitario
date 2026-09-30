package com.si2.segSocU.ui;

import com.si2.segSocU.model.Cita;
import java.util.List;

public class MostrarCitasProgramadas {

    // Task 04: Mostrar las citas programadas
    public String mostrarCitas(List<Cita> citas) {

        if (citas == null || citas.isEmpty()) {
            return "No existen citas programadas.";
        }

        StringBuilder resultado = new StringBuilder();

        for (Cita cita : citas) {

            if (cita != null) {
                resultado.append(cita.toString());
                resultado.append("\n");
            }
        }

        return resultado.toString();
    }
}