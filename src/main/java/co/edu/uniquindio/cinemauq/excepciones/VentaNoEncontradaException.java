package co.edu.uniquindio.cinemauq.excepciones;

/**
 * No existe la venta buscada.
 */
public class VentaNoEncontradaException extends CinemaException {

    /**
     * Crea la excepción con un mensaje descriptivo.
     *
     * @param mensaje descripción del error
     */
    public VentaNoEncontradaException(String mensaje) {
        super(mensaje);
    }
}
