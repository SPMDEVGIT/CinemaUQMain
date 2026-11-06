package co.edu.uniquindio.cinemauq.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.edu.uniquindio.cinemauq.enumeraciones.TipoCliente;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import org.junit.jupiter.api.Test;

class TarifaTest {

    @Test
    void constructor_datosValidos_creaTarifa() throws DatoInvalidoException {
        Tarifa t = new Tarifa(TipoCliente.ESTUDIANTE, 20);
        assertEquals(TipoCliente.ESTUDIANTE, t.getTipoCliente());
        assertEquals(20, t.getPorcentajeDescuento(), 0.001);
    }

    @Test
    void constructor_tipoNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> new Tarifa(null, 10));
    }

    @Test
    void constructor_porcentajeNegativo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> new Tarifa(TipoCliente.GENERAL, -1));
    }

    @Test
    void constructor_porcentajeMayorA100_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> new Tarifa(TipoCliente.GENERAL, 100.5));
    }

    @Test
    void setPorcentajeDescuento_valorValido_actualiza() throws DatoInvalidoException {
        Tarifa t = new Tarifa(TipoCliente.NINO, 30);
        t.setPorcentajeDescuento(50);
        assertEquals(50, t.getPorcentajeDescuento(), 0.001);
    }

    @Test
    void setPorcentajeDescuento_fueraDeRango_lanzaDatoInvalidoException()
            throws DatoInvalidoException {
        Tarifa t = new Tarifa(TipoCliente.NINO, 30);
        assertThrows(DatoInvalidoException.class, () -> t.setPorcentajeDescuento(101));
        assertEquals(30, t.getPorcentajeDescuento(), 0.001);
    }

    @Test
    void calcularValor_estudiante20Porciento_devuelve12000() throws DatoInvalidoException {
        Tarifa t = new Tarifa(TipoCliente.ESTUDIANTE, 20);
        assertEquals(12000, t.calcularValor(15000), 0.001);
    }

    @Test
    void calcularValor_sinDescuento_devuelveValorBase() throws DatoInvalidoException {
        Tarifa t = new Tarifa(TipoCliente.GENERAL, 0);
        assertEquals(15000, t.calcularValor(15000), 0.001);
    }
}
