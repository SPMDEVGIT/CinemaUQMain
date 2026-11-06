package co.edu.uniquindio.cinemauq.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.uniquindio.cinemauq.enumeraciones.Clasificacion;
import co.edu.uniquindio.cinemauq.enumeraciones.EstadoVenta;
import co.edu.uniquindio.cinemauq.enumeraciones.Genero;
import co.edu.uniquindio.cinemauq.enumeraciones.TipoCliente;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.VentaYaCanceladaException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class VentaTest {

    private Cliente cliente;
    private Sala sala;
    private Funcion funcion;
    private Asiento asiento;
    private final LocalDateTime ahora = LocalDateTime.now();

    @BeforeEach
    void preparar() throws CinemaException {
        cliente = new Cliente("1", "Ana", TipoCliente.ESTUDIANTE);
        sala = new Sala("S1", "Sala 1", 2, 3);
        Pelicula pelicula = new Pelicula("P1", "Película", Genero.DRAMA, 100, Clasificacion.TODOS);
        funcion = new Funcion("F1", pelicula, sala, LocalDate.now().plusDays(1),
                LocalTime.of(14, 0), 15000);
        asiento = funcion.buscarAsiento("A1");
        asiento.ocupar();
    }

    private Venta crear() throws DatoInvalidoException {
        return new Venta(" v001 ", cliente, funcion, asiento, ahora, 12000);
    }

    @Test
    void constructor_datosValidos_creaVentaActiva() throws DatoInvalidoException {
        Venta v = crear();
        assertEquals("V001", v.getCodigo());
        assertSame(cliente, v.getCliente());
        assertSame(funcion, v.getFuncion());
        assertSame(asiento, v.getAsiento());
        assertEquals(ahora, v.getFechaHora());
        assertEquals(12000, v.getValorPagado(), 0.001);
        assertEquals(EstadoVenta.ACTIVA, v.getEstado());
        assertTrue(v.estaActiva());
    }

    @Test
    void constructor_codigoVacio_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Venta("", cliente, funcion, asiento, ahora, 1000));
    }

    @Test
    void constructor_clienteNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Venta("V1", null, funcion, asiento, ahora, 1000));
    }

    @Test
    void constructor_funcionNula_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Venta("V1", cliente, null, asiento, ahora, 1000));
    }

    @Test
    void constructor_asientoNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Venta("V1", cliente, funcion, null, ahora, 1000));
    }

    @Test
    void constructor_fechaHoraNula_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Venta("V1", cliente, funcion, asiento, null, 1000));
    }

    @Test
    void constructor_valorNegativo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Venta("V1", cliente, funcion, asiento, ahora, -1));
    }

    @Test
    void cancelar_ventaActiva_liberaElAsientoDeLaFuncionYQuedaCancelada() throws CinemaException {
        Venta v = crear();
        assertFalse(asiento.estaDisponible());
        v.cancelar();
        assertTrue(asiento.estaDisponible());
        assertTrue(funcion.buscarAsiento("A1").estaDisponible());
        assertEquals(EstadoVenta.CANCELADA, v.getEstado());
        assertFalse(v.estaActiva());
    }

    @Test
    void cancelar_dosVeces_lanzaVentaYaCanceladaException() throws CinemaException {
        Venta v = crear();
        v.cancelar();
        assertThrows(VentaYaCanceladaException.class, v::cancelar);
    }

    @Test
    void cancelar_ventaYaCancelada_noLiberaUnAsientoReocupado() throws CinemaException {
        Venta v = crear();
        v.cancelar();
        asiento.ocupar();
        assertThrows(VentaYaCanceladaException.class, v::cancelar);
        assertFalse(asiento.estaDisponible());
    }
}
