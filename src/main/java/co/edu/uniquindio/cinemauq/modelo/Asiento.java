package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.enumeraciones.EstadoAsiento;
import co.edu.uniquindio.cinemauq.excepciones.AsientoOcupadoException;

/**
 * Asiento de una sala o de una función, identificado por fila y número (ej. B3).
 */
public class Asiento {

    private final char fila;
    private final int numero;
    private final String codigo;
    private EstadoAsiento estado;

    /**
     * Crea un asiento DISPONIBLE.
     *
     * @param fila letra de la fila
     * @param numero número dentro de la fila
     */
    public Asiento(char fila, int numero) {
        this.fila = fila;
        this.numero = numero;
        this.codigo = "" + fila + numero;
        this.estado = EstadoAsiento.DISPONIBLE;
    }

    /** @return la letra de la fila */
    public char getFila() {
        return fila;
    }

    /** @return el número del asiento */
    public int getNumero() {
        return numero;
    }

    /** @return el código del asiento, por ejemplo "B3" */
    public String getCodigo() {
        return codigo;
    }

    /** @return el estado del asiento */
    public EstadoAsiento getEstado() {
        return estado;
    }

    /**
     * Ocupa el asiento.
     *
     * @throws AsientoOcupadoException si ya estaba ocupado
     */
    public void ocupar() throws AsientoOcupadoException {
        if (estado == EstadoAsiento.OCUPADO) {
            throw new AsientoOcupadoException("El asiento " + codigo + " ya está ocupado");
        }
        estado = EstadoAsiento.OCUPADO;
    }

    /** Deja el asiento DISPONIBLE. */
    public void liberar() {
        estado = EstadoAsiento.DISPONIBLE;
    }

    /**
     * Indica si el asiento está disponible.
     *
     * @return true si el estado es DISPONIBLE
     */
    public boolean estaDisponible() {
        return estado == EstadoAsiento.DISPONIBLE;
    }
}
