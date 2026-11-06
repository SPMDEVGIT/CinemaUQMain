package co.edu.uniquindio.cinemauq.enumeraciones;

/**
 * Estado de un asiento.
 */
public enum EstadoAsiento {

    DISPONIBLE("Disponible"),
    OCUPADO("Ocupado");

    private final String texto;

    EstadoAsiento(String texto) {
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
