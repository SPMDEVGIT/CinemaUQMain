package co.edu.uniquindio.cinemauq.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.uniquindio.cinemauq.enumeraciones.Clasificacion;
import co.edu.uniquindio.cinemauq.enumeraciones.EstadoPelicula;
import co.edu.uniquindio.cinemauq.enumeraciones.Genero;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import org.junit.jupiter.api.Test;

class PeliculaTest {

    private Pelicula crear() throws DatoInvalidoException {
        return new Pelicula(" p001 ", "Interestelar", Genero.CIENCIA_FICCION, 169,
                Clasificacion.MAYORES_12);
    }

    @Test
    void constructor_datosValidos_creaPeliculaDisponible() throws DatoInvalidoException {
        Pelicula p = crear();
        assertEquals("P001", p.getCodigo());
        assertEquals("Interestelar", p.getNombre());
        assertEquals(Genero.CIENCIA_FICCION, p.getGenero());
        assertEquals(169, p.getDuracionMinutos());
        assertEquals(Clasificacion.MAYORES_12, p.getClasificacion());
        assertEquals(EstadoPelicula.DISPONIBLE, p.getEstado());
        assertTrue(p.estaDisponible());
    }

    @Test
    void constructor_codigoVacio_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Pelicula("", "X", Genero.DRAMA, 90, Clasificacion.TODOS));
    }

    @Test
    void constructor_nombreNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Pelicula("P1", null, Genero.DRAMA, 90, Clasificacion.TODOS));
    }

    @Test
    void constructor_generoNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Pelicula("P1", "X", null, 90, Clasificacion.TODOS));
    }

    @Test
    void constructor_duracionCero_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Pelicula("P1", "X", Genero.DRAMA, 0, Clasificacion.TODOS));
    }

    @Test
    void constructor_clasificacionNula_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Pelicula("P1", "X", Genero.DRAMA, 90, null));
    }

    @Test
    void setters_valoresValidos_actualizanDatos() throws DatoInvalidoException {
        Pelicula p = crear();
        p.setNombre("Otra");
        p.setGenero(Genero.ACCION);
        p.setDuracionMinutos(100);
        p.setClasificacion(Clasificacion.MAYORES_18);
        assertEquals("Otra", p.getNombre());
        assertEquals(Genero.ACCION, p.getGenero());
        assertEquals(100, p.getDuracionMinutos());
        assertEquals(Clasificacion.MAYORES_18, p.getClasificacion());
    }

    @Test
    void setNombre_vacio_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Pelicula p = crear();
        assertThrows(DatoInvalidoException.class, () -> p.setNombre("  "));
    }

    @Test
    void setGenero_nulo_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Pelicula p = crear();
        assertThrows(DatoInvalidoException.class, () -> p.setGenero(null));
    }

    @Test
    void setDuracionMinutos_negativa_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Pelicula p = crear();
        assertThrows(DatoInvalidoException.class, () -> p.setDuracionMinutos(-5));
        assertEquals(169, p.getDuracionMinutos());
    }

    @Test
    void setClasificacion_nula_lanzaDatoInvalidoException() throws DatoInvalidoException {
        Pelicula p = crear();
        assertThrows(DatoInvalidoException.class, () -> p.setClasificacion(null));
    }

    @Test
    void deshabilitar_peliculaDisponible_quedaNoDisponible() throws DatoInvalidoException {
        Pelicula p = crear();
        p.deshabilitar();
        assertFalse(p.estaDisponible());
        assertEquals(EstadoPelicula.NO_DISPONIBLE, p.getEstado());
    }
}
