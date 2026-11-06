package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.enumeraciones.EstadoVenta;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.VentaYaCanceladaException;
import java.time.LocalDateTime;

/**
 * Venta de una entrada: un cliente, una función y un asiento de esa función.
 */
public class Venta {

    private final String codigo;
    private final Cliente cliente;
    private final Funcion funcion;
    private final Asiento asiento;
    private final LocalDateTime fechaHora;
    private final double valorPagado;
    private EstadoVenta estado;

    /**
     * Crea una venta ACTIVA. No ocupa el asiento: eso lo hace Cinema.
     *
     * @param codigo código único (se guarda en mayúsculas)
     * @param cliente cliente que compra
     * @param funcion función de la entrada
     * @param asiento asiento de la función (no el de la sala)
     * @param fechaHora fecha y hora de la venta
     * @param valorPagado valor pagado (no negativo)
     * @throws DatoInvalidoException si algún dato es nulo o el valor es negativo
     */
    public Venta(String codigo, Cliente cliente, Funcion funcion, Asiento asiento,
                 LocalDateTime fechaHora, double valorPagado) throws DatoInvalidoException {
        if (codigo == null || codigo.isBlank()) {
            throw new DatoInvalidoException("El código de la venta es obligatorio");
        }
        if (cliente == null) {
            throw new DatoInvalidoException("El cliente de la venta es obligatorio");
        }
        if (funcion == null) {
            throw new DatoInvalidoException("La función de la venta es obligatoria");
        }
        if (asiento == null) {
            throw new DatoInvalidoException("El asiento de la venta es obligatorio");
        }
        if (fechaHora == null) {
            throw new DatoInvalidoException("La fecha y hora de la venta son obligatorias");
        }
        if (Double.isNaN(valorPagado) || valorPagado < 0) {
            throw new DatoInvalidoException("El valor pagado no puede ser negativo");
        }
        this.codigo = codigo.trim().toUpperCase();
        this.cliente = cliente;
        this.funcion = funcion;
        this.asiento = asiento;
        this.fechaHora = fechaHora;
        this.valorPagado = valorPagado;
        this.estado = EstadoVenta.ACTIVA;
    }

    /** @return el código de la venta */
    public String getCodigo() {
        return codigo;
    }

    /** @return el cliente */
    public Cliente getCliente() {
        return cliente;
    }

    /** @return la función */
    public Funcion getFuncion() {
        return funcion;
    }

    /** @return el asiento de la función vendido */
    public Asiento getAsiento() {
        return asiento;
    }

    /** @return la fecha y hora de la venta */
    public LocalDateTime getFechaHora() {
        return fechaHora;
    }

    /** @return el valor pagado */
    public double getValorPagado() {
        return valorPagado;
    }

    /** @return el estado de la venta */
    public EstadoVenta getEstado() {
        return estado;
    }

    /**
     * Cancela la venta y libera el asiento de la función.
     *
     * @throws VentaYaCanceladaException si la venta ya estaba cancelada
     */
    public void cancelar() throws VentaYaCanceladaException {
        if (estado == EstadoVenta.CANCELADA) {
            throw new VentaYaCanceladaException("La venta " + codigo + " ya estaba cancelada");
        }
        asiento.liberar();
        estado = EstadoVenta.CANCELADA;
    }

    /**
     * Indica si la venta está activa.
     *
     * @return true si el estado es ACTIVA
     */
    public boolean estaActiva() {
        return estado == EstadoVenta.ACTIVA;
    }
}
