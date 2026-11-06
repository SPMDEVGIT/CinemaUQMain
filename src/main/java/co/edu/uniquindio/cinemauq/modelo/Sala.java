package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.enumeraciones.EstadoSala;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sala de cine con una distribución de filas y asientos por fila.
 */
public class Sala {

    private final String codigo;
    private String nombre;
    private int filas;
    private int asientosPorFila;
    private EstadoSala estado;
    private List<Asiento> asientos;

    /**
     * Crea una sala DISPONIBLE y genera sus asientos.
     *
     * @param codigo código único (se guarda en mayúsculas)
     * @param nombre nombre de la sala
     * @param filas cantidad de filas (1 a 26, una letra por fila)
     * @param asientosPorFila asientos en cada fila (al menos 1)
     * @throws DatoInvalidoException si algún dato es nulo, vacío o fuera de rango
     */
    public Sala(String codigo, String nombre, int filas, int asientosPorFila)
            throws DatoInvalidoException {
        if (codigo == null || codigo.isBlank()) {
            throw new DatoInvalidoException("El código de la sala es obligatorio");
        }
        validarNombre(nombre);
        validarDistribucion(filas, asientosPorFila);
        this.codigo = codigo.trim().toUpperCase();
        this.nombre = nombre.trim();
        this.filas = filas;
        this.asientosPorFila = asientosPorFila;
        this.estado = EstadoSala.DISPONIBLE;
        generarAsientos();
    }

    /** @return el código de la sala */
    public String getCodigo() {
        return codigo;
    }

    /** @return el nombre de la sala */
    public String getNombre() {
        return nombre;
    }

    /** @return la cantidad de filas */
    public int getFilas() {
        return filas;
    }

    /** @return la cantidad de asientos por fila */
    public int getAsientosPorFila() {
        return asientosPorFila;
    }

    /** @return el estado de la sala */
    public EstadoSala getEstado() {
        return estado;
    }

    /** @return los asientos de la sala (lista no modificable) */
    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }

    /** @return la capacidad, es decir, la cantidad de asientos */
    public int getCapacidad() {
        return asientos.size();
    }

    /**
     * Crea una lista nueva con asientos nuevos, todos disponibles.
     * Es la base de los asientos de cada función.
     *
     * @return copia independiente de los asientos de la sala
     */
    public List<Asiento> copiarAsientos() {
        List<Asiento> copia = new ArrayList<>();
        for (Asiento original : asientos) {
            copia.add(new Asiento(original.getFila(), original.getNumero()));
        }
        return copia;
    }

    /**
     * Indica si la sala está disponible.
     *
     * @return true si el estado es DISPONIBLE
     */
    public boolean estaDisponible() {
        return estado == EstadoSala.DISPONIBLE;
    }

    /** Marca la sala como FUERA_DE_SERVICIO. */
    public void deshabilitar() {
        estado = EstadoSala.FUERA_DE_SERVICIO;
    }

    /** Marca la sala como DISPONIBLE. */
    public void habilitar() {
        estado = EstadoSala.DISPONIBLE;
    }

    /**
     * Cambia el nombre.
     *
     * @param nombre nuevo nombre (obligatorio)
     * @throws DatoInvalidoException si es nulo o vacío
     */
    public void setNombre(String nombre) throws DatoInvalidoException {
        validarNombre(nombre);
        this.nombre = nombre.trim();
    }

    /**
     * Cambia la distribución y regenera los asientos.
     *
     * @param filas cantidad de filas (1 a 26)
     * @param asientosPorFila asientos por fila (al menos 1)
     * @throws DatoInvalidoException si algún valor está fuera de rango
     */
    public void setDistribucion(int filas, int asientosPorFila) throws DatoInvalidoException {
        validarDistribucion(filas, asientosPorFila);
        this.filas = filas;
        this.asientosPorFila = asientosPorFila;
        generarAsientos();
    }

    /** Reconstruye la lista de asientos: A1...An, B1...Bn, etc. */
    private void generarAsientos() {
        asientos = new ArrayList<>();
        for (int f = 0; f < filas; f++) {
            char letra = (char) ('A' + f);
            for (int n = 1; n <= asientosPorFila; n++) {
                asientos.add(new Asiento(letra, n));
            }
        }
    }

    private void validarNombre(String nombre) throws DatoInvalidoException {
        if (nombre == null || nombre.isBlank()) {
            throw new DatoInvalidoException("El nombre de la sala es obligatorio");
        }
    }

    private void validarDistribucion(int filas, int asientosPorFila) throws DatoInvalidoException {
        if (filas < 1 || filas > 26) {
            throw new DatoInvalidoException("Las filas deben estar entre 1 y 26");
        }
        if (asientosPorFila < 1) {
            throw new DatoInvalidoException("Los asientos por fila deben ser al menos 1");
        }
    }
}
