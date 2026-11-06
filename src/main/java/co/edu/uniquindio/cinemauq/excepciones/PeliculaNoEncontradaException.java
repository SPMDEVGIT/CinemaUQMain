package co.edu.uniquindio.cinemauq.excepciones;

/**
 * No existe la película buscada.
 */
public class PeliculaNoEncontradaException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public PeliculaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
