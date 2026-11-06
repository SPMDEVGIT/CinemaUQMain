package co.edu.uniquindio.cinemauq.util;

import co.edu.uniquindio.cinemauq.modelo.Funcion;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javafx.util.StringConverter;

/**
 * Formatos de presentación: pesos colombianos sin decimales, fechas dd/MM/yyyy y horas HH:mm.
 */
public final class Formato {

    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private Formato() {
    }

    /**
     * Da formato de pesos colombianos sin decimales, por ejemplo $15.000.
     *
     * @param valor valor en pesos
     * @return texto con el valor formateado
     */
    public static String moneda(double valor) {
        DecimalFormatSymbols simbolos = new DecimalFormatSymbols(Locale.ROOT);
        simbolos.setGroupingSeparator('.');
        return "$" + new DecimalFormat("#,##0", simbolos).format(Math.round(valor));
    }

    /**
     * Da formato a un porcentaje, sin decimales si es entero (20 %) y con ellos si no (12.5 %).
     *
     * @param porcentaje valor entre 0 y 100
     * @return texto con el porcentaje
     */
    public static String porcentaje(double porcentaje) {
        if (porcentaje == Math.rint(porcentaje)) {
            return (long) porcentaje + " %";
        }
        return porcentaje + " %";
    }

    /**
     * @param fecha fecha a formatear
     * @return la fecha como dd/MM/yyyy
     */
    public static String fecha(LocalDate fecha) {
        return fecha.format(FECHA);
    }

    /**
     * @param hora hora a formatear
     * @return la hora como HH:mm
     */
    public static String hora(LocalTime hora) {
        return hora.format(HORA);
    }

    /**
     * @param fechaHora fecha y hora a formatear
     * @return el texto como dd/MM/yyyy HH:mm
     */
    public static String fechaHora(LocalDateTime fechaHora) {
        return fechaHora.format(FECHA_HORA);
    }

    /**
     * Describe una función en una línea, por ejemplo
     * "F001 · Interestelar · Sala 1 · 06/11/2026 14:00".
     *
     * @param funcion función a describir
     * @return texto para mostrar en listas desplegables
     */
    public static String funcion(Funcion funcion) {
        return funcion.getCodigo() + " · " + funcion.getPelicula().getNombre() + " · "
                + funcion.getSala().getNombre() + " · " + fecha(funcion.getFecha()) + " "
                + hora(funcion.getHoraInicio());
    }

    /**
     * Convertidor para mostrar funciones en un ComboBox con el texto de {@link #funcion}.
     *
     * @return convertidor de funciones a texto
     */
    public static StringConverter<Funcion> convertidorFuncion() {
        return new StringConverter<>() {
            @Override
            public String toString(Funcion funcion) {
                return funcion == null ? "" : funcion(funcion);
            }

            @Override
            public Funcion fromString(String texto) {
                return null;
            }
        };
    }
}
