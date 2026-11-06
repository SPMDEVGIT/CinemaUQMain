package co.edu.uniquindio.cinemauq.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.uniquindio.cinemauq.enumeraciones.EstadoAsiento;
import co.edu.uniquindio.cinemauq.excepciones.AsientoOcupadoException;
import org.junit.jupiter.api.Test;

class AsientoTest {

    @Test
    void constructor_filaYNumero_generaCodigoYEstadoDisponible() {
        Asiento a = new Asiento('B', 3);
        assertEquals("B3", a.getCodigo());
        assertEquals('B', a.getFila());
        assertEquals(3, a.getNumero());
        assertEquals(EstadoAsiento.DISPONIBLE, a.getEstado());
        assertTrue(a.estaDisponible());
    }

    @Test
    void ocupar_asientoDisponible_quedaOcupado() throws AsientoOcupadoException {
        Asiento a = new Asiento('A', 1);
        a.ocupar();
        assertFalse(a.estaDisponible());
        assertEquals(EstadoAsiento.OCUPADO, a.getEstado());
    }

    @Test
    void ocupar_asientoOcupado_lanzaAsientoOcupadoException() throws AsientoOcupadoException {
        Asiento a = new Asiento('B', 3);
        a.ocupar();
        AsientoOcupadoException ex = assertThrows(AsientoOcupadoException.class, a::ocupar);
        assertTrue(ex.getMessage().contains("B3"));
    }

    @Test
    void liberar_asientoOcupado_quedaDisponible() throws AsientoOcupadoException {
        Asiento a = new Asiento('A', 1);
        a.ocupar();
        a.liberar();
        assertTrue(a.estaDisponible());
    }
}
