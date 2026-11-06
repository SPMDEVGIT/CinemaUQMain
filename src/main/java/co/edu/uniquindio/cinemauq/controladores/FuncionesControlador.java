package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.util.Contexto;
import co.edu.uniquindio.cinemauq.util.Formato;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Pestaña "Funciones": tabla de solo lectura con las funciones programadas.
 */
public class FuncionesControlador implements Recargable {

    @FXML
    private TableView<Funcion> tabla;

    @FXML
    private TableColumn<Funcion, String> colCodigo;

    @FXML
    private TableColumn<Funcion, String> colPelicula;

    @FXML
    private TableColumn<Funcion, String> colSala;

    @FXML
    private TableColumn<Funcion, String> colFecha;

    @FXML
    private TableColumn<Funcion, String> colHora;

    @FXML
    private TableColumn<Funcion, String> colValor;

    @FXML
    private TableColumn<Funcion, String> colDisponibles;

    @FXML
    private void initialize() {
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colPelicula.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getPelicula().getNombre()));
        colSala.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getSala().getNombre()));
        colFecha.setCellValueFactory(
                c -> new SimpleStringProperty(Formato.fecha(c.getValue().getFecha())));
        colHora.setCellValueFactory(
                c -> new SimpleStringProperty(Formato.hora(c.getValue().getHoraInicio())));
        colValor.setCellValueFactory(
                c -> new SimpleStringProperty(Formato.moneda(c.getValue().getValorBase())));
        colDisponibles.setCellValueFactory(
                c -> new SimpleStringProperty(String.valueOf(c.getValue().contarDisponibles())));
        recargar();
    }

    @Override
    public void recargar() {
        tabla.getItems().setAll(Contexto.getCinema().listarFuncionesProgramadas());
    }
}
