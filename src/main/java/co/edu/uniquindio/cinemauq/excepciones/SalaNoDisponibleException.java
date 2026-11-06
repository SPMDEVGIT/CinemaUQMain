package co.edu.uniquindio.cinemauq.excepciones;

/**
 * La sala está fuera de servicio.
 */
public class SalaNoDisponibleException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public SalaNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
