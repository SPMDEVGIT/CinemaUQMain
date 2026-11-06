package co.edu.uniquindio.cinemauq.enumeraciones;

/**
 * Estado de una película en cartelera.
 */
public enum EstadoPelicula {

    DISPONIBLE("Disponible"),
    NO_DISPONIBLE("No disponible");

    private final String texto;

    EstadoPelicula(String texto) {
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
