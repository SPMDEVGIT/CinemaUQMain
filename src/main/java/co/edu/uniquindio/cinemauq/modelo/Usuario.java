package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.enumeraciones.Rol;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;

/**
 * Usuario del sistema con credenciales y rol.
 */
public class Usuario extends Persona {

    private final String nombreUsuario;
    private final String contrasena;
    private final Rol rol;

    /**
     * Crea un usuario.
     *
     * @param identificacion documento de identidad
     * @param nombre nombre completo
     * @param nombreUsuario nombre con el que inicia sesión
     * @param contrasena contraseña
     * @param rol rol dentro del sistema
     * @throws DatoInvalidoException si algún dato es nulo o vacío
     */
    public Usuario(String identificacion, String nombre, String nombreUsuario,
                   String contrasena, Rol rol) throws DatoInvalidoException {
        super(identificacion, nombre);
        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new DatoInvalidoException("El nombre de usuario es obligatorio");
        }
        if (contrasena == null || contrasena.isEmpty()) {
            throw new DatoInvalidoException("La contraseña es obligatoria");
        }
        if (rol == null) {
            throw new DatoInvalidoException("El rol es obligatorio");
        }
        this.nombreUsuario = nombreUsuario.trim();
        this.contrasena = contrasena;
        this.rol = rol;
    }

    /** @return el nombre de usuario */
    public String getNombreUsuario() {
        return nombreUsuario;
    }

    /** @return el rol del usuario */
    public Rol getRol() {
        return rol;
    }

    /**
     * Indica si las credenciales coinciden exactamente con las del usuario.
     *
     * @param nombreUsuario nombre de usuario ingresado
     * @param contrasena contraseña ingresada
     * @return true si ambos coinciden
     */
    public boolean validarCredenciales(String nombreUsuario, String contrasena) {
        return this.nombreUsuario.equals(nombreUsuario) && this.contrasena.equals(contrasena);
    }
}
