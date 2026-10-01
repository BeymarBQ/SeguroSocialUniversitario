package com.si2.segSocU.ui;

import java.util.ArrayList;
import java.util.List;

public class OcultarHorariosOcupados {

    /**
     * Task T11: Ocultar horarios que ya estén ocupados.
     */
    public static List<String> obtenerHorariosDisponibles(
            List<String> todosLosHorarios,
            List<String> horariosOcupados) {

        List<String> horariosDisponibles = new ArrayList<>();

        if (todosLosHorarios == null) {
            return horariosDisponibles;
        }

        for (String horario : todosLosHorarios) {

            if (horariosOcupados == null ||
                !horariosOcupados.contains(horario)) {

                horariosDisponibles.add(horario);
            }
        }

        return horariosDisponibles;
    }
}
