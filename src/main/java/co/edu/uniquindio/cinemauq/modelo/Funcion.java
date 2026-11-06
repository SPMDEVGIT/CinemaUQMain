package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.enumeraciones.EstadoFuncion;
import co.edu.uniquindio.cinemauq.excepciones.AsientoNoEncontradoException;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionNoDisponibleException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

/**
 * Presentación de una película en una sala, en una fecha y hora.
 * Cada función tiene sus propios asientos.
 */
public class Funcion {

    private final String codigo;
    private final Pelicula pelicula;
    private final Sala sala;
    private final LocalDate fecha;
    private final LocalTime horaInicio;
    private final double valorBase;
    private EstadoFuncion estado;
    private final List<Asiento> asientos;

    /**
     * Crea una función PROGRAMADA con una copia propia de los asientos de la sala.
     * No valida disponibilidad ni cruces: eso lo hace Cinema.
     *
     * @param codigo código único (se guarda en mayúsculas)
     * @param pelicula película que se presenta
     * @param sala sala donde se presenta
     * @param fecha fecha de la función
     * @param horaInicio hora de inicio
     * @param valorBase valor base de la entrada (mayor que 0)
     * @throws DatoInvalidoException si algún dato es nulo o el valor no es mayor que 0
     */
    public Funcion(String codigo, Pelicula pelicula, Sala sala, LocalDate fecha,
                   LocalTime horaInicio, double valorBase) throws DatoInvalidoException {
        if (codigo == null || codigo.isBlank()) {
            throw new DatoInvalidoException("El código de la función es obligatorio");
        }
        if (pelicula == null) {
            throw new DatoInvalidoException("La película de la función es obligatoria");
        }
        if (sala == null) {
            throw new DatoInvalidoException("La sala de la función es obligatoria");
        }
        if (fecha == null) {
            throw new DatoInvalidoException("La fecha de la función es obligatoria");
        }
        if (horaInicio == null) {
            throw new DatoInvalidoException("La hora de inicio de la función es obligatoria");
        }
        if (Double.isNaN(valorBase) || valorBase <= 0) {
            throw new DatoInvalidoException("El valor base debe ser mayor que 0");
        }
        this.codigo = codigo.trim().toUpperCase();
        this.pelicula = pelicula;
        this.sala = sala;
        this.fecha = fecha;
        this.horaInicio = horaInicio;
        this.valorBase = valorBase;
        this.estado = EstadoFuncion.PROGRAMADA;
        this.asientos = sala.copiarAsientos();
    }

    /** @return el código de la función */
    public String getCodigo() {
        return codigo;
    }

    /** @return la película */
    public Pelicula getPelicula() {
        return pelicula;
    }

    /** @return la sala */
    public Sala getSala() {
        return sala;
    }

    /** @return la fecha */
    public LocalDate getFecha() {
        return fecha;
    }

    /** @return la hora de inicio */
    public LocalTime getHoraInicio() {
        return horaInicio;
    }

    /** @return el valor base de la entrada */
    public double getValorBase() {
        return valorBase;
    }

    /** @return el estado de la función */
    public EstadoFuncion getEstado() {
        return estado;
    }

    /** @return los asientos de la función (lista no modificable) */
    public List<Asiento> getAsientos() {
        return Collections.unmodifiableList(asientos);
    }

    /**
     * Calcula la hora de fin: hora de inicio más la duración de la película.
     *
     * @return hora de fin
     */
    public LocalTime getHoraFin() {
        return horaInicio.plusMinutes(pelicula.getDuracionMinutos());
    }

    /**
     * Busca un asiento de esta función por su código.
     *
     * @param codigo código del asiento, por ejemplo "B3"
     * @return el asiento de la función
     * @throws AsientoNoEncontradoException si no existe en la función
     */
    public Asiento buscarAsiento(String codigo) throws AsientoNoEncontradoException {
        String buscado = codigo == null ? "" : codigo.trim().toUpperCase();
        for (Asiento asiento : asientos) {
            if (asiento.getCodigo().equals(buscado)) {
                return asiento;
            }
        }
        throw new AsientoNoEncontradoException(
                "El asiento " + buscado + " no existe en la función " + this.codigo);
    }

    /** @return la cantidad de asientos disponibles */
    public int contarDisponibles() {
        int cantidad = 0;
        for (Asiento asiento : asientos) {
            if (asiento.estaDisponible()) {
                cantidad++;
            }
        }
        return cantidad;
    }

    /** @return la cantidad de asientos ocupados */
    public int contarOcupados() {
        int cantidad = 0;
        for (Asiento asiento : asientos) {
            if (!asiento.estaDisponible()) {
                cantidad++;
            }
        }
        return cantidad;
    }

    /** @return el porcentaje de ocupación (0 a 100) */
    public double calcularOcupacion() {
        return contarOcupados() * 100.0 / asientos.size();
    }

    /**
     * Indica si esta función se solapa con otra.
     * No hay cruce si las salas son distintas o si alguna no está PROGRAMADA.
     * Terminar exactamente cuando la otra empieza no es cruce.
     *
     * @param otra la otra función
     * @return true si los intervalos de tiempo se solapan en la misma sala
     */
    public boolean seCruzaCon(Funcion otra) {
        if (!sala.getCodigo().equals(otra.sala.getCodigo())) {
            return false;
        }
        if (!estaProgramada() || !otra.estaProgramada()) {
            return false;
        }
        LocalDateTime inicioA = fecha.atTime(horaInicio);
        LocalDateTime finA = inicioA.plusMinutes(pelicula.getDuracionMinutos());
        LocalDateTime inicioB = otra.fecha.atTime(otra.horaInicio);
        LocalDateTime finB = inicioB.plusMinutes(otra.pelicula.getDuracionMinutos());
        return inicioA.isBefore(finB) && inicioB.isBefore(finA);
    }

    /**
     * Indica si la función está PROGRAMADA.
     *
     * @return true si el estado es PROGRAMADA
     */
    public boolean estaProgramada() {
        return estado == EstadoFuncion.PROGRAMADA;
    }

    /**
     * Marca la función como FINALIZADA.
     *
     * @throws FuncionNoDisponibleException si no estaba programada
     */
    public void finalizar() throws FuncionNoDisponibleException {
        validarProgramada();
        estado = EstadoFuncion.FINALIZADA;
    }

    /**
     * Marca la función como CANCELADA.
     *
     * @throws FuncionNoDisponibleException si no estaba programada
     */
    public void cancelar() throws FuncionNoDisponibleException {
        validarProgramada();
        estado = EstadoFuncion.CANCELADA;
    }

    private void validarProgramada() throws FuncionNoDisponibleException {
        if (!estaProgramada()) {
            throw new FuncionNoDisponibleException(
                    "La función " + codigo + " no está programada (estado: " + estado + ")");
        }
    }
}
