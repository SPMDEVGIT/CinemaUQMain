package co.edu.uniquindio.cinemauq.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.uniquindio.cinemauq.enumeraciones.Clasificacion;
import co.edu.uniquindio.cinemauq.enumeraciones.EstadoFuncion;
import co.edu.uniquindio.cinemauq.enumeraciones.Genero;
import co.edu.uniquindio.cinemauq.excepciones.AsientoNoEncontradoException;
import co.edu.uniquindio.cinemauq.excepciones.AsientoOcupadoException;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionNoDisponibleException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class FuncionTest {

    private final LocalDate manana = LocalDate.now().plusDays(1);

    private Pelicula pelicula(int minutos) throws DatoInvalidoException {
        return new Pelicula("P1", "Película", Genero.DRAMA, minutos, Clasificacion.TODOS);
    }

    private Sala sala(String codigo) throws DatoInvalidoException {
        return new Sala(codigo, "Sala " + codigo, 2, 3);
    }

    private Funcion funcion(String codigo, Sala sala, int minutos, LocalDate fecha,
                            LocalTime hora) throws DatoInvalidoException {
        return new Funcion(codigo, pelicula(minutos), sala, fecha, hora, 15000);
    }

    @Test
    void constructor_datosValidos_creaFuncionProgramada() throws DatoInvalidoException {
        Sala s = sala("S1");
        Funcion f = new Funcion(" f001 ", pelicula(120), s, manana, LocalTime.of(14, 0), 15000);
        assertEquals("F001", f.getCodigo());
        assertEquals(EstadoFuncion.PROGRAMADA, f.getEstado());
        assertTrue(f.estaProgramada());
        assertSame(s, f.getSala());
        assertEquals(manana, f.getFecha());
        assertEquals(LocalTime.of(14, 0), f.getHoraInicio());
        assertEquals(15000, f.getValorBase(), 0.001);
        assertEquals(6, f.getAsientos().size());
    }

    @Test
    void constructor_codigoVacio_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Pelicula p = pelicula(100);
        Sala s = sala("S1");
        assertThrows(DatoInvalidoException.class,
                () -> new Funcion("", p, s, manana, LocalTime.NOON, 1000));
    }

    @Test
    void constructor_peliculaNula_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Sala s = sala("S1");
        assertThrows(DatoInvalidoException.class,
                () -> new Funcion("F1", null, s, manana, LocalTime.NOON, 1000));
    }

    @Test
    void constructor_salaNula_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Pelicula p = pelicula(100);
        assertThrows(DatoInvalidoException.class,
                () -> new Funcion("F1", p, null, manana, LocalTime.NOON, 1000));
    }

    @Test
    void constructor_fechaNula_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Pelicula p = pelicula(100);
        Sala s = sala("S1");
        assertThrows(DatoInvalidoException.class,
                () -> new Funcion("F1", p, s, null, LocalTime.NOON, 1000));
    }

    @Test
    void constructor_horaNula_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Pelicula p = pelicula(100);
        Sala s = sala("S1");
        assertThrows(DatoInvalidoException.class,
                () -> new Funcion("F1", p, s, manana, null, 1000));
    }

    @Test
    void constructor_valorBaseCero_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Pelicula p = pelicula(100);
        Sala s = sala("S1");
        assertThrows(DatoInvalidoException.class,
                () -> new Funcion("F1", p, s, manana, LocalTime.NOON, 0));
    }

    @Test
    void getAsientos_listaNoModificable() throws DatoInvalidoException {
        List<Asiento> lista = funcion("F1", sala("S1"), 100, manana, LocalTime.NOON).getAsientos();
        assertThrows(UnsupportedOperationException.class, () -> lista.remove(0));
    }

    @Test
    void asientos_dosFuncionesDeLaMismaSala_sonIndependientes() throws CinemaException {
        Sala s = sala("S1");
        Funcion f1 = funcion("F1", s, 100, manana, LocalTime.of(14, 0));
        Funcion f2 = funcion("F2", s, 100, manana, LocalTime.of(18, 0));
        f1.buscarAsiento("A1").ocupar();
        assertFalse(f1.buscarAsiento("A1").estaDisponible());
        assertTrue(f2.buscarAsiento("A1").estaDisponible());
        assertTrue(s.getAsientos().get(0).estaDisponible());
        assertEquals(1, f1.contarOcupados());
        assertEquals(0, f2.contarOcupados());
    }

    @Test
    void getHoraFin_sumaDuracionALaHoraDeInicio() throws DatoInvalidoException {
        Funcion f = funcion("F1", sala("S1"), 120, manana, LocalTime.of(14, 49));
        assertEquals(LocalTime.of(16, 49), f.getHoraFin());
    }

    @Test
    void buscarAsiento_existente_devuelveAsientoDeLaFuncion() throws CinemaException {
        Funcion f = funcion("F1", sala("S1"), 100, manana, LocalTime.NOON);
        assertEquals("B2", f.buscarAsiento(" b2 ").getCodigo());
    }

    @Test
    void buscarAsiento_inexistente_lanzaAsientoNoEncontradoException() throws DatoInvalidoException {
        Funcion f = funcion("F1", sala("S1"), 100, manana, LocalTime.NOON);
        assertThrows(AsientoNoEncontradoException.class, () -> f.buscarAsiento("Z9"));
    }

    @Test
    void contarYOcupacion_conAsientosOcupados_calculaCorrectamente() throws CinemaException {
        Funcion f = funcion("F1", sala("S1"), 100, manana, LocalTime.NOON);
        assertEquals(6, f.contarDisponibles());
        assertEquals(0.0, f.calcularOcupacion(), 0.001);
        f.buscarAsiento("A1").ocupar();
        f.buscarAsiento("A2").ocupar();
        f.buscarAsiento("A3").ocupar();
        assertEquals(3, f.contarDisponibles());
        assertEquals(3, f.contarOcupados());
        assertEquals(50.0, f.calcularOcupacion(), 0.001);
    }

    @Test
    void seCruzaCon_terminaCuandoLaOtraEmpieza_noSeCruza() throws DatoInvalidoException {
        Sala s = sala("S1");
        Funcion a = funcion("F1", s, 120, manana, LocalTime.of(14, 49));
        Funcion b = funcion("F2", s, 120, manana, LocalTime.of(16, 49));
        assertFalse(a.seCruzaCon(b));
        assertFalse(b.seCruzaCon(a));
    }

    @Test
    void seCruzaCon_empiezaUnMinutoAntesDeQueTermine_seCruza() throws DatoInvalidoException {
        Sala s = sala("S1");
        Funcion a = funcion("F1", s, 120, manana, LocalTime.of(14, 49));
        Funcion b = funcion("F2", s, 120, manana, LocalTime.of(16, 48));
        assertTrue(a.seCruzaCon(b));
        assertTrue(b.seCruzaCon(a));
    }

    @Test
    void seCruzaCon_funcionQuePasaDeMedianoche_seCruzaConLaDelDiaSiguiente()
            throws DatoInvalidoException {
        Sala s = sala("S1");
        Funcion a = funcion("F1", s, 120, manana, LocalTime.of(23, 0));
        Funcion b = funcion("F2", s, 60, manana.plusDays(1), LocalTime.of(0, 10));
        assertTrue(a.seCruzaCon(b));
        assertTrue(b.seCruzaCon(a));
    }

    @Test
    void seCruzaCon_funcionQuePasaDeMedianoche_noSeCruzaSiLaOtraEmpiezaAlTerminar()
            throws DatoInvalidoException {
        Sala s = sala("S1");
        Funcion a = funcion("F1", s, 120, manana, LocalTime.of(23, 0));
        Funcion b = funcion("F2", s, 60, manana.plusDays(1), LocalTime.of(1, 0));
        assertFalse(a.seCruzaCon(b));
    }

    @Test
    void seCruzaCon_mismaHoraEnOtroDia_noSeCruza() throws DatoInvalidoException {
        Sala s = sala("S1");
        Funcion a = funcion("F1", s, 120, manana, LocalTime.of(14, 0));
        Funcion b = funcion("F2", s, 120, manana.plusDays(1), LocalTime.of(14, 0));
        assertFalse(a.seCruzaCon(b));
    }

    @Test
    void seCruzaCon_salasDistintas_noSeCruza() throws DatoInvalidoException {
        Funcion a = funcion("F1", sala("S1"), 120, manana, LocalTime.of(14, 0));
        Funcion b = funcion("F2", sala("S2"), 120, manana, LocalTime.of(14, 0));
        assertFalse(a.seCruzaCon(b));
    }

    @Test
    void seCruzaCon_funcionCancelada_noSeCruza() throws CinemaException {
        Sala s = sala("S1");
        Funcion a = funcion("F1", s, 120, manana, LocalTime.of(14, 0));
        Funcion b = funcion("F2", s, 120, manana, LocalTime.of(14, 30));
        assertTrue(a.seCruzaCon(b));
        b.cancelar();
        assertFalse(a.seCruzaCon(b));
        assertFalse(b.seCruzaCon(a));
    }

    @Test
    void seCruzaCon_funcionFinalizada_noSeCruza() throws CinemaException {
        Sala s = sala("S1");
        Funcion a = funcion("F1", s, 120, manana, LocalTime.of(14, 0));
        Funcion b = funcion("F2", s, 120, manana, LocalTime.of(14, 30));
        a.finalizar();
        assertFalse(a.seCruzaCon(b));
    }

    @Test
    void finalizar_funcionProgramada_quedaFinalizada() throws CinemaException {
        Funcion f = funcion("F1", sala("S1"), 100, manana, LocalTime.NOON);
        f.finalizar();
        assertEquals(EstadoFuncion.FINALIZADA, f.getEstado());
        assertFalse(f.estaProgramada());
    }

    @Test
    void finalizar_funcionNoProgramada_lanzaFuncionNoDisponibleException() throws CinemaException {
        Funcion f = funcion("F1", sala("S1"), 100, manana, LocalTime.NOON);
        f.cancelar();
        assertThrows(FuncionNoDisponibleException.class, f::finalizar);
    }

    @Test
    void cancelar_funcionProgramada_quedaCancelada() throws CinemaException {
        Funcion f = funcion("F1", sala("S1"), 100, manana, LocalTime.NOON);
        f.cancelar();
        assertEquals(EstadoFuncion.CANCELADA, f.getEstado());
    }

    @Test
    void cancelar_funcionNoProgramada_lanzaFuncionNoDisponibleException() throws CinemaException {
        Funcion f = funcion("F1", sala("S1"), 100, manana, LocalTime.NOON);
        f.finalizar();
        assertThrows(FuncionNoDisponibleException.class, f::cancelar);
    }

    @Test
    void asientoOcupado_noSePuedeOcuparDeNuevo() throws CinemaException {
        Funcion f = funcion("F1", sala("S1"), 100, manana, LocalTime.NOON);
        f.buscarAsiento("A1").ocupar();
        assertThrows(AsientoOcupadoException.class, () -> f.buscarAsiento("A1").ocupar());
    }
}
