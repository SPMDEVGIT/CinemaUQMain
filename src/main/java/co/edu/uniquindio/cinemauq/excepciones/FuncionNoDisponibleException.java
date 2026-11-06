package co.edu.uniquindio.cinemauq.excepciones;

/**
 * La función no está en estado programada.
 */
public class FuncionNoDisponibleException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public FuncionNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
