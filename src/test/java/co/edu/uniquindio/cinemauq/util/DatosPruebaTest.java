package co.edu.uniquindio.cinemauq.util;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.uniquindio.cinemauq.enumeraciones.EstadoSala;
import co.edu.uniquindio.cinemauq.enumeraciones.EstadoVenta;
import co.edu.uniquindio.cinemauq.enumeraciones.Rol;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Cinema;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class DatosPruebaTest {

    private Cinema cinema;

    @BeforeEach
    void preparar() throws CinemaException {
        cinema = new Cinema("CinemaUQ", "Armenia");
        DatosPrueba.cargar(cinema);
    }

    @Test
    void cargar_cinemaVacio_noLanzaErrores() throws CinemaException {
        Cinema otro = new Cinema("Otro", "Dirección");
        assertDoesNotThrow(() -> DatosPrueba.cargar(otro));
    }

    @Test
    void cargar_dejaLasCantidadesEsperadas() {
        assertEquals(2, cinema.getUsuarios().size());
        assertEquals(4, cinema.getPeliculas().size());
        assertEquals(3, cinema.getSalas().size());
        assertEquals(4, cinema.getClientes().size());
        assertEquals(4, cinema.getFunciones().size());
        assertEquals(4, cinema.getVentas().size());
        assertEquals(3, cinema.totalEntradasVendidas());
    }

    @Test
    void cargar_usuariosPuedenIniciarSesion() throws CinemaException {
        assertEquals(Rol.ADMINISTRADOR, cinema.iniciarSesion("admin", "admin123").getRol());
        assertEquals(Rol.OPERADOR, cinema.iniciarSesion("operador", "operador123").getRol());
    }

    @Test
    void cargar_salaS03QuedaFueraDeServicioYLasOtrasDisponibles() throws CinemaException {
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, cinema.buscarSala("S03").getEstado());
        assertTrue(cinema.buscarSala("S01").estaDisponible());
        assertTrue(cinema.buscarSala("S02").estaDisponible());
        assertEquals(60, cinema.buscarSala("S01").getCapacidad());
        assertEquals(40, cinema.buscarSala("S02").getCapacidad());
        assertEquals(24, cinema.buscarSala("S03").getCapacidad());
    }

    @Test
    void cargar_funcionesTienenLosDatosDeLaEspecificacion() throws CinemaException {
        LocalDate manana = LocalDate.now().plusDays(1);
        Funcion f1 = cinema.buscarFuncion("F001");
        Funcion f3 = cinema.buscarFuncion("F003");
        Funcion f4 = cinema.buscarFuncion("F004");
        assertEquals("P001", f1.getPelicula().getCodigo());
        assertEquals("S01", f1.getSala().getCodigo());
        assertEquals(manana, f1.getFecha());
        assertEquals(LocalTime.of(14, 0), f1.getHoraInicio());
        assertEquals(15000, f1.getValorBase(), 0.001);
        assertEquals(LocalTime.of(18, 0), cinema.buscarFuncion("F002").getHoraInicio());
        assertEquals("P004", f3.getPelicula().getCodigo());
        assertEquals(LocalTime.of(18, 30), f3.getHoraInicio());
        assertEquals(12000, f3.getValorBase(), 0.001);
        assertEquals("P002", f4.getPelicula().getCodigo());
        assertEquals(manana.plusDays(1), f4.getFecha());
        assertEquals(10000, f4.getValorBase(), 0.001);
    }

    @Test
    void cargar_ventasSonTresActivasEnF001YUnaCanceladaEnF003() throws CinemaException {
        assertEquals(3, cinema.buscarFuncion("F001").contarOcupados());
        assertEquals(0, cinema.buscarFuncion("F003").contarOcupados());
        assertEquals(EstadoVenta.CANCELADA, cinema.buscarVenta("V004").getEstado());
        assertEquals("F003", cinema.buscarVenta("V004").getFuncion().getCodigo());
        assertEquals(12000, cinema.buscarVenta("V001").getValorPagado(), 0.001);
        assertEquals(15000, cinema.buscarVenta("V002").getValorPagado(), 0.001);
        assertEquals(11250, cinema.buscarVenta("V003").getValorPagado(), 0.001);
        assertEquals(8400, cinema.buscarVenta("V004").getValorPagado(), 0.001);
    }

    @Test
    void cargar_reportesDanLosValoresEsperados() throws CinemaException {
        assertEquals(3, cinema.totalEntradasVendidas());
        assertEquals(38250, cinema.totalIngresos(), 0.001);
        List<Pelicula> masVendidas = cinema.peliculasMasVendidas();
        assertEquals(1, masVendidas.size());
        assertEquals("P001", masVendidas.get(0).getCodigo());
        assertEquals(5.0, cinema.ocupacionFuncion("F001"), 0.001);
        assertEquals(0.0, cinema.ocupacionFuncion("F003"), 0.001);
    }

    @Test
    void cargar_funcionesPorFecha_ordenadasPorHora() throws CinemaException {
        LocalDate manana = LocalDate.now().plusDays(1);
        List<Funcion> deManana = cinema.funcionesPorFecha(manana);
        assertEquals(3, deManana.size());
        assertEquals("F001", deManana.get(0).getCodigo());
        assertEquals("F002", deManana.get(1).getCodigo());
        assertEquals("F003", deManana.get(2).getCodigo());
        assertEquals(1, cinema.funcionesPorFecha(manana.plusDays(1)).size());
        assertFalse(cinema.listarFuncionesProgramadas().isEmpty());
    }
}
