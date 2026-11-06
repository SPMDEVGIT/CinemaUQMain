package co.edu.uniquindio.cinemauq.enumeraciones;

/**
 * Estado de una venta.
 */
public enum EstadoVenta {

    ACTIVA("Activa"),
    CANCELADA("Cancelada");

    private final String texto;

    EstadoVenta(String texto) {
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
