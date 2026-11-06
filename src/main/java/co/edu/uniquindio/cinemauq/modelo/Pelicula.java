package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.enumeraciones.Clasificacion;
import co.edu.uniquindio.cinemauq.enumeraciones.EstadoPelicula;
import co.edu.uniquindio.cinemauq.enumeraciones.Genero;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;

/**
 * Película de la cartelera.
 */
public class Pelicula {

    private final String codigo;
    private String nombre;
    private Genero genero;
    private int duracionMinutos;
    private Clasificacion clasificacion;
    private EstadoPelicula estado;

    /**
     * Crea una película en estado DISPONIBLE.
     *
     * @param codigo código único (se guarda en mayúsculas)
     * @param nombre nombre de la película
     * @param genero género
     * @param duracionMinutos duración en minutos (mayor que 0)
     * @param clasificacion clasificación por edad
     * @throws DatoInvalidoException si algún dato es nulo, vacío o fuera de rango
     */
    public Pelicula(String codigo, String nombre, Genero genero, int duracionMinutos,
                    Clasificacion clasificacion) throws DatoInvalidoException {
        if (codigo == null || codigo.isBlank()) {
            throw new DatoInvalidoException("El código de la película es obligatorio");
        }
        this.codigo = codigo.trim().toUpperCase();
        setNombre(nombre);
        setGenero(genero);
        setDuracionMinutos(duracionMinutos);
        setClasificacion(clasificacion);
        this.estado = EstadoPelicula.DISPONIBLE;
    }

    /** @return el código de la película */
    public String getCodigo() {
        return codigo;
    }

    /** @return el nombre de la película */
    public String getNombre() {
        return nombre;
    }

    /** @return el género */
    public Genero getGenero() {
        return genero;
    }

    /** @return la duración en minutos */
    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    /** @return la clasificación por edad */
    public Clasificacion getClasificacion() {
        return clasificacion;
    }

    /** @return el estado de la película */
    public EstadoPelicula getEstado() {
        return estado;
    }

    /**
     * Cambia el nombre.
     *
     * @param nombre nuevo nombre (obligatorio)
     * @throws DatoInvalidoException si es nulo o vacío
     */
    public void setNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.isBlank()) {
            throw new DatoInvalidoException("El nombre de la película es obligatorio");
        }
        this.nombre = nombre.trim();
    }

    /**
     * Cambia el género.
     *
     * @param genero nuevo género (obligatorio)
     * @throws DatoInvalidoException si es nulo
     */
    public void setGenero(Genero genero) throws DatoInvalidoException {
        if (genero == null) {
            throw new DatoInvalidoException("El género es obligatorio");
        }
        this.genero = genero;
    }

    /**
     * Cambia la duración.
     *
     * @param duracionMinutos nueva duración (mayor que 0)
     * @throws DatoInvalidoException si no es mayor que 0
     */
    public void setDuracionMinutos(int duracionMinutos) throws DatoInvalidoException {
        if (duracionMinutos <= 0) {
            throw new DatoInvalidoException("La duración debe ser mayor que 0 minutos");
        }
        this.duracionMinutos = duracionMinutos;
    }

    /**
     * Cambia la clasificación.
     *
     * @param clasificacion nueva clasificación (obligatoria)
     * @throws DatoInvalidoException si es nula
     */
    public void setClasificacion(Clasificacion clasificacion) throws DatoInvalidoException {
        if (clasificacion == null) {
            throw new DatoInvalidoException("La clasificación es obligatoria");
        }
        this.clasificacion = clasificacion;
    }

    /**
     * Indica si la película está disponible.
     *
     * @return true si el estado es DISPONIBLE
     */
    public boolean estaDisponible() {
        return estado == EstadoPelicula.DISPONIBLE;
    }

    /** Marca la película como NO_DISPONIBLE. */
    public void deshabilitar() {
        estado = EstadoPelicula.NO_DISPONIBLE;
    }
}
