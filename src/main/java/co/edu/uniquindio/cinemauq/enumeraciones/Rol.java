package co.edu.uniquindio.cinemauq.enumeraciones;

/**
 * Rol de un usuario del sistema.
 */
public enum Rol {

    ADMINISTRADOR("Administrador"),
    OPERADOR("Operador");

    private final String texto;

    Rol(String texto) {
        this.texto = texto;
    }

    /**
     * Devuelve el texto legible.
     */
    @Override
    public String toString() {
        return texto;
    }
}
