package co.edu.uniquindio.cinemauq.excepciones;

/**
 * Dato obligatorio vacío, número fuera de rango, fecha pasada o nombre de usuario repetido.
 */
public class DatoInvalidoException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public DatoInvalidoException(String mensaje) {
        super(mensaje);
    }
}
