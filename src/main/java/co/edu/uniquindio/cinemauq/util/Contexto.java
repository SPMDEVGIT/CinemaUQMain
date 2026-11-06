package co.edu.uniquindio.cinemauq.util;

import co.edu.uniquindio.cinemauq.modelo.Cinema;
import co.edu.uniquindio.cinemauq.modelo.Usuario;

/**
 * Datos compartidos por toda la interfaz: la única instancia de {@link Cinema}
 * y el usuario que tiene la sesión iniciada.
 */
public final class Contexto {

    private static Cinema cinema;
    private static Usuario usuario;

    private Contexto() {
    }

    /** @return el cinema de la aplicación */
    public static Cinema getCinema() {
        return cinema;
    }

    /** @param nuevoCinema el cinema que usará la aplicación */
    public static void setCinema(Cinema nuevoCinema) {
        cinema = nuevoCinema;
    }

    /** @return el usuario en sesión, o null si no hay sesión */
    public static Usuario getUsuario() {
        return usuario;
    }

    /** @param nuevoUsuario el usuario que inició sesión */
    public static void setUsuario(Usuario nuevoUsuario) {
        usuario = nuevoUsuario;
    }

    /** Cierra la sesión: olvida al usuario actual. */
    public static void cerrarSesion() {
        usuario = null;
    }
}
