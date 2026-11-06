package co.edu.uniquindio.cinemauq.excepciones;

/**
 * No existe el cliente buscado.
 */
public class ClienteNoEncontradoException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public ClienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}
