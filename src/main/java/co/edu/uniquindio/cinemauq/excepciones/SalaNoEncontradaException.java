package co.edu.uniquindio.cinemauq.excepciones;

/**
 * No existe la sala buscada.
 */
public class SalaNoEncontradaException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public SalaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
