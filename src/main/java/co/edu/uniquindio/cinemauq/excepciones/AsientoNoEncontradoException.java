package co.edu.uniquindio.cinemauq.excepciones;

/**
 * El asiento no existe en la función.
 */
public class AsientoNoEncontradoException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public AsientoNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
