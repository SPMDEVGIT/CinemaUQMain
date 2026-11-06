package co.edu.uniquindio.cinemauq.excepciones;

/**
 * La venta ya estaba cancelada.
 */
public class VentaYaCanceladaException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public VentaYaCanceladaException(String mensaje) {
        super(mensaje);
    }
}
