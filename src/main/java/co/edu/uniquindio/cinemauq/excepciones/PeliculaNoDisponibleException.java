package co.edu.uniquindio.cinemauq.excepciones;

/**
 * La película no está disponible para programar funciones.
 */
public class PeliculaNoDisponibleException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public PeliculaNoDisponibleException(String mensaje) {
        super(mensaje);
    }
}
