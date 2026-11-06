package co.edu.uniquindio.cinemauq.enumeraciones;

/**
 * Tipo de cliente, que determina la tarifa aplicada.
 */
public enum TipoCliente {

    GENERAL("General"),
    ESTUDIANTE("Estudiante"),
    NINO("Niño"),
    ADULTO_MAYOR("Adulto mayor");

    private final String texto;

    TipoCliente(String texto) {
        this.texto = texto;
    }

    /**
     * Devuelve el texto legible.
     */
    @Override
    public String toString() {
        return texto;
    }
}
