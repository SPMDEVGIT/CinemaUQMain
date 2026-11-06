package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.excepciones.ClienteNoEncontradoException;
import co.edu.uniquindio.cinemauq.modelo.Cinema;
import co.edu.uniquindio.cinemauq.modelo.Cliente;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.Venta;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import co.edu.uniquindio.cinemauq.util.Formato;
import co.edu.uniquindio.cinemauq.util.MapaAsientos;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

/**
 * Pestaña "Vender": elegir función, buscar cliente, escoger asiento y confirmar la venta.
 */
public class VenderControlador implements Recargable {

    @FXML
    private ComboBox<Funcion> cmbFunciones;

    @FXML
    private TextField txtIdentificacion;

    @FXML
    private Label lblCliente;

    @FXML
    private Label lblAsiento;

    @FXML
    private Label lblValor;

    @FXML
    private ScrollPane scrollMapa;

    private final MapaAsientos mapa = new MapaAsientos(true);
    private Cliente clienteActual;

    @FXML
    private void initialize() {
        cmbFunciones.setConverter(Formato.convertidorFuncion());
        mapa.setAlCambiarSeleccion(codigo -> actualizarResumen());
        VBox contenedor = new VBox(12, MapaAsientos.crearLeyenda(), mapa);
        contenedor.setAlignment(Pos.TOP_CENTER);
        scrollMapa.setContent(contenedor);
        cmbFunciones.valueProperty().addListener((obs, anterior, nueva) -> mostrarMapa());
        txtIdentificacion.textProperty().addListener((obs, anterior, nuevo) -> olvidarCliente());
        txtIdentificacion.setOnAction(e -> buscarCliente());
        recargar();
    }

    @Override
    public void recargar() {
        Funcion anterior = cmbFunciones.getValue();
        cmbFunciones.getItems().setAll(Contexto.getCinema().listarFuncionesProgramadas());
        Funcion elegida = null;
        for (Funcion funcion : cmbFunciones.getItems()) {
            if (anterior != null && funcion.getCodigo().equals(anterior.getCodigo())) {
                elegida = funcion;
            }
        }
        cmbFunciones.setValue(elegida);
        mostrarMapa();
    }

    /** Busca al cliente por su identificación y muestra su nombre y tipo. */
    @FXML
    private void buscarCliente() {
        try {
            clienteActual = Contexto.getCinema().buscarCliente(txtIdentificacion.getText());
            lblCliente.setText(clienteActual.getNombre() + " · " + clienteActual.getTipoCliente());
        } catch (ClienteNoEncontradoException e) {
            olvidarCliente();
            Alertas.error(e.getMessage());
        }
        actualizarResumen();
    }

    /** Vende la entrada para la función, el cliente y el asiento elegidos. */
    @FXML
    private void confirmarVenta() {
        Funcion funcion = cmbFunciones.getValue();
        String asiento = mapa.getCodigoSeleccionado();
        if (funcion == null) {
            Alertas.error("Selecciona una función");
            return;
        }
        if (clienteActual == null) {
            Alertas.error("Busca un cliente por su identificación antes de vender");
            return;
        }
        if (asiento == null) {
            Alertas.error("Selecciona un asiento en el mapa");
            return;
        }
        try {
            Venta venta = Contexto.getCinema().venderEntrada(
                    funcion.getCodigo(), clienteActual.getIdentificacion(), asiento);
            Alertas.informacion(textoEntrada(venta));
            txtIdentificacion.clear();
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
        mostrarMapa();
    }

    private void mostrarMapa() {
        mapa.mostrar(cmbFunciones.getValue());
        actualizarResumen();
    }

    private void olvidarCliente() {
        clienteActual = null;
        lblCliente.setText("");
        actualizarResumen();
    }

    private void actualizarResumen() {
        String asiento = mapa.getCodigoSeleccionado();
        lblAsiento.setText("Asiento: " + (asiento == null ? "—" : asiento));
        Funcion funcion = cmbFunciones.getValue();
        if (funcion != null && clienteActual != null) {
            Cinema cinema = Contexto.getCinema();
            double valor = cinema.getTarifa(clienteActual.getTipoCliente())
                    .calcularValor(funcion.getValorBase());
            lblValor.setText("Valor a pagar: " + Formato.moneda(valor));
        } else {
            lblValor.setText("Valor a pagar: —");
        }
    }

    private String textoEntrada(Venta venta) {
        Funcion funcion = venta.getFuncion();
        return "Entrada generada\n\n"
                + "Código de venta: " + venta.getCodigo() + "\n"
                + "Película: " + funcion.getPelicula().getNombre() + "\n"
                + "Sala: " + funcion.getSala().getNombre() + "\n"
                + "Fecha: " + Formato.fecha(funcion.getFecha()) + "\n"
                + "Hora: " + Formato.hora(funcion.getHoraInicio()) + "\n"
                + "Asiento: " + venta.getAsiento().getCodigo() + "\n"
                + "Cliente: " + venta.getCliente().getNombre() + "\n"
                + "Tipo de cliente: " + venta.getCliente().getTipoCliente() + "\n"
                + "Valor pagado: " + Formato.moneda(venta.getValorPagado()) + "\n"
                + "Fecha y hora de venta: " + Formato.fechaHora(venta.getFechaHora());
    }
}
