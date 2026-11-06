package co.edu.uniquindio.cinemauq.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import co.edu.uniquindio.cinemauq.enumeraciones.Clasificacion;
import co.edu.uniquindio.cinemauq.enumeraciones.Genero;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import org.junit.jupiter.api.Test;

class FormatoTest {

    @Test
    void moneda_valorEntero_usaPuntoComoSeparadorDeMiles() {
        assertEquals("$15.000", Formato.moneda(15000));
        assertEquals("$1.234.567", Formato.moneda(1234567));
        assertEquals("$0", Formato.moneda(0));
    }

    @Test
    void moneda_valorConDecimales_losRedondea() {
        assertEquals("$11.250", Formato.moneda(11250.0));
        assertEquals("$8.400", Formato.moneda(8399.6));
    }

    @Test
    void porcentaje_enteroSinDecimalesYFraccionConDecimales() {
        assertEquals("20 %", Formato.porcentaje(20));
        assertEquals("0 %", Formato.porcentaje(0));
        assertEquals("12.5 %", Formato.porcentaje(12.5));
    }

    @Test
    void fecha_formatoDiaMesAnio() {
        assertEquals("06/11/2026", Formato.fecha(LocalDate.of(2026, 11, 6)));
    }

    @Test
    void hora_formato24Horas() {
        assertEquals("14:05", Formato.hora(LocalTime.of(14, 5)));
    }

    @Test
    void funcion_describeCodigoPeliculaSalaFechaYHora() throws CinemaException {
        Pelicula pelicula = new Pelicula("P1", "Interestelar", Genero.CIENCIA_FICCION, 169,
                Clasificacion.MAYORES_12);
        Sala sala = new Sala("S1", "Sala 1", 2, 2);
        Funcion funcion = new Funcion("F001", pelicula, sala, LocalDate.of(2026, 11, 6),
                LocalTime.of(14, 0), 15000);
        assertEquals("F001 · Interestelar · Sala 1 · 06/11/2026 14:00", Formato.funcion(funcion));
    }

    @Test
    void fechaHora_combinaFechaYHora() {
        assertEquals("06/11/2026 18:30", Formato.fechaHora(LocalDateTime.of(2026, 11, 6, 18, 30)));
    }
}
