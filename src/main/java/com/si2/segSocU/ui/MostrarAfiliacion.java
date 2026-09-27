public void mostrarAfiliacion(Afiliacion afiliacion) {

    lblNombre.setText(afiliacion.getNombre());
    lblDocumento.setText(afiliacion.getDocumento());
    lblNacionalidad.setText(afiliacion.getNacionalidad());
    lblCodigoUniversitario.setText(afiliacion.getCodigoUniversitario());
    lblTelefono.setText(afiliacion.getTelefono());
    lblCorreo.setText(afiliacion.getCorreo());

    lblEstadoAfiliacion.setText(afiliacion.getEstadoAfiliacion());
}