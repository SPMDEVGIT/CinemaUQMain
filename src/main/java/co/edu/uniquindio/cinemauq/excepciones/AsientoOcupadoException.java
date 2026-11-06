package co.edu.uniquindio.cinemauq.excepciones;

/**
 * El asiento ya está ocupado.
 */
public class AsientoOcupadoException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public AsientoOcupadoException(String mensaje) {
        super(mensaje);
    }
}
