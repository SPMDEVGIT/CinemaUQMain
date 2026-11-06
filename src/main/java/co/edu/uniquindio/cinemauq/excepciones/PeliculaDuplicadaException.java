package co.edu.uniquindio.cinemauq.excepciones;

/**
 * Ya existe una película con ese código.
 */
public class PeliculaDuplicadaException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public PeliculaDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
