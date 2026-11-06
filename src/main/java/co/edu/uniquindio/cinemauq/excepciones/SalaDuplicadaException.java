package co.edu.uniquindio.cinemauq.excepciones;

/**
 * Ya existe una sala con ese código.
 */
public class SalaDuplicadaException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public SalaDuplicadaException(String mensaje) {
        super(mensaje);
    }
}
