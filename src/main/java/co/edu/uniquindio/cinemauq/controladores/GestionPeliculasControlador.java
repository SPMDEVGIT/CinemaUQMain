package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.enumeraciones.Clasificacion;
import co.edu.uniquindio.cinemauq.enumeraciones.Genero;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Cinema;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Pestaña "Gestión de películas": registrar, modificar y deshabilitar películas.
 */
public class GestionPeliculasControlador implements Recargable {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private ComboBox<Genero> cmbGenero;

    @FXML
    private TextField txtDuracion;

    @FXML
    private ComboBox<Clasificacion> cmbClasificacion;

    @FXML
    private Button btnGuardar;

    @FXML
    private TableView<Pelicula> tabla;

    @FXML
    private TableColumn<Pelicula, String> colCodigo;

    @FXML
    private TableColumn<Pelicula, String> colNombre;

    @FXML
    private TableColumn<Pelicula, String> colGenero;

    @FXML
    private TableColumn<Pelicula, String> colDuracion;

    @FXML
    private TableColumn<Pelicula, String> colClasificacion;

    @FXML
    private TableColumn<Pelicula, String> colEstado;

    /** Película que se está modificando; null cuando el formulario registra una nueva. */
    private Pelicula seleccionada;

    @FXML
    private void initialize() {
        cmbGenero.getItems().setAll(Genero.values());
        cmbClasificacion.getItems().setAll(Clasificacion.values());
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colGenero.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getGenero().toString()));
        colDuracion.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getDuracionMinutos() + " min"));
        colClasificacion.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getClasificacion().toString()));
        colEstado.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getEstado().toString()));
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nueva) -> {
            if (nueva != null) {
                cargarFormulario(nueva);
            }
        });
        recargar();
    }

    @Override
    public void recargar() {
        tabla.getItems().setAll(Contexto.getCinema().getPeliculas());
        tabla.refresh();
        limpiar();
    }

    /** Registra una película nueva o guarda los cambios de la seleccionada. */
    @FXML
    private void guardar() {
        Cinema cinema = Contexto.getCinema();
        try {
            int duracion = Integer.parseInt(txtDuracion.getText().trim());
            if (seleccionada == null) {
                cinema.registrarPelicula(new Pelicula(txtCodigo.getText(), txtNombre.getText(),
                        cmbGenero.getValue(), duracion, cmbClasificacion.getValue()));
                Alertas.informacion("Película registrada");
            } else {
                cinema.modificarPelicula(seleccionada.getCodigo(), txtNombre.getText(),
                        cmbGenero.getValue(), duracion, cmbClasificacion.getValue());
                Alertas.informacion("Película modificada");
            }
            recargar();
        } catch (NumberFormatException e) {
            Alertas.error("Ingresa un valor numérico válido");
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }

    /** Vacía el formulario para registrar una película nueva. */
    @FXML
    private void nuevo() {
        limpiar();
    }

    /** Deshabilita la película seleccionada, previa confirmación. */
    @FXML
    private void deshabilitar() {
        if (seleccionada == null) {
            Alertas.error("Selecciona una película de la tabla");
            return;
        }
        if (!Alertas.confirmar("¿Deshabilitar la película " + seleccionada.getNombre() + "?")) {
            return;
        }
        try {
            Contexto.getCinema().deshabilitarPelicula(seleccionada.getCodigo());
            Alertas.informacion("Película deshabilitada");
            recargar();
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }

    private void cargarFormulario(Pelicula pelicula) {
        seleccionada = pelicula;
        txtCodigo.setText(pelicula.getCodigo());
        txtCodigo.setDisable(true);
        txtNombre.setText(pelicula.getNombre());
        cmbGenero.setValue(pelicula.getGenero());
        txtDuracion.setText(String.valueOf(pelicula.getDuracionMinutos()));
        cmbClasificacion.setValue(pelicula.getClasificacion());
        btnGuardar.setText("Guardar cambios");
    }

    private void limpiar() {
        seleccionada = null;
        tabla.getSelectionModel().clearSelection();
        txtCodigo.clear();
        txtCodigo.setDisable(false);
        txtNombre.clear();
        cmbGenero.setValue(null);
        txtDuracion.clear();
        cmbClasificacion.setValue(null);
        btnGuardar.setText("Registrar");
    }
}
