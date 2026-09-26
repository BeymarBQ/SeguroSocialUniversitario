""
package com.si2.segsocu.model;

public class Afiliado {
    private int id;
    private String registroUniversitario;
    private String nombres;
    private String apellidos;
    private String rol; // Estudiante, Docente, Extranjero
    private String fechaNacimiento;
    private String telefono;
    private String email;
    private String domicilio;
    private String estado; // ACTIVO / INACTIVO
    private String fechaAfiliacion;

    public Afiliado() {}

    public Afiliado(int id, String registroUniversitario, String nombres, String apellidos, String rol,
                     String fechaNacimiento, String telefono, String email, String domicilio,
                     String estado, String fechaAfiliacion) {
        this.id = id;
        this.registroUniversitario = registroUniversitario;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.rol = rol;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.email = email;
        this.domicilio = domicilio;
        this.estado = estado;
        this.fechaAfiliacion = fechaAfiliacion;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRegistroUniversitario() { return registroUniversitario; }
    public void setRegistroUniversitario(String registroUniversitario) { this.registroUniversitario = registroUniversitario; }

    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }

    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getDomicilio() { return domicilio; }
    public void setDomicilio(String domicilio) { this.domicilio = domicilio; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getFechaAfiliacion() { return fechaAfiliacion; }
    public void setFechaAfiliacion(String fechaAfiliacion) { this.fechaAfiliacion = fechaAfiliacion; }

    public String getNombreCompleto() { return nombres + " " + apellidos; }

    @Override
    public String toString() {
        return registroUniversitario + " - " + getNombreCompleto();
    }
}


// 7. Implementar envío del formulario
btnGuardar.setOnAction(e -> {

    // Obtener los datos registrados por el estudiante
    String registro = txtRegistro.getText().trim();
    String nombres = txtNombres.getText().trim();
    String apellidos = txtApellidos.getText().trim();
    String telefono = txtTelefono.getText().trim();
    String email = txtEmail.getText().trim();
    String domicilio = txtDomicilio.getText().trim();

    // Crear el objeto Afiliado
    Afiliado afiliado = new Afiliado();

    // Asignar los datos del formulario
    afiliado.setRegistroUniversitario(registro);
    afiliado.setNombres(nombres);
    afiliado.setApellidos(apellidos);
    afiliado.setRol(cbRol.getValue());

    if (dpNacimiento.getValue() != null) {
        afiliado.setFechaNacimiento(
                dpNacimiento.getValue().toString()
        );
    }

    afiliado.setTelefono(telefono);
    afiliado.setEmail(email);
    afiliado.setDomicilio(domicilio);

    // Datos del registro
    afiliado.setEstado("ACTIVO");
    afiliado.setFechaAfiliacion(
            LocalDate.now().toString()
    );

    // Enviar los datos para guardarlos
    afiliadoDAO.insertar(afiliado);
});