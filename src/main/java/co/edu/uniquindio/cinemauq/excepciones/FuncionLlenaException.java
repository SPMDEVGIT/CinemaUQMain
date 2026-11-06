package co.edu.uniquindio.cinemauq.excepciones;

/**
 * La función no tiene asientos disponibles.
 */
public class FuncionLlenaException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public FuncionLlenaException(String mensaje) {
        super(mensaje);
    }
}
