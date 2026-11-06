package co.edu.uniquindio.cinemauq.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.uniquindio.cinemauq.enumeraciones.Rol;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    void constructor_datosValidos_creaUsuario() throws DatoInvalidoException {
        Usuario u = new Usuario("1", "Admin", "admin", "clave", Rol.ADMINISTRADOR);
        assertEquals("1", u.getIdentificacion());
        assertEquals("Admin", u.getNombre());
        assertEquals("admin", u.getNombreUsuario());
        assertEquals(Rol.ADMINISTRADOR, u.getRol());
    }

    @Test
    void constructor_identificacionVacia_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Usuario(" ", "Admin", "admin", "clave", Rol.OPERADOR));
    }

    @Test
    void constructor_nombreNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Usuario("1", null, "admin", "clave", Rol.OPERADOR));
    }

    @Test
    void constructor_nombreUsuarioVacio_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Usuario("1", "Admin", "", "clave", Rol.OPERADOR));
    }

    @Test
    void constructor_contrasenaNula_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Usuario("1", "Admin", "admin", null, Rol.OPERADOR));
    }

    @Test
    void constructor_rolNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> new Usuario("1", "Admin", "admin", "clave", null));
    }

    @Test
    void validarCredenciales_correctas_devuelveTrue() throws DatoInvalidoException {
        Usuario u = new Usuario("1", "Admin", "admin", "clave", Rol.ADMINISTRADOR);
        assertTrue(u.validarCredenciales("admin", "clave"));
    }

    @Test
    void validarCredenciales_incorrectas_devuelveFalse() throws DatoInvalidoException {
        Usuario u = new Usuario("1", "Admin", "admin", "clave", Rol.ADMINISTRADOR);
        assertFalse(u.validarCredenciales("admin", "otra"));
        assertFalse(u.validarCredenciales("otro", "clave"));
        assertFalse(u.validarCredenciales("ADMIN", "clave"));
    }
}
