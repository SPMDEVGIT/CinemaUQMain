package co.edu.uniquindio.cinemauq.enumeraciones;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class EnumeracionesTest {

    @Test
    void tipoCliente_toString_devuelveTextoLegible() {
        assertEquals("General", TipoCliente.GENERAL.toString());
        assertEquals("Estudiante", TipoCliente.ESTUDIANTE.toString());
        assertEquals("Niño", TipoCliente.NINO.toString());
        assertEquals("Adulto mayor", TipoCliente.ADULTO_MAYOR.toString());
    }

    @Test
    void genero_toString_devuelveTextoLegible() {
        assertEquals("Acción", Genero.ACCION.toString());
        assertEquals("Comedia", Genero.COMEDIA.toString());
        assertEquals("Drama", Genero.DRAMA.toString());
        assertEquals("Terror", Genero.TERROR.toString());
        assertEquals("Ciencia ficción", Genero.CIENCIA_FICCION.toString());
        assertEquals("Animación", Genero.ANIMACION.toString());
    }
}
