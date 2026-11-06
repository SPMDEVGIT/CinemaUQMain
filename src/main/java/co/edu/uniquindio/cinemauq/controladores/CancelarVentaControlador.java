package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Venta;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import co.edu.uniquindio.cinemauq.util.Formato;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Pestaña "Cancelar venta": tabla de ventas filtrable por código y cancelación con confirmación.
 */
public class CancelarVentaControlador implements Recargable {

    @FXML
    private TextField txtFiltro;

    @FXML
    private TableView<Venta> tabla;

    @FXML
    private TableColumn<Venta, String> colCodigo;

    @FXML
    private TableColumn<Venta, String> colFuncion;

    @FXML
    private TableColumn<Venta, String> colPelicula;

    @FXML
    private TableColumn<Venta, String> colCliente;

    @FXML
    private TableColumn<Venta, String> colAsiento;

    @FXML
    private TableColumn<Venta, String> colValor;

    @FXML
    private TableColumn<Venta, String> colFechaHora;

    @FXML
    private TableColumn<Venta, String> colEstado;

    private final ObservableList<Venta> datos = FXCollections.observableArrayList();
    private final FilteredList<Venta> filtradas = new FilteredList<>(datos, venta -> true);

    @FXML
    private void initialize() {
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colFuncion.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getFuncion().getCodigo()));
        colPelicula.setCellValueFactory(c -> new SimpleStringProperty(
                c.getValue().getFuncion().getPelicula().getNombre()));
        colCliente.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getCliente().getNombre()));
        colAsiento.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getAsiento().getCodigo()));
        colValor.setCellValueFactory(
                c -> new SimpleStringProperty(Formato.moneda(c.getValue().getValorPagado())));
        colFechaHora.setCellValueFactory(
                c -> new SimpleStringProperty(Formato.fechaHora(c.getValue().getFechaHora())));
        colEstado.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getEstado().toString()));
        tabla.setItems(filtradas);
        txtFiltro.textProperty().addListener((obs, anterior, nuevo) -> filtradas.setPredicate(
                venta -> venta.getCodigo().contains(nuevo.trim().toUpperCase())));
        recargar();
    }

    @Override
    public void recargar() {
        datos.setAll(Contexto.getCinema().getVentas());
        tabla.refresh();
    }

    /** Cancela la venta seleccionada, previa confirmación. */
    @FXML
    private void cancelarVenta() {
        Venta venta = tabla.getSelectionModel().getSelectedItem();
        if (venta == null) {
            Alertas.error("Selecciona una venta de la tabla");
            return;
        }
        if (!Alertas.confirmar("¿Cancelar la venta " + venta.getCodigo() + " del asiento "
                + venta.getAsiento().getCodigo() + "?")) {
            return;
        }
        try {
            Contexto.getCinema().cancelarVenta(venta.getCodigo());
            Alertas.informacion("La venta " + venta.getCodigo() + " fue cancelada");
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
        recargar();
    }
}
