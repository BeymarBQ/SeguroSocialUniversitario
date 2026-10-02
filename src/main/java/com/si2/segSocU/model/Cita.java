public class Cita {

    private int estudianteId;
    private int servicioId;
    private String fecha;
    private String hora;
    private String estado;

    public Cita(int estudianteId, int servicioId,
                String fecha, String hora, String estado) {
        this.estudianteId = estudianteId;
        this.servicioId = servicioId;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
    }

    public int getEstudianteId() {
        return estudianteId;
    }

    public int getServicioId() {
        return servicioId;
    }

    public String getFecha() {
        return fecha;
    }

    public String getHora() {
        return hora;
    }

    public String getEstado() {
        return estado;
    }
}


// task 9 registrar una atencion medica
package model;

public class Atencion {
    private int id;
    private int citaId;
    private String motivo;
    private String diagnostico;
    private String indicaciones;
    private String fecha;

    public Atencion() {}

    public Atencion(int citaId, String motivo, String diagnostico, String indicaciones, String fecha) {
        this.citaId = citaId;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.indicaciones = indicaciones;
        this.fecha = fecha;
    }

    public Atencion(int id, int citaId, String motivo, String diagnostico, String indicaciones, String fecha) {
        this.id = id;
        this.citaId = citaId;
        this.motivo = motivo;
        this.diagnostico = diagnostico;
        this.indicaciones = indicaciones;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCitaId() { return citaId; }
    public void setCitaId(int citaId) { this.citaId = citaId; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }

    public String getIndicaciones() { return indicaciones; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
}


//task dia lunes N° 10
package dao;

import db.Database;
import model.Atencion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AtencionDAO {

    /**
     * Tarea 10: Inserta el registro de atención médica en la base de datos.
     * Retorna el ID generado para la atención registrada.
     */
    public int insertar(Atencion atencion) throws SQLException {
        String sql = "INSERT INTO atencion (cita_id, motivo, diagnostico, indicaciones, fecha) VALUES (?, ?, ?, ?, ?)";

        Connection conn = Database.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, atencion.getCitaId());
            stmt.setString(2, atencion.getMotivo());
            stmt.setString(3, atencion.getDiagnostico());
            stmt.setString(4, atencion.getIndicaciones());
            stmt.setString(5, atencion.getFecha());

            int filasAfectadas = stmt.executeUpdate();
            if (filasAfectadas > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        return rs.getInt(1);
                    }
                }
            }
        }
        return -1;
    }
}

//task N°6 cancelar una cita medica

package dao;

import db.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CitaDAO {

    /**
     * Task 09 / Task 07: Cambia el estado de la cita a "CANCELADA".
     * No elimina la cita de la BD, manteniendo el registro histórico.
     */
    public void cambiarEstado(int idCita, String nuevoEstado) throws SQLException {
        String sql = "UPDATE cita SET estado = ? WHERE id = ?";

        Connection conn = Database.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nuevoEstado);
            stmt.setInt(2, idCita);
            stmt.executeUpdate();
        }
    }
}

//task N° 9 martes

package ui;

import dao.CitaDAO;
import dao.DisponibilidadDAO;
import model.Cita;
import util.AlertUtil;

import java.sql.SQLException;

public class CancelarCitaView {

    private CitaDAO citaDAO = new CitaDAO();
    private DisponibilidadDAO disponibilidadDAO = new DisponibilidadDAO();

    /**
     * Maneja la acción del botón / opción de cancelación de una cita seleccionada.
     *
     * @param citaSeleccionada La cita pendiente elegida por el docente.
     */
    public void cancelarCitaSeleccionada(Cita citaSeleccionada) {
        if (citaSeleccionada == null) {
            AlertUtil.mostrarError("Selección requerida", "Debe seleccionar una cita pendiente para cancelar.");
            return;
        }

        // =========================================================================
        // Task 06: Solicitar confirmación de la cancelación
        // =========================================================================
        boolean confirmado = AlertUtil.mostrarConfirmacion(
                "Confirmar cancelación",
                "¿Está seguro de que desea cancelar la cita médica del " +
                        citaSeleccionada.getFecha() + " a las " + citaSeleccionada.getHora() + "?"
        );

        if (!confirmado) {
            // El usuario canceló la acción de confirmación
            return;
        }

        try {
            // =========================================================================
            // Task 09 (y Task 07, 08): Actualizar estado a CANCELADA, mantener en registro
            // y liberar cupo/horario.
            // =========================================================================

            // 1. Cambiar estado a "CANCELADA" (Mantiene el registro en BD - Task 09)
            citaDAO.cambiarEstado(citaSeleccionada.getId(), "CANCELADA");

            // 2. Liberar el cupo/horario correspondiente (Task 08)
            disponibilidadDAO.incrementarCupoPorMedicoFechaHora(
                    citaSeleccionada.getMedicoId(),
                    citaSeleccionada.getFecha(),
                    citaSeleccionada.getHora()
            );

            AlertUtil.mostrarInfo(
                    "Cita Cancelada",
                    "La cita fue cancelada exitosamente y permanecerá en su historial de registros."
            );

            // Refrescar lista de citas pendientes en la interfaz
            actualizarListaCitasPendientes();

        } catch (SQLException e) {
            e.printStackTrace();
            AlertUtil.mostrarError("Error", "Ocurrió un error al intentar cancelar la cita: " + e.getMessage());
        }
    }

    private void actualizarListaCitasPendientes() {
        // Lógica para recargar la tabla/lista de citas de la UI
    }
}


//tarea del miercoles task 1 --- 2 1111
package model;

public class Especialidad {
    private int id;
    private String nombre;
    private String requisitos;      // Task 02: Requisitos de acceso
    private String descripcion;     // Task 02: Información específica
    private boolean requiereCita;   // Task 02: Indicador si requiere cita previa
    private String horarioAtencion; // Task 02: Horarios del servicio

    public Especialidad() {}

    public Especialidad(int id, String nombre, String requisitos) {
        this.id = id;
        this.nombre = nombre;
        this.requisitos = requisitos;
    }

    public Especialidad(int id, String nombre, String requisitos, String descripcion, boolean requiereCita, String horarioAtencion) {
        this.id = id;
        this.nombre = nombre;
        this.requisitos = requisitos;
        this.descripcion = descripcion;
        this.requiereCita = requiereCita;
        this.horarioAtencion = horarioAtencion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getRequisitos() { return requisitos; }
    public void setRequisitos(String requisitos) { this.requisitos = requisitos; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public boolean isRequiereCita() { return requiereCita; }
    public void setRequiereCita(boolean requiereCita) { this.requiereCita = requiereCita; }

    public String getHorarioAtencion() { return horarioAtencion; }
    public void setHorarioAtencion(String horarioAtencion) { this.horarioAtencion = horarioAtencion; }

    @Override
    public String toString() {
        return this.nombre;
    }
}

//task 2 miercoles
package dao;

import db.Database;
import model.Especialidad;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class EspecialidadDAO {

    /**
     * Task 01 & Task 02:
     * Identifica y recupera la lista completa de servicios médicos disponibles
     * con toda su información asociada (requisitos, cita previa, descripción).
     */
    public List<Especialidad> listarTodos() throws SQLException {
        List<Especialidad> listaServicios = new ArrayList<>();

        // Consulta SQL para traer todos los servicios activos/disponibles
        String sql = "SELECT id, nombre, requisitos, descripcion, requiere_cita, horario_atencion FROM especialidad";

        Connection conn = Database.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Especialidad esp = new Especialidad();
                esp.setId(rs.getInt("id"));
                esp.setNombre(rs.getString("nombre"));
                esp.setRequisitos(rs.getString("requisitos"));
                esp.setDescripcion(rs.getString("descripcion"));
                esp.setRequiereCita(rs.getBoolean("requiere_cita"));
                esp.setHorarioAtencion(rs.getString("horario_atencion"));

                listaServicios.add(esp);
            }
        }
        return listaServicios;
    }

    /**
     * Task 02: Obtener la información detallada de un servicio específico por su ID
     */
    public Especialidad buscarPorId(int id) throws SQLException {
        String sql = "SELECT id, nombre, requisitos, descripcion, requiere_cita, horario_atencion FROM especialidad WHERE id = ?";

        Connection conn = Database.getInstance().getConnection();
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Especialidad(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getString("requisitos"),
                            rs.getString("descripcion"),
                            rs.getBoolean("requiere_cita"),
                            rs.getString("horarioAtencion")
                    );
                }
            }
        }
        return null;
    }
}


//hola mundo dia jueves task 1
package model;

import java.time.LocalDate;

public class SolicitudBaja {
    private int afiliadoId;
    private String codsis;          // Para identificar y buscar al estudiante
    private String motivo;          // Condición: Registrar motivo de la baja
    private String fechaEfectiva;   // Condición: Registrar fecha efectiva
    private String estadoAnterior;

    public SolicitudBaja() {}

    public SolicitudBaja(int afiliadoId, String codsis, String motivo, String fechaEfectiva) {
        this.afiliadoId = afiliadoId;
        this.codsis = codsis;
        this.motivo = motivo;
        this.fechaEfectiva = fechaEfectiva != null ? fechaEfectiva : LocalDate.now().toString();
    }

    // Getters y Setters
    public int getAfiliadoId() { return afiliadoId; }
    public void setAfiliadoId(int afiliadoId) { this.afiliadoId = afiliadoId; }

    public String getCodsis() { return codsis; }
    public void setCodsis(String codsis) { this.codsis = codsis; }

    public String getMotivo() { return motivo; }
    public void setMotivo(String motivo) { this.motivo = motivo; }

    public String getFechaEfectiva() { return fechaEfectiva; }
    public void setFechaEfectiva(String fechaEfectiva) { this.fechaEfectiva = fechaEfectiva; }

    public String getEstadoAnterior() { return estadoAnterior; }
    public void setEstadoAnterior(String estadoAnterior) { this.estadoAnterior = estadoAnterior; }
}


//task 2
package ui;

import model.Afiliado;
import model.SolicitudBaja;
import util.AlertUtil;

import java.time.LocalDate;

public class DarDeBajaAfiliacionView {

    // Componentes del Formulario / UI
    private String txtCodsisBusqueda;
    private String txtMotivoBaja;
    private String txtFechaEfectiva;
    private Afiliado estudianteEncontrado;

    public DarDeBajaAfiliacionView() {
        this.txtFechaEfectiva = LocalDate.now().toString(); // Fecha por defecto
    }

    /**
     * Renderiza o inicializa la estructura de la vista
     */
    public void inicializarPantalla() {
        // En una UI Swing/JavaFX aquí se instancian los paneles,
        // botones ("Buscar", "Confirmar Baja") y campos de texto.
        System.out.println("--- Pantalla de Solicitud de Baja de Afiliación ---");
    }

    /**
     * Captura los datos ingresados en la interfaz para construir el objeto de solicitud (Task 01)
     */
    public SolicitudBaja obtenerDatosFormulario() {
        if (estudianteEncontrado == null) {
            AlertUtil.mostrarError("Error", "Debe buscar y seleccionar un estudiante primero.");
            return null;
        }

        if (txtMotivoBaja == null || txtMotivoBaja.trim().isEmpty()) {
            AlertUtil.mostrarError("Campo Requerido", "Debe ingresar el motivo de la baja.");
            return null;
        }

        return new SolicitudBaja(
                estudianteEncontrado.getId(),
                estudianteEncontrado.getCodsis(),
                this.txtMotivoBaja.trim(),
                this.txtFechaEfectiva
        );
    }

    // Setters mock para simular la captura de datos en la UI
    public void setTxtCodsisBusqueda(String codsis) { this.txtCodsisBusqueda = codsis; }
    public void setTxtMotivoBaja(String motivo) { this.txtMotivoBaja = motivo; }
    public void setEstudianteEncontrado(Afiliado afiliado) { this.estudianteEncontrado = afiliado; }
}