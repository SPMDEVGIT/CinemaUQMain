package co.edu.uniquindio.cinemauq.excepciones;

/**
 * Excepción base (comprobada) de todos los errores de negocio de CinemaUQ.
 */
public class CinemaException extends Exception {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public CinemaException(String mensaje) {
        super(mensaje);
    }
}
