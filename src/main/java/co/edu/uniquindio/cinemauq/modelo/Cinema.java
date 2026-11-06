package co.edu.uniquindio.cinemauq.modelo;

import co.edu.uniquindio.cinemauq.enumeraciones.Clasificacion;
import co.edu.uniquindio.cinemauq.enumeraciones.Genero;
import co.edu.uniquindio.cinemauq.enumeraciones.TipoCliente;
import co.edu.uniquindio.cinemauq.excepciones.ClienteDuplicadoException;
import co.edu.uniquindio.cinemauq.excepciones.AsientoNoEncontradoException;
import co.edu.uniquindio.cinemauq.excepciones.AsientoOcupadoException;
import co.edu.uniquindio.cinemauq.excepciones.ClienteNoEncontradoException;
import co.edu.uniquindio.cinemauq.excepciones.CruceHorarioException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionLlenaException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionNoDisponibleException;
import co.edu.uniquindio.cinemauq.excepciones.FuncionNoEncontradaException;
import co.edu.uniquindio.cinemauq.excepciones.PeliculaNoDisponibleException;
import co.edu.uniquindio.cinemauq.excepciones.SalaNoDisponibleException;
import co.edu.uniquindio.cinemauq.excepciones.VentaNoEncontradaException;
import co.edu.uniquindio.cinemauq.excepciones.VentaYaCanceladaException;
import co.edu.uniquindio.cinemauq.excepciones.CredencialesInvalidasException;
import co.edu.uniquindio.cinemauq.excepciones.DatoInvalidoException;
import co.edu.uniquindio.cinemauq.excepciones.PeliculaDuplicadaException;
import co.edu.uniquindio.cinemauq.excepciones.PeliculaNoEncontradaException;
import co.edu.uniquindio.cinemauq.excepciones.SalaDuplicadaException;
import co.edu.uniquindio.cinemauq.excepciones.SalaNoEncontradaException;
import co.edu.uniquindio.cinemauq.excepciones.TieneFuncionesProgramadasException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Fachada del sistema: administra usuarios, películas, salas, clientes, funciones,
 * ventas y tarifas del cinema.
 */
public class Cinema {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_HORA = DateTimeFormatter.ofPattern("HH:mm");

    private final String nombre;
    private final String direccion;
    private final List<Usuario> usuarios;
    private final List<Pelicula> peliculas;
    private final List<Sala> salas;
    private final List<Cliente> clientes;
    private final List<Funcion> funciones;
    private final List<Venta> ventas;
    private final Map<TipoCliente, Tarifa> tarifas;
    private int consecutivoFuncion;
    private int consecutivoVenta;

    /**
     * Crea el cinema con las tarifas por defecto:
     * GENERAL 0 %, ESTUDIANTE 20 %, NINO 30 % y ADULTO_MAYOR 25 %.
     *
     * @param nombre nombre del cinema
     * @param direccion dirección del cinema
     * @throws DatoInvalidoException si alguna tarifa por defecto es inválida
     */
    public Cinema(String nombre, String direccion) throws DatoInvalidoException {
        this.nombre = nombre;
        this.direccion = direccion;
        this.usuarios = new ArrayList<>();
        this.peliculas = new ArrayList<>();
        this.salas = new ArrayList<>();
        this.clientes = new ArrayList<>();
        this.funciones = new ArrayList<>();
        this.ventas = new ArrayList<>();
        this.tarifas = new EnumMap<>(TipoCliente.class);
        this.tarifas.put(TipoCliente.GENERAL, new Tarifa(TipoCliente.GENERAL, 0));
        this.tarifas.put(TipoCliente.ESTUDIANTE, new Tarifa(TipoCliente.ESTUDIANTE, 20));
        this.tarifas.put(TipoCliente.NINO, new Tarifa(TipoCliente.NINO, 30));
        this.tarifas.put(TipoCliente.ADULTO_MAYOR, new Tarifa(TipoCliente.ADULTO_MAYOR, 25));
        this.consecutivoFuncion = 1;
        this.consecutivoVenta = 1;
    }

    /** @return el nombre del cinema */
    public String getNombre() {
        return nombre;
    }

    /** @return la dirección del cinema */
    public String getDireccion() {
        return direccion;
    }

    /** @return los usuarios (lista no modificable) */
    public List<Usuario> getUsuarios() {
        return Collections.unmodifiableList(usuarios);
    }

    /** @return las películas (lista no modificable) */
    public List<Pelicula> getPeliculas() {
        return Collections.unmodifiableList(peliculas);
    }

    /** @return las salas (lista no modificable) */
    public List<Sala> getSalas() {
        return Collections.unmodifiableList(salas);
    }

    /** @return los clientes (lista no modificable) */
    public List<Cliente> getClientes() {
        return Collections.unmodifiableList(clientes);
    }

    /** @return las funciones (lista no modificable) */
    public List<Funcion> getFunciones() {
        return Collections.unmodifiableList(funciones);
    }

    /** @return las ventas (lista no modificable) */
    public List<Venta> getVentas() {
        return Collections.unmodifiableList(ventas);
    }

    /**
     * Devuelve la tarifa de un tipo de cliente.
     *
     * @param tipo tipo de cliente
     * @return la tarifa correspondiente
     */
    public Tarifa getTarifa(TipoCliente tipo) {
        return tarifas.get(tipo);
    }

    // ------------------------------------------------------------------ Usuarios

    /**
     * Registra un usuario.
     *
     * @param usuario usuario a registrar
     * @throws DatoInvalidoException si es nulo o su nombre de usuario ya existe
     */
    public void registrarUsuario(Usuario usuario) throws DatoInvalidoException {
        if (usuario == null) {
            throw new DatoInvalidoException("El usuario es obligatorio");
        }
        for (Usuario existente : usuarios) {
            if (existente.getNombreUsuario().equals(usuario.getNombreUsuario())) {
                throw new DatoInvalidoException("El nombre de usuario "
                        + usuario.getNombreUsuario() + " ya está registrado");
            }
        }
        usuarios.add(usuario);
    }

    /**
     * Inicia sesión con un nombre de usuario y una contraseña.
     *
     * @param nombreUsuario nombre de usuario
     * @param contrasena contraseña
     * @return el usuario cuyas credenciales coinciden
     * @throws CredencialesInvalidasException si ningún usuario coincide
     */
    public Usuario iniciarSesion(String nombreUsuario, String contrasena)
            throws CredencialesInvalidasException {
        for (Usuario usuario : usuarios) {
            if (usuario.validarCredenciales(nombreUsuario, contrasena)) {
                return usuario;
            }
        }
        throw new CredencialesInvalidasException("Usuario o contraseña incorrectos");
    }

    // ----------------------------------------------------------------- Películas

    /**
     * Registra una película.
     *
     * @param pelicula película a registrar
     * @throws DatoInvalidoException si es nula
     * @throws PeliculaDuplicadaException si ya existe una con el mismo código
     */
    public void registrarPelicula(Pelicula pelicula)
            throws DatoInvalidoException, PeliculaDuplicadaException {
        if (pelicula == null) {
            throw new DatoInvalidoException("La película es obligatoria");
        }
        if (encontrarPelicula(pelicula.getCodigo()) != null) {
            throw new PeliculaDuplicadaException(
                    "Ya existe una película con el código " + pelicula.getCodigo());
        }
        peliculas.add(pelicula);
    }

    /**
     * Busca una película por su código.
     *
     * @param codigo código de la película
     * @return la película
     * @throws PeliculaNoEncontradaException si no existe
     */
    public Pelicula buscarPelicula(String codigo) throws PeliculaNoEncontradaException {
        Pelicula pelicula = encontrarPelicula(codigo);
        if (pelicula == null) {
            throw new PeliculaNoEncontradaException(
                    "No existe una película con el código " + normalizar(codigo));
        }
        return pelicula;
    }

    /**
     * Modifica los datos de una película. La duración solo puede cambiar si la película
     * no tiene funciones programadas.
     *
     * @param codigo código de la película
     * @param nombre nuevo nombre
     * @param genero nuevo género
     * @param duracionMinutos nueva duración
     * @param clasificacion nueva clasificación
     * @throws PeliculaNoEncontradaException si no existe
     * @throws TieneFuncionesProgramadasException si cambia la duración y tiene funciones programadas
     * @throws DatoInvalidoException si algún dato es inválido
     */
    public void modificarPelicula(String codigo, String nombre, Genero genero,
                                  int duracionMinutos, Clasificacion clasificacion)
            throws PeliculaNoEncontradaException, TieneFuncionesProgramadasException,
            DatoInvalidoException {
        Pelicula pelicula = buscarPelicula(codigo);
        if (duracionMinutos != pelicula.getDuracionMinutos()
                && tieneFuncionesProgramadas(pelicula)) {
            throw new TieneFuncionesProgramadasException("No se puede cambiar la duración de "
                    + pelicula.getNombre() + " porque tiene funciones programadas");
        }
        // Se crea una película temporal solo para validar todos los datos antes de
        // modificar la real; así, si algo es inválido, no queda modificada a medias.
        new Pelicula(pelicula.getCodigo(), nombre, genero, duracionMinutos, clasificacion);
        pelicula.setNombre(nombre);
        pelicula.setGenero(genero);
        pelicula.setDuracionMinutos(duracionMinutos);
        pelicula.setClasificacion(clasificacion);
    }

    /**
     * Deshabilita una película.
     *
     * @param codigo código de la película
     * @throws PeliculaNoEncontradaException si no existe
     * @throws TieneFuncionesProgramadasException si tiene funciones programadas
     */
    public void deshabilitarPelicula(String codigo)
            throws PeliculaNoEncontradaException, TieneFuncionesProgramadasException {
        Pelicula pelicula = buscarPelicula(codigo);
        if (tieneFuncionesProgramadas(pelicula)) {
            throw new TieneFuncionesProgramadasException("No se puede deshabilitar "
                    + pelicula.getNombre() + " porque tiene funciones programadas");
        }
        pelicula.deshabilitar();
    }

    /**
     * Lista las películas disponibles.
     *
     * @return lista nueva con las películas en estado DISPONIBLE
     */
    public List<Pelicula> listarPeliculasDisponibles() {
        List<Pelicula> disponibles = new ArrayList<>();
        for (Pelicula pelicula : peliculas) {
            if (pelicula.estaDisponible()) {
                disponibles.add(pelicula);
            }
        }
        return disponibles;
    }

    // --------------------------------------------------------------------- Salas

    /**
     * Registra una sala.
     *
     * @param sala sala a registrar
     * @throws DatoInvalidoException si es nula
     * @throws SalaDuplicadaException si ya existe una con el mismo código
     */
    public void registrarSala(Sala sala) throws DatoInvalidoException, SalaDuplicadaException {
        if (sala == null) {
            throw new DatoInvalidoException("La sala es obligatoria");
        }
        if (encontrarSala(sala.getCodigo()) != null) {
            throw new SalaDuplicadaException("Ya existe una sala con el código " + sala.getCodigo());
        }
        salas.add(sala);
    }

    /**
     * Busca una sala por su código.
     *
     * @param codigo código de la sala
     * @return la sala
     * @throws SalaNoEncontradaException si no existe
     */
    public Sala buscarSala(String codigo) throws SalaNoEncontradaException {
        Sala sala = encontrarSala(codigo);
        if (sala == null) {
            throw new SalaNoEncontradaException(
                    "No existe una sala con el código " + normalizar(codigo));
        }
        return sala;
    }

    /**
     * Modifica una sala. La distribución solo puede cambiar si la sala no tiene
     * funciones programadas.
     *
     * @param codigo código de la sala
     * @param nombre nuevo nombre
     * @param filas nuevas filas
     * @param asientosPorFila nuevos asientos por fila
     * @throws SalaNoEncontradaException si no existe
     * @throws TieneFuncionesProgramadasException si cambia la distribución y tiene funciones programadas
     * @throws DatoInvalidoException si algún dato es inválido
     */
    public void modificarSala(String codigo, String nombre, int filas, int asientosPorFila)
            throws SalaNoEncontradaException, TieneFuncionesProgramadasException,
            DatoInvalidoException {
        Sala sala = buscarSala(codigo);
        boolean cambiaDistribucion = filas != sala.getFilas()
                || asientosPorFila != sala.getAsientosPorFila();
        if (cambiaDistribucion && tieneFuncionesProgramadas(sala)) {
            throw new TieneFuncionesProgramadasException("No se puede cambiar la distribución de "
                    + sala.getNombre() + " porque tiene funciones programadas");
        }
        // Sala temporal solo para validar los datos antes de modificar la real.
        new Sala(sala.getCodigo(), nombre, filas, asientosPorFila);
        sala.setNombre(nombre);
        if (cambiaDistribucion) {
            sala.setDistribucion(filas, asientosPorFila);
        }
    }

    /**
     * Deja una sala fuera de servicio.
     *
     * @param codigo código de la sala
     * @throws SalaNoEncontradaException si no existe
     * @throws TieneFuncionesProgramadasException si tiene funciones programadas
     */
    public void deshabilitarSala(String codigo)
            throws SalaNoEncontradaException, TieneFuncionesProgramadasException {
        Sala sala = buscarSala(codigo);
        if (tieneFuncionesProgramadas(sala)) {
            throw new TieneFuncionesProgramadasException("No se puede deshabilitar "
                    + sala.getNombre() + " porque tiene funciones programadas");
        }
        sala.deshabilitar();
    }

    /**
     * Habilita una sala.
     *
     * @param codigo código de la sala
     * @throws SalaNoEncontradaException si no existe
     */
    public void habilitarSala(String codigo) throws SalaNoEncontradaException {
        buscarSala(codigo).habilitar();
    }

    // ------------------------------------------------------------------ Clientes

    /**
     * Registra un cliente.
     *
     * @param cliente cliente a registrar
     * @throws DatoInvalidoException si es nulo
     * @throws ClienteDuplicadoException si ya existe uno con la misma identificación
     */
    public void registrarCliente(Cliente cliente)
            throws DatoInvalidoException, ClienteDuplicadoException {
        if (cliente == null) {
            throw new DatoInvalidoException("El cliente es obligatorio");
        }
        if (encontrarCliente(cliente.getIdentificacion()) != null) {
            throw new ClienteDuplicadoException(
                    "Ya existe un cliente con la identificación " + cliente.getIdentificacion());
        }
        clientes.add(cliente);
    }

    /**
     * Busca un cliente por su identificación.
     *
     * @param identificacion identificación del cliente
     * @return el cliente
     * @throws ClienteNoEncontradoException si no existe
     */
    public Cliente buscarCliente(String identificacion) throws ClienteNoEncontradoException {
        Cliente cliente = encontrarCliente(identificacion);
        if (cliente == null) {
            throw new ClienteNoEncontradoException(
                    "No existe un cliente con la identificación "
                            + (identificacion == null ? "" : identificacion.trim()));
        }
        return cliente;
    }

    // ------------------------------------------------------------------- Tarifas

    /**
     * Cambia el porcentaje de descuento de una tarifa. No altera ventas ya hechas.
     *
     * @param tipoCliente tipo de cliente
     * @param porcentajeDescuento nuevo porcentaje (0 a 100)
     * @throws DatoInvalidoException si el tipo es nulo o el porcentaje está fuera de rango
     */
    public void modificarTarifa(TipoCliente tipoCliente, double porcentajeDescuento)
            throws DatoInvalidoException {
        if (tipoCliente == null) {
            throw new DatoInvalidoException("El tipo de cliente es obligatorio");
        }
        tarifas.get(tipoCliente).setPorcentajeDescuento(porcentajeDescuento);
    }

    // ----------------------------------------------------------------- Funciones

    /**
     * Programa una función. El código se asigna con el consecutivo (F001, F002...),
     * que solo avanza si la función se agrega.
     *
     * @param codigoPelicula código de la película
     * @param codigoSala código de la sala
     * @param fecha fecha de la función
     * @param horaInicio hora de inicio
     * @param valorBase valor base de la entrada
     * @return la función programada
     * @throws PeliculaNoEncontradaException si la película no existe
     * @throws PeliculaNoDisponibleException si la película no está disponible
     * @throws SalaNoEncontradaException si la sala no existe
     * @throws SalaNoDisponibleException si la sala está fuera de servicio
     * @throws DatoInvalidoException si la fecha u hora faltan o ya pasaron, o el valor no es válido
     * @throws CruceHorarioException si se solapa con otra función programada en la misma sala
     */
    public Funcion programarFuncion(String codigoPelicula, String codigoSala, LocalDate fecha,
                                    LocalTime horaInicio, double valorBase)
            throws PeliculaNoEncontradaException, PeliculaNoDisponibleException,
            SalaNoEncontradaException, SalaNoDisponibleException, DatoInvalidoException,
            CruceHorarioException {
        Pelicula pelicula = buscarPelicula(codigoPelicula);
        if (!pelicula.estaDisponible()) {
            throw new PeliculaNoDisponibleException(
                    "La película " + pelicula.getNombre() + " no está disponible");
        }
        Sala sala = buscarSala(codigoSala);
        if (!sala.estaDisponible()) {
            throw new SalaNoDisponibleException(
                    "La sala " + sala.getNombre() + " está fuera de servicio");
        }
        if (fecha == null || horaInicio == null) {
            throw new DatoInvalidoException("La fecha y la hora de la función son obligatorias");
        }
        if (fecha.atTime(horaInicio).isBefore(LocalDateTime.now())) {
            throw new DatoInvalidoException("La fecha y hora de la función no pueden ser pasadas");
        }
        String codigo = String.format("F%03d", consecutivoFuncion);
        Funcion nueva = new Funcion(codigo, pelicula, sala, fecha, horaInicio, valorBase);
        for (Funcion existente : funciones) {
            if (nueva.seCruzaCon(existente)) {
                throw new CruceHorarioException("La función se cruza con " + existente.getCodigo()
                        + " en " + sala.getNombre() + " ("
                        + existente.getFecha().format(FORMATO_FECHA) + " "
                        + existente.getHoraInicio().format(FORMATO_HORA) + " - "
                        + existente.getHoraFin().format(FORMATO_HORA) + ")");
            }
        }
        funciones.add(nueva);
        consecutivoFuncion++;
        return nueva;
    }

    /**
     * Busca una función por su código.
     *
     * @param codigo código de la función
     * @return la función
     * @throws FuncionNoEncontradaException si no existe
     */
    public Funcion buscarFuncion(String codigo) throws FuncionNoEncontradaException {
        String buscado = normalizar(codigo);
        for (Funcion funcion : funciones) {
            if (funcion.getCodigo().equals(buscado)) {
                return funcion;
            }
        }
        throw new FuncionNoEncontradaException("No existe una función con el código " + buscado);
    }

    /**
     * Finaliza una función. Sus ventas siguen ACTIVAS.
     *
     * @param codigo código de la función
     * @throws FuncionNoEncontradaException si no existe
     * @throws FuncionNoDisponibleException si no está programada
     */
    public void finalizarFuncion(String codigo)
            throws FuncionNoEncontradaException, FuncionNoDisponibleException {
        buscarFuncion(codigo).finalizar();
    }

    /**
     * Cancela una función y todas sus ventas activas.
     *
     * @param codigo código de la función
     * @throws FuncionNoEncontradaException si no existe
     * @throws FuncionNoDisponibleException si no está programada
     * @throws VentaYaCanceladaException nunca ocurre aquí, porque solo se cancelan ventas activas
     */
    public void cancelarFuncion(String codigo) throws FuncionNoEncontradaException,
            FuncionNoDisponibleException, VentaYaCanceladaException {
        Funcion funcion = buscarFuncion(codigo);
        if (!funcion.estaProgramada()) {
            throw new FuncionNoDisponibleException("La función " + funcion.getCodigo()
                    + " no está programada (estado: " + funcion.getEstado() + ")");
        }
        for (Venta venta : ventas) {
            if (venta.estaActiva() && venta.getFuncion() == funcion) {
                venta.cancelar();
            }
        }
        funcion.cancelar();
    }

    /**
     * Lista las funciones programadas.
     *
     * @return lista nueva con las funciones en estado PROGRAMADA
     */
    public List<Funcion> listarFuncionesProgramadas() {
        List<Funcion> programadas = new ArrayList<>();
        for (Funcion funcion : funciones) {
            if (funcion.estaProgramada()) {
                programadas.add(funcion);
            }
        }
        return programadas;
    }

    // -------------------------------------------------------------------- Ventas

    /**
     * Vende una entrada. El valor se calcula con la tarifa del tipo de cliente y queda
     * guardado en la venta.
     *
     * @param codigoFuncion código de la función
     * @param identificacionCliente identificación del cliente
     * @param codigoAsiento código del asiento (por ejemplo "B3")
     * @return la venta registrada
     * @throws FuncionNoEncontradaException si la función no existe
     * @throws FuncionNoDisponibleException si la función no está programada
     * @throws ClienteNoEncontradoException si el cliente no existe
     * @throws FuncionLlenaException si no quedan asientos disponibles
     * @throws AsientoNoEncontradoException si el asiento no existe en la función
     * @throws AsientoOcupadoException si el asiento ya está ocupado
     * @throws DatoInvalidoException si los datos de la venta no son válidos
     */
    public Venta venderEntrada(String codigoFuncion, String identificacionCliente,
                               String codigoAsiento)
            throws FuncionNoEncontradaException, FuncionNoDisponibleException,
            ClienteNoEncontradoException, FuncionLlenaException, AsientoNoEncontradoException,
            AsientoOcupadoException, DatoInvalidoException {
        Funcion funcion = buscarFuncion(codigoFuncion);
        if (!funcion.estaProgramada()) {
            throw new FuncionNoDisponibleException("La función " + funcion.getCodigo()
                    + " no está programada (estado: " + funcion.getEstado() + ")");
        }
        Cliente cliente = buscarCliente(identificacionCliente);
        if (funcion.contarDisponibles() == 0) {
            throw new FuncionLlenaException(
                    "La función " + funcion.getCodigo() + " no tiene asientos disponibles");
        }
        Asiento asiento = funcion.buscarAsiento(codigoAsiento);
        if (!asiento.estaDisponible()) {
            throw new AsientoOcupadoException("El asiento " + asiento.getCodigo()
                    + " ya está ocupado en la función " + funcion.getCodigo());
        }
        double valor = getTarifa(cliente.getTipoCliente()).calcularValor(funcion.getValorBase());
        asiento.ocupar();
        String codigo = String.format("V%03d", consecutivoVenta);
        Venta venta = new Venta(codigo, cliente, funcion, asiento, LocalDateTime.now(), valor);
        ventas.add(venta);
        consecutivoVenta++;
        return venta;
    }

    /**
     * Cancela una venta y libera el asiento de la función.
     *
     * @param codigoVenta código de la venta
     * @throws VentaNoEncontradaException si la venta no existe
     * @throws VentaYaCanceladaException si ya estaba cancelada
     * @throws FuncionNoDisponibleException si su función ya no está programada
     */
    public void cancelarVenta(String codigoVenta) throws VentaNoEncontradaException,
            VentaYaCanceladaException, FuncionNoDisponibleException {
        Venta venta = buscarVenta(codigoVenta);
        if (!venta.estaActiva()) {
            throw new VentaYaCanceladaException(
                    "La venta " + venta.getCodigo() + " ya estaba cancelada");
        }
        Funcion funcion = venta.getFuncion();
        if (!funcion.estaProgramada()) {
            throw new FuncionNoDisponibleException("No se puede cancelar la venta "
                    + venta.getCodigo() + " porque la función " + funcion.getCodigo()
                    + " no está programada (estado: " + funcion.getEstado() + ")");
        }
        venta.cancelar();
    }

    /**
     * Busca una venta por su código.
     *
     * @param codigo código de la venta
     * @return la venta
     * @throws VentaNoEncontradaException si no existe
     */
    public Venta buscarVenta(String codigo) throws VentaNoEncontradaException {
        String buscado = normalizar(codigo);
        for (Venta venta : ventas) {
            if (venta.getCodigo().equals(buscado)) {
                return venta;
            }
        }
        throw new VentaNoEncontradaException("No existe una venta con el código " + buscado);
    }

    // ------------------------------------------------------------------ Reportes

    /**
     * Cuenta las entradas vendidas (ventas ACTIVAS).
     *
     * @return cantidad de ventas activas
     */
    public int totalEntradasVendidas() {
        int total = 0;
        for (Venta venta : ventas) {
            if (venta.estaActiva()) {
                total++;
            }
        }
        return total;
    }

    /**
     * Suma lo pagado en las ventas ACTIVAS.
     *
     * @return total de ingresos
     */
    public double totalIngresos() {
        double total = 0;
        for (Venta venta : ventas) {
            if (venta.estaActiva()) {
                total += venta.getValorPagado();
            }
        }
        return total;
    }

    /**
     * Devuelve la película (o películas, si hay empate) con más ventas ACTIVAS.
     *
     * @return lista con las películas más vendidas; vacía si no hay ventas activas
     */
    public List<Pelicula> peliculasMasVendidas() {
        int maximo = 0;
        for (Pelicula pelicula : peliculas) {
            int cantidad = contarVentasActivas(pelicula);
            if (cantidad > maximo) {
                maximo = cantidad;
            }
        }
        List<Pelicula> masVendidas = new ArrayList<>();
        if (maximo == 0) {
            return masVendidas;
        }
        for (Pelicula pelicula : peliculas) {
            if (contarVentasActivas(pelicula) == maximo) {
                masVendidas.add(pelicula);
            }
        }
        return masVendidas;
    }

    /**
     * Calcula el porcentaje de ocupación de una función.
     *
     * @param codigoFuncion código de la función
     * @return porcentaje de ocupación (0 a 100)
     * @throws FuncionNoEncontradaException si la función no existe
     */
    public double ocupacionFuncion(String codigoFuncion) throws FuncionNoEncontradaException {
        return buscarFuncion(codigoFuncion).calcularOcupacion();
    }

    /**
     * Lista las funciones PROGRAMADAS de una fecha, ordenadas por hora de inicio.
     *
     * @param fecha fecha a consultar
     * @return lista nueva de funciones
     * @throws DatoInvalidoException si la fecha es nula
     */
    public List<Funcion> funcionesPorFecha(LocalDate fecha) throws DatoInvalidoException {
        if (fecha == null) {
            throw new DatoInvalidoException("La fecha es obligatoria");
        }
        List<Funcion> resultado = new ArrayList<>();
        for (Funcion funcion : funciones) {
            if (funcion.estaProgramada() && funcion.getFecha().equals(fecha)) {
                // Inserción ordenada: se ubica antes de la primera función que empiece más tarde.
                int posicion = resultado.size();
                for (int i = 0; i < resultado.size(); i++) {
                    if (funcion.getHoraInicio().isBefore(resultado.get(i).getHoraInicio())) {
                        posicion = i;
                        break;
                    }
                }
                resultado.add(posicion, funcion);
            }
        }
        return resultado;
    }

    // ------------------------------------------------------------------ Auxiliares

    private int contarVentasActivas(Pelicula pelicula) {
        int cantidad = 0;
        for (Venta venta : ventas) {
            if (venta.estaActiva()
                    && venta.getFuncion().getPelicula().getCodigo().equals(pelicula.getCodigo())) {
                cantidad++;
            }
        }
        return cantidad;
    }

    private String normalizar(String codigo) {
        return codigo == null ? "" : codigo.trim().toUpperCase();
    }

    private Pelicula encontrarPelicula(String codigo) {
        String buscado = normalizar(codigo);
        for (Pelicula pelicula : peliculas) {
            if (pelicula.getCodigo().equals(buscado)) {
                return pelicula;
            }
        }
        return null;
    }

    private Sala encontrarSala(String codigo) {
        String buscado = normalizar(codigo);
        for (Sala sala : salas) {
            if (sala.getCodigo().equals(buscado)) {
                return sala;
            }
        }
        return null;
    }

    private Cliente encontrarCliente(String identificacion) {
        String buscada = identificacion == null ? "" : identificacion.trim();
        for (Cliente cliente : clientes) {
            if (cliente.getIdentificacion().equals(buscada)) {
                return cliente;
            }
        }
        return null;
    }

    private boolean tieneFuncionesProgramadas(Pelicula pelicula) {
        for (Funcion funcion : funciones) {
            if (funcion.estaProgramada()
                    && funcion.getPelicula().getCodigo().equals(pelicula.getCodigo())) {
                return true;
            }
        }
        return false;
    }

    private boolean tieneFuncionesProgramadas(Sala sala) {
        for (Funcion funcion : funciones) {
            if (funcion.estaProgramada()
                    && funcion.getSala().getCodigo().equals(sala.getCodigo())) {
                return true;
            }
        }
        return false;
    }
}
