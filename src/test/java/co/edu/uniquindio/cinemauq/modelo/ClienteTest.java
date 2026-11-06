package co.edu.uniquindio.cinemauq.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import co.edu.uniquindio.cinemauq.enumeraciones.TipoCliente;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import org.junit.jupiter.api.Test;

class ClienteTest {

    @Test
    void constructor_datosValidos_creaCliente() throws DatoInvalidoException {
        Cliente c = new Cliente(" 100 ", " Ana Gómez ", TipoCliente.ESTUDIANTE);
        assertEquals("100", c.getIdentificacion());
        assertEquals("Ana Gómez", c.getNombre());
        assertEquals(TipoCliente.ESTUDIANTE, c.getTipoCliente());
    }

    @Test
    void constructor_identificacionNula_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Cliente(null, "Ana", TipoCliente.GENERAL));
    }

    @Test
    void constructor_nombreVacio_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Cliente("100", "  ", TipoCliente.GENERAL));
    }

    @Test
    void constructor_tipoClienteNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> new Cliente("100", "Ana", null));
    }
}
