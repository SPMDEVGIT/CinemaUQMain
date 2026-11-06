package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Cinema;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import co.edu.uniquindio.cinemauq.util.Formato;
import java.time.LocalDate;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Pestaña "Reportes": totales, película más vendida, ocupación de una función y
 * funciones programadas por fecha.
 */
public class ReportesControlador implements Recargable {

    @FXML
    private Label lblEntradas;

    @FXML
    private Label lblIngresos;

    @FXML
    private Label lblMasVendidas;

    @FXML
    private ComboBox<Funcion> cmbFunciones;

    @FXML
    private Label lblDisponibles;

    @FXML
    private Label lblOcupados;

    @FXML
    private Label lblPorcentaje;

    @FXML
    private ProgressBar barra;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TableView<Funcion> tabla;

    @FXML
    private TableColumn<Funcion, String> colCodigo;

    @FXML
    private TableColumn<Funcion, String> colPelicula;

    @FXML
    private TableColumn<Funcion, String> colSala;

    @FXML
    private TableColumn<Funcion, String> colHora;

    @FXML
    private TableColumn<Funcion, String> colValor;

    @FXML
    private TableColumn<Funcion, String> colDisponibles;

    @FXML
    private void initialize() {
        cmbFunciones.setConverter(Formato.convertidorFuncion());
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colPelicula.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getPelicula().getNombre()));
        colSala.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getSala().getNombre()));
        colHora.setCellValueFactory(
                c -> new SimpleStringProperty(Formato.hora(c.getValue().getHoraInicio())));
        colValor.setCellValueFactory(
                c -> new SimpleStringProperty(Formato.moneda(c.getValue().getValorBase())));
        colDisponibles.setCellValueFactory(
                c -> new SimpleStringProperty(String.valueOf(c.getValue().contarDisponibles())));
        cmbFunciones.valueProperty().addListener((obs, anterior, nueva) -> mostrarOcupacion());
        dpFecha.valueProperty().addListener((obs, anterior, nueva) -> mostrarFuncionesPorFecha());
        dpFecha.setValue(LocalDate.now().plusDays(1));
        recargar();
    }

    @Override
    public void recargar() {
        Cinema cinema = Contexto.getCinema();
        lblEntradas.setText(String.valueOf(cinema.totalEntradasVendidas()));
        lblIngresos.setText(Formato.moneda(cinema.totalIngresos()));
        lblMasVendidas.setText(textoMasVendidas(cinema.peliculasMasVendidas()));

        Funcion anterior = cmbFunciones.getValue();
        cmbFunciones.getItems().setAll(cinema.getFunciones());
        Funcion elegida = null;
        for (Funcion funcion : cmbFunciones.getItems()) {
            if (anterior != null && funcion.getCodigo().equals(anterior.getCodigo())) {
                elegida = funcion;
            }
        }
        cmbFunciones.setValue(elegida);
        mostrarOcupacion();
        mostrarFuncionesPorFecha();
    }

    private String textoMasVendidas(List<Pelicula> peliculas) {
        if (peliculas.isEmpty()) {
            return "Sin ventas";
        }
        StringBuilder texto = new StringBuilder();
        for (Pelicula pelicula : peliculas) {
            if (texto.length() > 0) {
                texto.append(", ");
            }
            texto.append(pelicula.getNombre());
        }
        return texto.toString();
    }

    private void mostrarOcupacion() {
        Funcion funcion = cmbFunciones.getValue();
        if (funcion == null) {
            lblDisponibles.setText("Disponibles: —");
            lblOcupados.setText("Ocupados: —");
            lblPorcentaje.setText("—");
            barra.setProgress(0);
            return;
        }
        try {
            double porcentaje = Contexto.getCinema().ocupacionFuncion(funcion.getCodigo());
            lblDisponibles.setText("Disponibles: " + funcion.contarDisponibles());
            lblOcupados.setText("Ocupados: " + funcion.contarOcupados());
            lblPorcentaje.setText(String.format("%.1f %%", porcentaje));
            barra.setProgress(porcentaje / 100);
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }

    private void mostrarFuncionesPorFecha() {
        LocalDate fecha = dpFecha.getValue();
        if (fecha == null) {
            tabla.getItems().clear();
            return;
        }
        try {
            tabla.getItems().setAll(Contexto.getCinema().funcionesPorFecha(fecha));
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }
}
