package co.edu.uniquindio.cinemauq.excepciones;

/**
 * La película o sala tiene funciones programadas y no puede modificarse o deshabilitarse.
 */
public class TieneFuncionesProgramadasException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public TieneFuncionesProgramadasException(String mensaje) {
        super(mensaje);
    }
}
