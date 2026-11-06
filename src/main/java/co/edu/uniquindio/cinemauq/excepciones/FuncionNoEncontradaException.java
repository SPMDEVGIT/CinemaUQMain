package co.edu.uniquindio.cinemauq.excepciones;

/**
 * No existe la función buscada.
 */
public class FuncionNoEncontradaException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public FuncionNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
