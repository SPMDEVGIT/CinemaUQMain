package co.edu.uniquindio.cinemauq.enumeraciones;

/**
 * Géneros de una película.
 */
public enum Genero {

    ACCION("Acción"),
    COMEDIA("Comedia"),
    DRAMA("Drama"),
    TERROR("Terror"),
    CIENCIA_FICCION("Ciencia ficción"),
    ANIMACION("Animación");

    private final String texto;

    Genero(String texto) {
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
