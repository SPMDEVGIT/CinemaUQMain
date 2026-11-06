package co.edu.uniquindio.cinemauq.util;

import co.edu.uniquindio.cinemauq.enumeraciones.Clasificacion;
import co.edu.uniquindio.cinemauq.enumeraciones.Genero;
import co.edu.uniquindio.cinemauq.enumeraciones.Rol;
import co.edu.uniquindio.cinemauq.enumeraciones.TipoCliente;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Cinema;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.modelo.Usuario;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Carga datos de ejemplo en el cinema usando solo sus métodos públicos,
 * de modo que pasan por las mismas validaciones que cualquier dato real.
 */
public final class DatosPrueba {

    private DatosPrueba() {
    }

    /**
     * Carga usuarios, películas, salas, clientes, funciones y ventas de ejemplo.
     * Las fechas son relativas al día de hoy.
     *
     * @param cinema cinema donde se cargan los datos
     * @throws CinemaException si algún dato no pasa las validaciones
     */
    public static void cargar(Cinema cinema) throws CinemaException {
        cargarUsuarios(cinema);
        cargarPeliculas(cinema);
        cargarSalas(cinema);
        cargarClientes(cinema);
        cargarFuncionesYVentas(cinema);
    }

    private static void cargarUsuarios(Cinema cinema) throws CinemaException {
        cinema.registrarUsuario(new Usuario("1000000001", "Administrador CinemaUQ",
                "admin", "admin123", Rol.ADMINISTRADOR));
        cinema.registrarUsuario(new Usuario("1000000002", "Operador Taquilla",
                "operador", "operador123", Rol.OPERADOR));
    }

    private static void cargarPeliculas(Cinema cinema) throws CinemaException {
        cinema.registrarPelicula(new Pelicula("P001", "Interestelar",
                Genero.CIENCIA_FICCION, 169, Clasificacion.MAYORES_12));
        cinema.registrarPelicula(new Pelicula("P002", "Intensamente 2",
                Genero.ANIMACION, 96, Clasificacion.TODOS));
        cinema.registrarPelicula(new Pelicula("P003", "El conjuro",
                Genero.TERROR, 112, Clasificacion.MAYORES_15));
        cinema.registrarPelicula(new Pelicula("P004", "El último algoritmo",
                Genero.DRAMA, 120, Clasificacion.MAYORES_7));
    }

    private static void cargarSalas(Cinema cinema) throws CinemaException {
        cinema.registrarSala(new Sala("S01", "Sala 1", 6, 10));
        cinema.registrarSala(new Sala("S02", "Sala 2", 5, 8));
        cinema.registrarSala(new Sala("S03", "Sala 3", 4, 6));
        cinema.deshabilitarSala("S03");
    }

    private static void cargarClientes(Cinema cinema) throws CinemaException {
        cinema.registrarCliente(new Cliente("1094000001", "Ana Gómez", TipoCliente.ESTUDIANTE));
        cinema.registrarCliente(new Cliente("1094000002", "Carlos Ruiz", TipoCliente.GENERAL));
        cinema.registrarCliente(new Cliente("1094000003", "Sofía Ruiz", TipoCliente.NINO));
        cinema.registrarCliente(new Cliente("1094000004", "Jorge Pérez", TipoCliente.ADULTO_MAYOR));
    }

    private static void cargarFuncionesYVentas(Cinema cinema) throws CinemaException {
        LocalDate manana = LocalDate.now().plusDays(1);
        LocalDate pasadoManana = LocalDate.now().plusDays(2);

        cinema.programarFuncion("P001", "S01", manana, LocalTime.of(14, 0), 15000);   // F001
        cinema.programarFuncion("P001", "S01", manana, LocalTime.of(18, 0), 15000);   // F002
        cinema.programarFuncion("P004", "S02", manana, LocalTime.of(18, 30), 12000);  // F003
        cinema.programarFuncion("P002", "S02", pasadoManana, LocalTime.of(15, 0), 10000); // F004

        cinema.venderEntrada("F001", "1094000001", "A1");
        cinema.venderEntrada("F001", "1094000002", "A2");
        cinema.venderEntrada("F001", "1094000004", "A3");
        cinema.venderEntrada("F003", "1094000003", "B2");
        cinema.cancelarVenta("V004");
    }
}
