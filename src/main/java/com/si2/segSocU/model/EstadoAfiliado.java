package com.si2.segsocu.model;

public class VerificarEstado {
    private boolean habilitado;
    private String mensaje;

    public VerificarEstado(Afiliado afiliado) {
        if (afiliado == null) {
            this.habilitado = false;
            this.mensaje = "Afiliado no encontrado.";
        } else if ("ACTIVO".equalsIgnoreCase(afiliado.getEstado())) {
            this.habilitado = true;
            this.mensaje = "HABILITADO PARA ATENCIÓN MÉDICA";
        } else {
            this.habilitado = false;
            this.mensaje = "NO HABILITADO (Estado actual: " + afiliado.getEstado() + ")";
        }
    }

    public boolean isHabilitado() {
        return habilitado;
    }

    public String getMensaje() {
        return mensaje;
    }
}
// 7. Asociar / Obtener la información de afiliación por Registro Universitario
public Afiliado buscarPorRegistroUniversitario(String ru) {
    String sql = "SELECT * FROM afiliados WHERE registro_universitario = ?";
    try (Connection conn = Database.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

        pstmt.setString(1, ru);
        try (ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return new Afiliado(
                        rs.getInt("id"),
                        rs.getString("registro_universitario"),
                        rs.getString("nombres"),
                        rs.getString("apellidos"),
                        rs.getString("rol"),
                        rs.getString("fecha_nacimiento"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getString("domicilio"),
                        rs.getString("estado"),
                        rs.getString("fecha_afiliacion")
                );
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null; // Retorna null si el registro no existe
}