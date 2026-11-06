package co.edu.uniquindio.cinemauq.excepciones;

/**
 * Ya existe un cliente con esa identificación.
 */
public class ClienteDuplicadoException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public ClienteDuplicadoException(String mensaje) {
        super(mensaje);
    }
}
