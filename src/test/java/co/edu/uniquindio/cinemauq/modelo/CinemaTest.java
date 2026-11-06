package co.edu.uniquindio.cinemauq.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import co.edu.uniquindio.cinemauq.enumeraciones.Clasificacion;
import co.edu.uniquindio.cinemauq.enumeraciones.EstadoPelicula;
import co.edu.uniquindio.cinemauq.enumeraciones.EstadoSala;
import co.edu.uniquindio.cinemauq.enumeraciones.Genero;
import co.edu.uniquindio.cinemauq.enumeraciones.Rol;
import co.edu.uniquindio.cinemauq.enumeraciones.TipoCliente;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.excepciones.ClienteDuplicadoException;
import co.edu.uniquindio.cinemauq.excepciones.ClienteNoEncontradoException;
import co.edu.uniquindio.cinemauq.excepciones.CredencialesInvalidasException;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.PeliculaDuplicadaException;
import co.edu.uniquindio.cinemauq.excepciones.PeliculaNoEncontradaException;
import co.edu.uniquindio.cinemauq.excepciones.SalaDuplicadaException;
import co.edu.uniquindio.cinemauq.excepciones.SalaNoEncontradaException;
import co.edu.uniquindio.cinemauq.enumeraciones.EstadoFuncion;
import co.edu.uniquindio.cinemauq.enumeraciones.EstadoVenta;
import co.edu.uniquindio.cinemauq.excepciones.AsientoNoEncontradoException;
import co.edu.uniquindio.cinemauq.excepciones.AsientoOcupadoException;
import co.edu.uniquindio.cinemauq.excepciones.CruceHorarioException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionLlenaException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionNoDisponibleException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionNoEncontradaException;
import co.edu.uniquindio.cinemauq.excepciones.PeliculaNoDisponibleException;
import co.edu.uniquindio.cinemauq.excepciones.SalaNoDisponibleException;
import co.edu.uniquindio.cinemauq.excepciones.TieneFuncionesProgramadasException;
import co.edu.uniquindio.cinemauq.excepciones.VentaNoEncontradaException;
import co.edu.uniquindio.cinemauq.excepciones.VentaYaCanceladaException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CinemaTest {

    private Cinema cinema;

    @BeforeEach
    void preparar() throws DatoInvalidoException {
        cinema = new Cinema("CinemaUQ", "Armenia");
    }

    private Pelicula pelicula(String codigo) throws DatoInvalidoException {
        return new Pelicula(codigo, "Película " + codigo, Genero.DRAMA, 100, Clasificacion.TODOS);
    }

    private Sala sala(String codigo) throws DatoInvalidoException {
        return new Sala(codigo, "Sala " + codigo, 2, 3);
    }

    // ------------------------------------------------------------ Constructor

    @Test
    void constructor_creaLasCuatroTarifasPorDefecto() {
        assertEquals(0, cinema.getTarifa(TipoCliente.GENERAL).getPorcentajeDescuento(), 0.001);
        assertEquals(20, cinema.getTarifa(TipoCliente.ESTUDIANTE).getPorcentajeDescuento(), 0.001);
        assertEquals(30, cinema.getTarifa(TipoCliente.NINO).getPorcentajeDescuento(), 0.001);
        assertEquals(25, cinema.getTarifa(TipoCliente.ADULTO_MAYOR).getPorcentajeDescuento(), 0.001);
        assertEquals("CinemaUQ", cinema.getNombre());
        assertEquals("Armenia", cinema.getDireccion());
    }

    @Test
    void getters_listasNoModificables() throws CinemaException {
        assertThrows(UnsupportedOperationException.class, () -> cinema.getUsuarios().clear());
        assertThrows(UnsupportedOperationException.class, () -> cinema.getPeliculas().clear());
        assertThrows(UnsupportedOperationException.class, () -> cinema.getSalas().clear());
        assertThrows(UnsupportedOperationException.class, () -> cinema.getClientes().clear());
        assertThrows(UnsupportedOperationException.class, () -> cinema.getFunciones().clear());
        assertThrows(UnsupportedOperationException.class, () -> cinema.getVentas().clear());
    }

    // --------------------------------------------------------------- Usuarios

    @Test
    void registrarUsuario_valido_agregaUsuario() throws CinemaException {
        cinema.registrarUsuario(new Usuario("1", "Admin", "admin", "clave", Rol.ADMINISTRADOR));
        assertEquals(1, cinema.getUsuarios().size());
    }

    @Test
    void registrarUsuario_nulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> cinema.registrarUsuario(null));
    }

    @Test
    void registrarUsuario_nombreUsuarioRepetido_lanzaDatoInvalidoException() throws CinemaException {
        cinema.registrarUsuario(new Usuario("1", "Admin", "admin", "clave", Rol.ADMINISTRADOR));
        Usuario repetido = new Usuario("2", "Otro", "admin", "otra", Rol.OPERADOR);
        assertThrows(DatoInvalidoException.class, () -> cinema.registrarUsuario(repetido));
        assertEquals(1, cinema.getUsuarios().size());
    }

    @Test
    void iniciarSesion_credencialesCorrectas_devuelveElUsuario() throws CinemaException {
        Usuario u = new Usuario("1", "Admin", "admin", "clave", Rol.ADMINISTRADOR);
        cinema.registrarUsuario(u);
        assertSame(u, cinema.iniciarSesion("admin", "clave"));
    }

    @Test
    void iniciarSesion_contrasenaIncorrecta_lanzaCredencialesInvalidasException()
            throws CinemaException {
        cinema.registrarUsuario(new Usuario("1", "Admin", "admin", "clave", Rol.ADMINISTRADOR));
        assertThrows(CredencialesInvalidasException.class,
                () -> cinema.iniciarSesion("admin", "mala"));
    }

    @Test
    void iniciarSesion_usuarioInexistente_lanzaCredencialesInvalidasException() {
        assertThrows(CredencialesInvalidasException.class,
                () -> cinema.iniciarSesion("nadie", "clave"));
    }

    @Test
    void iniciarSesion_mensajeNoRevelaCualDatoFallo() throws CinemaException {
        cinema.registrarUsuario(new Usuario("1", "Admin", "admin", "clave", Rol.ADMINISTRADOR));
        CredencialesInvalidasException a = assertThrows(CredencialesInvalidasException.class,
                () -> cinema.iniciarSesion("admin", "mala"));
        CredencialesInvalidasException b = assertThrows(CredencialesInvalidasException.class,
                () -> cinema.iniciarSesion("otro", "clave"));
        assertEquals(a.getMessage(), b.getMessage());
    }

    // -------------------------------------------------------------- Películas

    @Test
    void registrarPelicula_valida_agregaPelicula() throws CinemaException {
        cinema.registrarPelicula(pelicula("P1"));
        assertEquals(1, cinema.getPeliculas().size());
    }

    @Test
    void registrarPelicula_nula_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> cinema.registrarPelicula(null));
    }

    @Test
    void registrarPelicula_codigoRepetido_lanzaPeliculaDuplicadaException() throws CinemaException {
        cinema.registrarPelicula(pelicula("P1"));
        Pelicula repetida = pelicula("p1");
        assertThrows(PeliculaDuplicadaException.class, () -> cinema.registrarPelicula(repetida));
    }

    @Test
    void buscarPelicula_existente_devuelveLaPelicula() throws CinemaException {
        Pelicula p = pelicula("P1");
        cinema.registrarPelicula(p);
        assertSame(p, cinema.buscarPelicula(" p1 "));
    }

    @Test
    void buscarPelicula_inexistente_lanzaPeliculaNoEncontradaException() {
        assertThrows(PeliculaNoEncontradaException.class, () -> cinema.buscarPelicula("P9"));
    }

    @Test
    void modificarPelicula_datosValidos_actualizaLaPelicula() throws CinemaException {
        cinema.registrarPelicula(pelicula("P1"));
        cinema.modificarPelicula("P1", "Nuevo", Genero.TERROR, 150, Clasificacion.MAYORES_18);
        Pelicula p = cinema.buscarPelicula("P1");
        assertEquals("Nuevo", p.getNombre());
        assertEquals(Genero.TERROR, p.getGenero());
        assertEquals(150, p.getDuracionMinutos());
        assertEquals(Clasificacion.MAYORES_18, p.getClasificacion());
    }

    @Test
    void modificarPelicula_inexistente_lanzaPeliculaNoEncontradaException() {
        assertThrows(PeliculaNoEncontradaException.class, () -> cinema.modificarPelicula(
                "P9", "X", Genero.DRAMA, 100, Clasificacion.TODOS));
    }

    @Test
    void modificarPelicula_datoInvalido_lanzaDatoInvalidoExceptionYNoModificaNada()
            throws CinemaException {
        cinema.registrarPelicula(pelicula("P1"));
        assertThrows(DatoInvalidoException.class, () -> cinema.modificarPelicula(
                "P1", "Nuevo", Genero.TERROR, 0, Clasificacion.MAYORES_18));
        Pelicula p = cinema.buscarPelicula("P1");
        assertEquals("Película P1", p.getNombre());
        assertEquals(Genero.DRAMA, p.getGenero());
        assertEquals(100, p.getDuracionMinutos());
    }

    @Test
    void deshabilitarPelicula_existente_quedaNoDisponible() throws CinemaException {
        cinema.registrarPelicula(pelicula("P1"));
        cinema.deshabilitarPelicula("P1");
        assertEquals(EstadoPelicula.NO_DISPONIBLE, cinema.buscarPelicula("P1").getEstado());
    }

    @Test
    void deshabilitarPelicula_inexistente_lanzaPeliculaNoEncontradaException() {
        assertThrows(PeliculaNoEncontradaException.class, () -> cinema.deshabilitarPelicula("P9"));
    }

    @Test
    void listarPeliculasDisponibles_excluyeLasDeshabilitadas() throws CinemaException {
        cinema.registrarPelicula(pelicula("P1"));
        cinema.registrarPelicula(pelicula("P2"));
        cinema.deshabilitarPelicula("P2");
        List<Pelicula> disponibles = cinema.listarPeliculasDisponibles();
        assertEquals(1, disponibles.size());
        assertEquals("P1", disponibles.get(0).getCodigo());
    }

    // ------------------------------------------------------------------ Salas

    @Test
    void registrarSala_valida_agregaSala() throws CinemaException {
        cinema.registrarSala(sala("S1"));
        assertEquals(1, cinema.getSalas().size());
    }

    @Test
    void registrarSala_nula_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> cinema.registrarSala(null));
    }

    @Test
    void registrarSala_codigoRepetido_lanzaSalaDuplicadaException() throws CinemaException {
        cinema.registrarSala(sala("S1"));
        Sala repetida = sala("s1");
        assertThrows(SalaDuplicadaException.class, () -> cinema.registrarSala(repetida));
    }

    @Test
    void buscarSala_existente_devuelveLaSala() throws CinemaException {
        Sala s = sala("S1");
        cinema.registrarSala(s);
        assertSame(s, cinema.buscarSala("s1"));
    }

    @Test
    void buscarSala_inexistente_lanzaSalaNoEncontradaException() {
        assertThrows(SalaNoEncontradaException.class, () -> cinema.buscarSala("S9"));
    }

    @Test
    void modificarSala_datosValidos_actualizaNombreYDistribucion() throws CinemaException {
        cinema.registrarSala(sala("S1"));
        cinema.modificarSala("S1", "VIP", 4, 5);
        Sala s = cinema.buscarSala("S1");
        assertEquals("VIP", s.getNombre());
        assertEquals(20, s.getCapacidad());
    }

    @Test
    void modificarSala_inexistente_lanzaSalaNoEncontradaException() {
        assertThrows(SalaNoEncontradaException.class, () -> cinema.modificarSala("S9", "X", 2, 2));
    }

    @Test
    void modificarSala_datoInvalido_lanzaDatoInvalidoExceptionYNoModificaNada()
            throws CinemaException {
        cinema.registrarSala(sala("S1"));
        assertThrows(DatoInvalidoException.class, () -> cinema.modificarSala("S1", "VIP", 30, 5));
        Sala s = cinema.buscarSala("S1");
        assertEquals("Sala S1", s.getNombre());
        assertEquals(6, s.getCapacidad());
    }

    @Test
    void deshabilitarYHabilitarSala_cambianElEstado() throws CinemaException {
        cinema.registrarSala(sala("S1"));
        cinema.deshabilitarSala("S1");
        assertEquals(EstadoSala.FUERA_DE_SERVICIO, cinema.buscarSala("S1").getEstado());
        cinema.habilitarSala("S1");
        assertTrue(cinema.buscarSala("S1").estaDisponible());
    }

    @Test
    void deshabilitarSala_inexistente_lanzaSalaNoEncontradaException() {
        assertThrows(SalaNoEncontradaException.class, () -> cinema.deshabilitarSala("S9"));
    }

    @Test
    void habilitarSala_inexistente_lanzaSalaNoEncontradaException() {
        assertThrows(SalaNoEncontradaException.class, () -> cinema.habilitarSala("S9"));
    }

    // --------------------------------------------------------------- Clientes

    @Test
    void registrarCliente_valido_agregaCliente() throws CinemaException {
        cinema.registrarCliente(new Cliente("100", "Ana", TipoCliente.ESTUDIANTE));
        assertEquals(1, cinema.getClientes().size());
    }

    @Test
    void registrarCliente_nulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> cinema.registrarCliente(null));
    }

    @Test
    void registrarCliente_identificacionRepetida_lanzaClienteDuplicadoException()
            throws CinemaException {
        cinema.registrarCliente(new Cliente("100", "Ana", TipoCliente.ESTUDIANTE));
        Cliente repetido = new Cliente("100", "Otra", TipoCliente.GENERAL);
        assertThrows(ClienteDuplicadoException.class, () -> cinema.registrarCliente(repetido));
    }

    @Test
    void buscarCliente_existente_devuelveElCliente() throws CinemaException {
        Cliente c = new Cliente("100", "Ana", TipoCliente.ESTUDIANTE);
        cinema.registrarCliente(c);
        assertSame(c, cinema.buscarCliente(" 100 "));
    }

    @Test
    void buscarCliente_inexistente_lanzaClienteNoEncontradoException() {
        assertThrows(ClienteNoEncontradoException.class, () -> cinema.buscarCliente("999"));
    }

    // ---------------------------------------------------------------- Tarifas

    @Test
    void modificarTarifa_valida_actualizaElPorcentaje() throws CinemaException {
        cinema.modificarTarifa(TipoCliente.GENERAL, 10);
        assertEquals(10, cinema.getTarifa(TipoCliente.GENERAL).getPorcentajeDescuento(), 0.001);
    }

    @Test
    void modificarTarifa_fueraDeRango_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class,
                () -> cinema.modificarTarifa(TipoCliente.GENERAL, 150));
        assertEquals(0, cinema.getTarifa(TipoCliente.GENERAL).getPorcentajeDescuento(), 0.001);
    }

    @Test
    void modificarTarifa_tipoNulo_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> cinema.modificarTarifa(null, 10));
    }

    @Test
    void listarPeliculasDisponibles_sinPeliculas_devuelveListaVacia() {
        assertTrue(cinema.listarPeliculasDisponibles().isEmpty());
    }

    // =================================================== Fase 5: funciones y ventas

    private final LocalDate manana = LocalDate.now().plusDays(1);

    /** Registra la película P1 (100 min), la sala S1 (2 x 3) y el cliente 100 (estudiante). */
    private void escenario() throws CinemaException {
        cinema.registrarPelicula(pelicula("P1"));
        cinema.registrarSala(sala("S1"));
        cinema.registrarCliente(new Cliente("100", "Ana", TipoCliente.ESTUDIANTE));
    }

    private Funcion programar(LocalTime hora) throws CinemaException {
        return cinema.programarFuncion("P1", "S1", manana, hora, 15000);
    }

    // ------------------------------------------------------------ programarFuncion

    @Test
    void programarFuncion_datosValidos_creaFuncionConCodigoConsecutivo() throws CinemaException {
        escenario();
        Funcion f1 = programar(LocalTime.of(14, 0));
        Funcion f2 = programar(LocalTime.of(18, 0));
        assertEquals("F001", f1.getCodigo());
        assertEquals("F002", f2.getCodigo());
        assertTrue(f1.estaProgramada());
        assertEquals(2, cinema.getFunciones().size());
        assertEquals(6, f1.getAsientos().size());
    }

    @Test
    void programarFuncion_peliculaInexistente_lanzaPeliculaNoEncontradaException()
            throws CinemaException {
        escenario();
        assertThrows(PeliculaNoEncontradaException.class, () -> cinema.programarFuncion(
                "P9", "S1", manana, LocalTime.NOON, 15000));
    }

    @Test
    void programarFuncion_peliculaNoDisponible_lanzaPeliculaNoDisponibleException()
            throws CinemaException {
        escenario();
        cinema.deshabilitarPelicula("P1");
        assertThrows(PeliculaNoDisponibleException.class, () -> programar(LocalTime.NOON));
    }

    @Test
    void programarFuncion_salaInexistente_lanzaSalaNoEncontradaException() throws CinemaException {
        escenario();
        assertThrows(SalaNoEncontradaException.class, () -> cinema.programarFuncion(
                "P1", "S9", manana, LocalTime.NOON, 15000));
    }

    @Test
    void programarFuncion_salaFueraDeServicio_lanzaSalaNoDisponibleException()
            throws CinemaException {
        escenario();
        cinema.deshabilitarSala("S1");
        assertThrows(SalaNoDisponibleException.class, () -> programar(LocalTime.NOON));
    }

    @Test
    void programarFuncion_fechaNula_lanzaDatoInvalidoException() throws CinemaException {
        escenario();
        assertThrows(DatoInvalidoException.class, () -> cinema.programarFuncion(
                "P1", "S1", null, LocalTime.NOON, 15000));
    }

    @Test
    void programarFuncion_horaNula_lanzaDatoInvalidoException() throws CinemaException {
        escenario();
        assertThrows(DatoInvalidoException.class, () -> cinema.programarFuncion(
                "P1", "S1", manana, null, 15000));
    }

    @Test
    void programarFuncion_fechaYHoraPasadas_lanzaDatoInvalidoException() throws CinemaException {
        escenario();
        LocalDateTime pasado = LocalDateTime.now().minusMinutes(5);
        assertThrows(DatoInvalidoException.class, () -> cinema.programarFuncion(
                "P1", "S1", pasado.toLocalDate(), pasado.toLocalTime(), 15000));
        assertThrows(DatoInvalidoException.class, () -> cinema.programarFuncion(
                "P1", "S1", LocalDate.now().minusDays(1), LocalTime.NOON, 15000));
    }

    @Test
    void programarFuncion_valorBaseCero_lanzaDatoInvalidoException() throws CinemaException {
        escenario();
        assertThrows(DatoInvalidoException.class, () -> cinema.programarFuncion(
                "P1", "S1", manana, LocalTime.NOON, 0));
    }

    @Test
    void programarFuncion_cruceDeHorario_lanzaCruceHorarioExceptionConDatosDeLaExistente()
            throws CinemaException {
        escenario();
        programar(LocalTime.of(14, 0));
        CruceHorarioException ex = assertThrows(CruceHorarioException.class,
                () -> programar(LocalTime.of(15, 0)));
        assertTrue(ex.getMessage().contains("F001"));
        assertTrue(ex.getMessage().contains("14:00"));
        assertTrue(ex.getMessage().contains("15:40"));
        assertEquals(1, cinema.getFunciones().size());
    }

    @Test
    void programarFuncion_empiezaCuandoTerminaLaOtra_noEsCruce() throws CinemaException {
        escenario();
        programar(LocalTime.of(14, 0));
        assertEquals("F002", programar(LocalTime.of(15, 40)).getCodigo());
    }

    @Test
    void programarFuncion_empiezaUnMinutoAntesDeQueTermineLaOtra_lanzaCruceHorarioException()
            throws CinemaException {
        escenario();
        programar(LocalTime.of(14, 0));
        assertThrows(CruceHorarioException.class, () -> programar(LocalTime.of(15, 39)));
    }

    @Test
    void programarFuncion_mismoHorarioEnOtraSala_noEsCruce() throws CinemaException {
        escenario();
        cinema.registrarSala(sala("S2"));
        programar(LocalTime.of(14, 0));
        Funcion otra = cinema.programarFuncion("P1", "S2", manana, LocalTime.of(14, 0), 15000);
        assertEquals("F002", otra.getCodigo());
    }

    @Test
    void programarFuncion_horarioDeFuncionCancelada_sePuedeReutilizar() throws CinemaException {
        escenario();
        programar(LocalTime.of(14, 0));
        cinema.cancelarFuncion("F001");
        assertEquals("F002", programar(LocalTime.of(14, 0)).getCodigo());
    }

    @Test
    void programarFuncion_consecutivoNoAvanzaCuandoFalla() throws CinemaException {
        escenario();
        // Falla por película no disponible antes de programar nada.
        cinema.registrarPelicula(pelicula("P2"));
        cinema.deshabilitarPelicula("P2");
        assertThrows(PeliculaNoDisponibleException.class, () -> cinema.programarFuncion(
                "P2", "S1", manana, LocalTime.of(10, 0), 15000));
        assertEquals("F001", programar(LocalTime.of(14, 0)).getCodigo());
        // Falla por cruce, por fecha pasada y por valor inválido.
        assertThrows(CruceHorarioException.class, () -> programar(LocalTime.of(14, 30)));
        assertThrows(DatoInvalidoException.class, () -> cinema.programarFuncion(
                "P1", "S1", LocalDate.now().minusDays(1), LocalTime.NOON, 15000));
        assertThrows(DatoInvalidoException.class, () -> cinema.programarFuncion(
                "P1", "S1", manana, LocalTime.of(20, 0), -5));
        assertEquals("F002", programar(LocalTime.of(18, 0)).getCodigo());
        assertEquals(2, cinema.getFunciones().size());
    }

    // ------------------------------------------------- buscar / finalizar / cancelar

    @Test
    void buscarFuncion_existente_devuelveLaFuncion() throws CinemaException {
        escenario();
        Funcion f = programar(LocalTime.NOON);
        assertSame(f, cinema.buscarFuncion(" f001 "));
    }

    @Test
    void buscarFuncion_inexistente_lanzaFuncionNoEncontradaException() {
        assertThrows(FuncionNoEncontradaException.class, () -> cinema.buscarFuncion("F999"));
    }

    @Test
    void finalizarFuncion_programada_quedaFinalizadaYSusVentasSiguenActivas()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        Venta v = cinema.venderEntrada("F001", "100", "A1");
        cinema.finalizarFuncion("F001");
        assertEquals(EstadoFuncion.FINALIZADA, cinema.buscarFuncion("F001").getEstado());
        assertTrue(v.estaActiva());
    }

    @Test
    void finalizarFuncion_inexistente_lanzaFuncionNoEncontradaException() {
        assertThrows(FuncionNoEncontradaException.class, () -> cinema.finalizarFuncion("F1"));
    }

    @Test
    void finalizarFuncion_yaFinalizada_lanzaFuncionNoDisponibleException() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.finalizarFuncion("F001");
        assertThrows(FuncionNoDisponibleException.class, () -> cinema.finalizarFuncion("F001"));
    }

    @Test
    void cancelarFuncion_programada_cancelaSusVentasActivas() throws CinemaException {
        escenario();
        cinema.registrarCliente(new Cliente("200", "Luis", TipoCliente.GENERAL));
        programar(LocalTime.of(14, 0));
        programar(LocalTime.of(18, 0));
        Venta v1 = cinema.venderEntrada("F001", "100", "A1");
        Venta v2 = cinema.venderEntrada("F001", "200", "A2");
        Venta otraFuncion = cinema.venderEntrada("F002", "100", "A1");

        cinema.cancelarFuncion("F001");

        assertEquals(EstadoFuncion.CANCELADA, cinema.buscarFuncion("F001").getEstado());
        assertEquals(EstadoVenta.CANCELADA, v1.getEstado());
        assertEquals(EstadoVenta.CANCELADA, v2.getEstado());
        assertTrue(otraFuncion.estaActiva());
        assertEquals(0, cinema.buscarFuncion("F001").contarOcupados());
        assertEquals(1, cinema.buscarFuncion("F002").contarOcupados());
    }

    @Test
    void cancelarFuncion_inexistente_lanzaFuncionNoEncontradaException() {
        assertThrows(FuncionNoEncontradaException.class, () -> cinema.cancelarFuncion("F1"));
    }

    @Test
    void cancelarFuncion_noProgramada_lanzaFuncionNoDisponibleException() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.finalizarFuncion("F001");
        assertThrows(FuncionNoDisponibleException.class, () -> cinema.cancelarFuncion("F001"));
    }

    @Test
    void listarFuncionesProgramadas_excluyeFinalizadasYCanceladas() throws CinemaException {
        escenario();
        programar(LocalTime.of(8, 0));
        programar(LocalTime.of(12, 0));
        programar(LocalTime.of(16, 0));
        cinema.finalizarFuncion("F001");
        cinema.cancelarFuncion("F002");
        List<Funcion> programadas = cinema.listarFuncionesProgramadas();
        assertEquals(1, programadas.size());
        assertEquals("F003", programadas.get(0).getCodigo());
    }

    // --------------------------------------------------------------- venderEntrada

    @Test
    void venderEntrada_datosValidos_creaVentaConValorConDescuentoYOcupaElAsiento()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        LocalDateTime antes = LocalDateTime.now();
        Venta v = cinema.venderEntrada("f001", "100", "b2");
        assertEquals("V001", v.getCodigo());
        assertEquals(12000, v.getValorPagado(), 0.001);
        assertEquals("B2", v.getAsiento().getCodigo());
        assertEquals(EstadoVenta.ACTIVA, v.getEstado());
        assertFalse(v.getFechaHora().isBefore(antes));
        assertFalse(cinema.buscarFuncion("F001").buscarAsiento("B2").estaDisponible());
        assertEquals(1, cinema.getVentas().size());
    }

    @Test
    void venderEntrada_usaElAsientoDeLaFuncion_elDeLaSalaSigueDisponible() throws CinemaException {
        escenario();
        programar(LocalTime.of(14, 0));
        programar(LocalTime.of(18, 0));
        Venta v = cinema.venderEntrada("F001", "100", "A1");
        Funcion f1 = cinema.buscarFuncion("F001");
        Funcion f2 = cinema.buscarFuncion("F002");
        assertSame(f1.buscarAsiento("A1"), v.getAsiento());
        assertFalse(f1.buscarAsiento("A1").estaDisponible());
        assertTrue(cinema.buscarSala("S1").getAsientos().get(0).estaDisponible());
        assertTrue(f2.buscarAsiento("A1").estaDisponible());
    }

    @Test
    void venderEntrada_funcionInexistente_lanzaFuncionNoEncontradaException()
            throws CinemaException {
        escenario();
        assertThrows(FuncionNoEncontradaException.class,
                () -> cinema.venderEntrada("F999", "100", "A1"));
    }

    @Test
    void venderEntrada_funcionNoProgramada_lanzaFuncionNoDisponibleException()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.finalizarFuncion("F001");
        assertThrows(FuncionNoDisponibleException.class,
                () -> cinema.venderEntrada("F001", "100", "A1"));
    }

    @Test
    void venderEntrada_clienteInexistente_lanzaClienteNoEncontradoException()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        assertThrows(ClienteNoEncontradoException.class,
                () -> cinema.venderEntrada("F001", "999", "A1"));
    }

    @Test
    void venderEntrada_funcionNoProgramadaYClienteInexistente_fallaPrimeroPorLaFuncion()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.cancelarFuncion("F001");
        assertThrows(FuncionNoDisponibleException.class,
                () -> cinema.venderEntrada("F001", "999", "A1"));
    }

    @Test
    void venderEntrada_funcionLlena_lanzaFuncionLlenaException() throws CinemaException {
        cinema.registrarPelicula(pelicula("P1"));
        cinema.registrarSala(new Sala("S1", "Sala 1", 1, 1));
        cinema.registrarCliente(new Cliente("100", "Ana", TipoCliente.GENERAL));
        cinema.registrarCliente(new Cliente("200", "Luis", TipoCliente.GENERAL));
        programar(LocalTime.NOON);
        cinema.venderEntrada("F001", "100", "A1");
        assertThrows(FuncionLlenaException.class, () -> cinema.venderEntrada("F001", "200", "A1"));
    }

    @Test
    void venderEntrada_asientoInexistente_lanzaAsientoNoEncontradoException()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        assertThrows(AsientoNoEncontradoException.class,
                () -> cinema.venderEntrada("F001", "100", "Z9"));
    }

    @Test
    void venderEntrada_asientoOcupado_lanzaAsientoOcupadoExceptionConMensajeClaro()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.venderEntrada("F001", "100", "B3");
        AsientoOcupadoException ex = assertThrows(AsientoOcupadoException.class,
                () -> cinema.venderEntrada("F001", "100", "B3"));
        assertEquals("El asiento B3 ya está ocupado en la función F001", ex.getMessage());
    }

    @Test
    void venderEntrada_consecutivoNoAvanzaCuandoFalla() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.venderEntrada("F001", "100", "A1");
        assertThrows(AsientoOcupadoException.class, () -> cinema.venderEntrada("F001", "100", "A1"));
        assertEquals("V002", cinema.venderEntrada("F001", "100", "A2").getCodigo());
    }

    @Test
    void venderEntrada_tarifaModificadaDespues_noCambiaElValorPagado() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        Venta v = cinema.venderEntrada("F001", "100", "A1");
        cinema.modificarTarifa(TipoCliente.ESTUDIANTE, 50);
        assertEquals(12000, v.getValorPagado(), 0.001);
        Venta nueva = cinema.venderEntrada("F001", "100", "A2");
        assertEquals(7500, nueva.getValorPagado(), 0.001);
        assertEquals(12000, cinema.buscarVenta("V001").getValorPagado(), 0.001);
    }

    // --------------------------------------------------------------- cancelarVenta

    @Test
    void cancelarVenta_ventaActiva_liberaElAsientoYSePuedeVolverAVender() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        Venta v = cinema.venderEntrada("F001", "100", "A1");
        cinema.cancelarVenta("v001");
        assertEquals(EstadoVenta.CANCELADA, v.getEstado());
        assertTrue(cinema.buscarFuncion("F001").buscarAsiento("A1").estaDisponible());
        assertEquals("V002", cinema.venderEntrada("F001", "100", "A1").getCodigo());
    }

    @Test
    void cancelarVenta_inexistente_lanzaVentaNoEncontradaException() {
        assertThrows(VentaNoEncontradaException.class, () -> cinema.cancelarVenta("V999"));
    }

    @Test
    void cancelarVenta_yaCancelada_lanzaVentaYaCanceladaException() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.venderEntrada("F001", "100", "A1");
        cinema.cancelarVenta("V001");
        assertThrows(VentaYaCanceladaException.class, () -> cinema.cancelarVenta("V001"));
    }

    @Test
    void cancelarVenta_funcionFinalizada_lanzaFuncionNoDisponibleException()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        Venta v = cinema.venderEntrada("F001", "100", "A1");
        cinema.finalizarFuncion("F001");
        assertThrows(FuncionNoDisponibleException.class, () -> cinema.cancelarVenta("V001"));
        assertTrue(v.estaActiva());
    }

    @Test
    void cancelarVenta_ventaCanceladaEnFuncionCancelada_fallaPorVentaYaCancelada()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.venderEntrada("F001", "100", "A1");
        cinema.cancelarFuncion("F001");
        assertThrows(VentaYaCanceladaException.class, () -> cinema.cancelarVenta("V001"));
    }

    @Test
    void buscarVenta_existente_devuelveLaVenta() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        Venta v = cinema.venderEntrada("F001", "100", "A1");
        assertSame(v, cinema.buscarVenta(" v001 "));
    }

    @Test
    void buscarVenta_inexistente_lanzaVentaNoEncontradaException() {
        assertThrows(VentaNoEncontradaException.class, () -> cinema.buscarVenta("V999"));
    }

    // -------------------------------------------------------------------- Reportes

    /** Agrega la película P2, el cliente 200 (general) y una función de P2 a las 14:00 en S1. */
    private void escenarioConSegundaPelicula() throws CinemaException {
        escenario();
        cinema.registrarPelicula(pelicula("P2"));
        cinema.registrarCliente(new Cliente("200", "Luis", TipoCliente.GENERAL));
        programar(LocalTime.of(10, 0));                                              // F001 (P1)
        cinema.programarFuncion("P2", "S1", manana, LocalTime.of(14, 0), 10000);     // F002 (P2)
    }

    @Test
    void reportes_sinVentas_devuelvenCeroYListaVacia() throws CinemaException {
        escenario();
        assertEquals(0, cinema.totalEntradasVendidas());
        assertEquals(0, cinema.totalIngresos(), 0.001);
        assertTrue(cinema.peliculasMasVendidas().isEmpty());
    }

    @Test
    void totalEntradasVendidasYIngresos_sumanSoloVentasActivas() throws CinemaException {
        escenarioConSegundaPelicula();
        cinema.venderEntrada("F001", "100", "A1");   // 12.000
        cinema.venderEntrada("F001", "200", "A2");   // 15.000
        cinema.venderEntrada("F002", "200", "A1");   // 10.000
        assertEquals(3, cinema.totalEntradasVendidas());
        assertEquals(37000, cinema.totalIngresos(), 0.001);
    }

    @Test
    void reportes_ignoranLasVentasCanceladas() throws CinemaException {
        escenarioConSegundaPelicula();
        cinema.venderEntrada("F001", "100", "A1");   // 12.000
        cinema.venderEntrada("F001", "200", "A2");   // 15.000
        cinema.venderEntrada("F002", "200", "A1");   // 10.000
        cinema.cancelarVenta("V002");
        assertEquals(2, cinema.totalEntradasVendidas());
        assertEquals(22000, cinema.totalIngresos(), 0.001);
        assertEquals(1, cinema.getFunciones().get(0).contarOcupados());
    }

    @Test
    void peliculasMasVendidas_unaGanadora_devuelveSoloEsa() throws CinemaException {
        escenarioConSegundaPelicula();
        cinema.venderEntrada("F001", "100", "A1");
        cinema.venderEntrada("F001", "200", "A2");
        cinema.venderEntrada("F002", "200", "A1");
        List<Pelicula> masVendidas = cinema.peliculasMasVendidas();
        assertEquals(1, masVendidas.size());
        assertEquals("P1", masVendidas.get(0).getCodigo());
    }

    @Test
    void peliculasMasVendidas_conEmpate_devuelveTodasLasEmpatadas() throws CinemaException {
        escenarioConSegundaPelicula();
        cinema.venderEntrada("F001", "100", "A1");
        cinema.venderEntrada("F002", "200", "A1");
        List<Pelicula> masVendidas = cinema.peliculasMasVendidas();
        assertEquals(2, masVendidas.size());
        assertEquals("P1", masVendidas.get(0).getCodigo());
        assertEquals("P2", masVendidas.get(1).getCodigo());
    }

    @Test
    void peliculasMasVendidas_ignoraVentasCanceladas() throws CinemaException {
        escenarioConSegundaPelicula();
        cinema.venderEntrada("F001", "100", "A1");
        cinema.venderEntrada("F001", "200", "A2");
        cinema.venderEntrada("F002", "200", "A1");
        cinema.cancelarVenta("V001");
        cinema.cancelarVenta("V002");
        List<Pelicula> masVendidas = cinema.peliculasMasVendidas();
        assertEquals(1, masVendidas.size());
        assertEquals("P2", masVendidas.get(0).getCodigo());
    }

    @Test
    void peliculasMasVendidas_todasLasVentasCanceladas_devuelveListaVacia() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.venderEntrada("F001", "100", "A1");
        cinema.cancelarVenta("V001");
        assertTrue(cinema.peliculasMasVendidas().isEmpty());
    }

    @Test
    void ocupacionFuncion_conAsientosOcupados_devuelveElPorcentaje() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.venderEntrada("F001", "100", "A1");
        cinema.venderEntrada("F001", "100", "A2");
        cinema.venderEntrada("F001", "100", "A3");
        assertEquals(50.0, cinema.ocupacionFuncion("F001"), 0.001);
        cinema.cancelarVenta("V003");
        assertEquals(100.0 / 3, cinema.ocupacionFuncion("F001"), 0.001);
    }

    @Test
    void ocupacionFuncion_inexistente_lanzaFuncionNoEncontradaException() {
        assertThrows(FuncionNoEncontradaException.class, () -> cinema.ocupacionFuncion("F999"));
    }

    @Test
    void funcionesPorFecha_excluyeNoProgramadasYOrdenaPorHora() throws CinemaException {
        escenario();
        programar(LocalTime.of(18, 0));                       // F001
        programar(LocalTime.of(8, 0));                        // F002
        programar(LocalTime.of(12, 0));                       // F003 (se finaliza)
        programar(LocalTime.of(15, 0));                       // F004 (se cancela)
        programar(LocalTime.of(10, 0));                       // F005
        cinema.programarFuncion("P1", "S1", manana.plusDays(1), LocalTime.of(9, 0), 15000); // F006
        cinema.finalizarFuncion("F003");
        cinema.cancelarFuncion("F004");

        List<Funcion> delDia = cinema.funcionesPorFecha(manana);

        assertEquals(3, delDia.size());
        assertEquals("F002", delDia.get(0).getCodigo());
        assertEquals("F005", delDia.get(1).getCodigo());
        assertEquals("F001", delDia.get(2).getCodigo());
        assertEquals(1, cinema.funcionesPorFecha(manana.plusDays(1)).size());
    }

    @Test
    void funcionesPorFecha_fechaSinFunciones_devuelveListaVacia() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        assertTrue(cinema.funcionesPorFecha(manana.plusDays(5)).isEmpty());
    }

    @Test
    void funcionesPorFecha_fechaNula_lanzaDatoInvalidoException() {
        assertThrows(DatoInvalidoException.class, () -> cinema.funcionesPorFecha(null));
    }

    // ------------------------- restricciones con funciones programadas (películas/salas)

    @Test
    void deshabilitarPelicula_conFuncionProgramada_lanzaTieneFuncionesProgramadasException()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        assertThrows(TieneFuncionesProgramadasException.class,
                () -> cinema.deshabilitarPelicula("P1"));
        assertTrue(cinema.buscarPelicula("P1").estaDisponible());
    }

    @Test
    void modificarPelicula_cambiarDuracionConFuncionProgramada_lanzaTieneFuncionesProgramadasException()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        assertThrows(TieneFuncionesProgramadasException.class, () -> cinema.modificarPelicula(
                "P1", "Nuevo", Genero.DRAMA, 130, Clasificacion.TODOS));
        assertEquals(100, cinema.buscarPelicula("P1").getDuracionMinutos());
    }

    @Test
    void modificarPelicula_sinCambiarDuracionConFuncionProgramada_sePermite()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.modificarPelicula("P1", "Nuevo nombre", Genero.ACCION, 100, Clasificacion.MAYORES_12);
        assertEquals("Nuevo nombre", cinema.buscarPelicula("P1").getNombre());
    }

    @Test
    void peliculaConFuncionFinalizada_sePuedeDeshabilitarYCambiarDuracion() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.finalizarFuncion("F001");
        cinema.modificarPelicula("P1", "Nuevo", Genero.DRAMA, 130, Clasificacion.TODOS);
        assertEquals(130, cinema.buscarPelicula("P1").getDuracionMinutos());
        cinema.deshabilitarPelicula("P1");
        assertFalse(cinema.buscarPelicula("P1").estaDisponible());
    }

    @Test
    void peliculaConFuncionCancelada_sePuedeDeshabilitarYCambiarDuracion() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.cancelarFuncion("F001");
        cinema.modificarPelicula("P1", "Nuevo", Genero.DRAMA, 130, Clasificacion.TODOS);
        cinema.deshabilitarPelicula("P1");
        assertFalse(cinema.buscarPelicula("P1").estaDisponible());
    }

    @Test
    void deshabilitarSala_conFuncionProgramada_lanzaTieneFuncionesProgramadasException()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        assertThrows(TieneFuncionesProgramadasException.class, () -> cinema.deshabilitarSala("S1"));
        assertTrue(cinema.buscarSala("S1").estaDisponible());
    }

    @Test
    void modificarSala_cambiarDistribucionConFuncionProgramada_lanzaTieneFuncionesProgramadasException()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        assertThrows(TieneFuncionesProgramadasException.class,
                () -> cinema.modificarSala("S1", "Sala 1", 5, 5));
        assertEquals(6, cinema.buscarSala("S1").getCapacidad());
    }

    @Test
    void modificarSala_soloElNombreConFuncionProgramada_sePermite() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.modificarSala("S1", "Sala Premium", 2, 3);
        assertEquals("Sala Premium", cinema.buscarSala("S1").getNombre());
    }

    @Test
    void salaConFuncionFinalizada_sePuedeCambiarDistribucionYDeshabilitar() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.finalizarFuncion("F001");
        cinema.modificarSala("S1", "Sala 1", 5, 5);
        assertEquals(25, cinema.buscarSala("S1").getCapacidad());
        cinema.deshabilitarSala("S1");
        assertFalse(cinema.buscarSala("S1").estaDisponible());
    }

    @Test
    void salaConFuncionCancelada_sePuedeCambiarDistribucionYDeshabilitar() throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.cancelarFuncion("F001");
        cinema.modificarSala("S1", "Sala 1", 5, 5);
        cinema.deshabilitarSala("S1");
        assertFalse(cinema.buscarSala("S1").estaDisponible());
    }

    @Test
    void modificarSala_distribucionNuevaNoAfectaAsientosDeFuncionesFinalizadas()
            throws CinemaException {
        escenario();
        programar(LocalTime.NOON);
        cinema.finalizarFuncion("F001");
        cinema.modificarSala("S1", "Sala 1", 5, 5);
        assertEquals(6, cinema.buscarFuncion("F001").getAsientos().size());
    }
}
