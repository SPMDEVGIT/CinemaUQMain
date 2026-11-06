package co.edu.uniquindio.cinemauq.enumeraciones;

/**
 * Estado de una sala.
 */
public enum EstadoSala {

    DISPONIBLE("Disponible"),
    FUERA_DE_SERVICIO("Fuera de servicio");

    private final String texto;

    EstadoSala(String texto) {
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
