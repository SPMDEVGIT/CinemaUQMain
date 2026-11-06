package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.util.Contexto;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * Pestaña "Películas": tabla de solo lectura con las películas disponibles.
 */
public class PeliculasControlador implements Recargable {

    @FXML
    private TableView<Pelicula> tabla;

    @FXML
    private TableColumn<Pelicula, String> colNombre;

    @FXML
    private TableColumn<Pelicula, String> colGenero;

    @FXML
    private TableColumn<Pelicula, String> colDuracion;

    @FXML
    private TableColumn<Pelicula, String> colClasificacion;

    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colGenero.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getGenero().toString()));
        colDuracion.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getDuracionMinutos() + " min"));
        colClasificacion.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getClasificacion().toString()));
        recargar();
    }

    @Override
    public void recargar() {
        tabla.getItems().setAll(Contexto.getCinema().listarPeliculasDisponibles());
    }
}
