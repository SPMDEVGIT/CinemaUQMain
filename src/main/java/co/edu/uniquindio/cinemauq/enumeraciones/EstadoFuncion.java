package co.edu.uniquindio.cinemauq.enumeraciones;

/**
 * Estado de una función.
 */
public enum EstadoFuncion {

    PROGRAMADA("Programada"),
    FINALIZADA("Finalizada"),
    CANCELADA("Cancelada");

    private final String texto;

    EstadoFuncion(String texto) {
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
