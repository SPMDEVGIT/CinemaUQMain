package co.edu.uniquindio.cinemauq.enumeraciones;

/**
 * Clasificación por edad de una película.
 */
public enum Clasificacion {

    TODOS("Todos"),
    MAYORES_7("+7"),
    MAYORES_12("+12"),
    MAYORES_15("+15"),
    MAYORES_18("+18");

    private final String texto;

    Clasificacion(String texto) {
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
