package co.edu.uniquindio.cinemauq.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.uniquindio.cinemauq.enumeraciones.EstadoSala;
import co.edu.uniquindio.cinemauq.excepciones.AsientoOcupadoException;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import java.util.List;
import org.junit.jupiter.api.Test;

class SalaTest {

    @Test
    void constructor_datosValidos_creaSalaConAsientosOrdenados() throws DatoInvalidoException {
        Sala s = new Sala(" s01 ", "Sala 1", 2, 3);
        assertEquals("S01", s.getCodigo());
        assertEquals("Sala 1", s.getNombre());
        assertEquals(EstadoSala.DISPONIBLE, s.getEstado());
        assertEquals(6, s.getCapacidad());
        assertEquals("A1", s.getAsientos().get(0).getCodigo());
        assertEquals("A3", s.getAsientos().get(2).getCodigo());
        assertEquals("B1", s.getAsientos().get(3).getCodigo());
        assertEquals("B3", s.getAsientos().get(5).getCodigo());
    }

    @Test
    void constructor_codigoVacio_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> new Sala(" ", "Sala", 2, 3));
    }

    @Test
    void constructor_nombreNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> new Sala("S1", null, 2, 3));
    }

    @Test
    void constructor_cero_filas_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> new Sala("S1", "Sala", 0, 3));
    }

    @Test
    void constructor_masDe26Filas_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> new Sala("S1", "Sala", 27, 3));
    }

    @Test
    void constructor_26Filas_esValido() throws DatoInvalidoException {
        Sala s = new Sala("S1", "Sala", 26, 1);
        assertEquals("Z1", s.getAsientos().get(25).getCodigo());
    }

    @Test
    void constructor_asientosPorFilaCero_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> new Sala("S1", "Sala", 2, 0));
    }

    @Test
    void getAsientos_listaNoModificable() throws DatoInvalidoException {
        Sala s = new Sala("S1", "Sala", 1, 1);
        List<Asiento> lista = s.getAsientos();
        assertThrows(UnsupportedOperationException.class, () -> lista.add(new Asiento('X', 1)));
    }

    @Test
    void copiarAsientos_devuelveAsientosNuevosDisponibles() throws DatoInvalidoException,
            AsientoOcupadoException {
        Sala s = new Sala("S1", "Sala", 2, 2);
        s.getAsientos().get(0).ocupar();
        List<Asiento> copia = s.copiarAsientos();
        assertEquals(4, copia.size());
        assertNotSame(s.getAsientos().get(0), copia.get(0));
        assertEquals("A1", copia.get(0).getCodigo());
        for (Asiento a : copia) {
            assertTrue(a.estaDisponible());
        }
    }

    @Test
    void deshabilitarYHabilitar_cambianEstado() throws DatoInvalidoException {
        Sala s = new Sala("S1", "Sala", 1, 1);
        s.deshabilitar();
        assertFalse(s.estaDisponible());
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, s.getEstado());
        s.habilitar();
        assertTrue(s.estaDisponible());
    }

    @Test
    void setNombre_valido_actualiza() throws DatoInvalidoException {
        Sala s = new Sala("S1", "Sala", 1, 1);
        s.setNombre("Sala VIP");
        assertEquals("Sala VIP", s.getNombre());
    }

    @Test
    void setNombre_vacio_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Sala s = new Sala("S1", "Sala", 1, 1);
        assertThrows(DatoInvalidoException.class, () -> s.setNombre(""));
    }

    @Test
    void setDistribucion_valida_regeneraAsientos() throws DatoInvalidoException {
        Sala s = new Sala("S1", "Sala", 2, 2);
        s.setDistribucion(3, 4);
        assertEquals(3, s.getFilas());
        assertEquals(4, s.getAsientosPorFila());
        assertEquals(12, s.getCapacidad());
        assertEquals("C4", s.getAsientos().get(11).getCodigo());
    }

    @Test
    void setDistribucion_invalida_lanzaExcepcionYNoCambiaNada() throws DatoInvalidoException {
        Sala s = new Sala("S1", "Sala", 2, 2);
        assertThrows(DatoInvalidoException.class, () -> s.setDistribucion(30, 4));
        assertThrows(DatoInvalidoException.class, () -> s.setDistribucion(3, 0));
        assertEquals(4, s.getCapacidad());
    }
}
