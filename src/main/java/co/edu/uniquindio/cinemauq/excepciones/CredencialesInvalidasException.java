package co.edu.uniquindio.cinemauq.excepciones;

/**
 * Usuario o contraseña incorrectos.
 */
public class CredencialesInvalidasException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }
}
