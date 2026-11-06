package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.enumeraciones.TipoCliente;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;

/**
 * Tarifa de un tipo de cliente, expresada como porcentaje de descuento.
 */
public class Tarifa {

    private final TipoCliente tipoCliente;
    private double porcentajeDescuento;

    /**
     * Crea una tarifa.
     *
     * @param tipoCliente tipo de cliente al que aplica
     * @param porcentajeDescuento descuento entre 0 y 100
     * @throws DatoInvalidoException si el tipo es nulo o el porcentaje está fuera de rango
     */
    public Tarifa(TipoCliente tipoCliente, double porcentajeDescuento) throws DatoInvalidoException {
        if (tipoCliente == null) {
            throw new DatoInvalidoException("El tipo de cliente de la tarifa es obligatorio");
        }
        this.tipoCliente = tipoCliente;
        setPorcentajeDescuento(porcentajeDescuento);
    }

    /** @return el tipo de cliente de la tarifa */
    public TipoCliente getTipoCliente() {
        return tipoCliente;
    }

    /** @return el porcentaje de descuento */
    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    /**
     * Cambia el porcentaje de descuento.
     *
     * @param porcentajeDescuento valor entre 0 y 100
     * @throws DatoInvalidoException si está fuera de rango
     */
    public void setPorcentajeDescuento(double porcentajeDescuento) throws DatoInvalidoException {
        if (Double.isNaN(porcentajeDescuento) || porcentajeDescuento < 0 || porcentajeDescuento > 100) {
            throw new DatoInvalidoException("El porcentaje de descuento debe estar entre 0 y 100");
        }
        this.porcentajeDescuento = porcentajeDescuento;
    }

    /**
     * Calcula el valor a pagar aplicando el descuento.
     *
     * @param valorBase valor base de la entrada
     * @return valor con el descuento aplicado
     */
    public double calcularValor(double valorBase) {
        return valorBase * (1 - porcentajeDescuento / 100);
    }
}
