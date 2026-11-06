package co.edu.uniquindio.cinemauq.excepciones;

/**
 * La función se solapa con otra programada en la misma sala.
 */
public class CruceHorarioException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public CruceHorarioException(String mensaje) {
        super(mensaje);
    }
}
