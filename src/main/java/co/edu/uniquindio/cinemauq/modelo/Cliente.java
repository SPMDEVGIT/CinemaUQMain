package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.enumeraciones.TipoCliente;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;

/**
 * Cliente que compra entradas. Su tipo define la tarifa que paga.
 */
public class Cliente extends Persona {

    private final TipoCliente tipoCliente;

    /**
     * Crea un cliente.
     *
     * @param identificacion documento de identidad
     * @param nombre nombre completo
     * @param tipoCliente tipo de cliente
     * @throws DatoInvalidoException si algún dato es nulo o vacío
     */
    public Cliente(String identificacion, String nombre, TipoCliente tipoCliente)
            throws DatoInvalidoException {
        super(identificacion, nombre);
        if (tipoCliente == null) {
            throw new DatoInvalidoException("El tipo de cliente es obligatorio");
        }
        this.tipoCliente = tipoCliente;
    }

    /** @return el tipo de cliente */
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }
}
