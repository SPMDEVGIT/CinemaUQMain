package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;

/**
 * Persona con identificación y nombre. Es la base de usuarios y clientes.
 */
public abstract class Persona {

    private final String identificacion;
    private final String nombre;

    /**
     * Crea una persona.
     *
     * @param identificacion documento de identidad (obligatorio)
     * @param nombre nombre completo (obligatorio)
     * @throws DatoInvalidoException si algún dato es nulo o vacío
     */
    protected Persona(String identificacion, String nombre) throws DatoInvalidoException {
        if (identificacion == null || identificacion.isBlank()) {
            throw new DatoInvalidoException("La identificación es obligatoria");
        }
        if (nombre == null || nombre.isBlank()) {
            throw new DatoInvalidoException("El nombre es obligatorio");
        }
        this.identificacion = identificacion.trim();
        this.nombre = nombre.trim();
    }

    /** @return la identificación de la persona */
    public String getIdentificacion() {
        return identificacion;
    }

    /** @return el nombre de la persona */
    public String getNombre() {
        return nombre;
    }
}
