package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Cinema;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Pestaña "Gestión de salas": registrar, modificar, deshabilitar y habilitar salas.
 */
public class GestionSalasControlador implements Recargable {

    @FXML
    private TextField txtCodigo;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtFilas;

    @FXML
    private TextField txtAsientosPorFila;

    @FXML
    private Button btnGuardar;

    @FXML
    private TableView<Sala> tabla;

    @FXML
    private TableColumn<Sala, String> colCodigo;

    @FXML
    private TableColumn<Sala, String> colNombre;

    @FXML
    private TableColumn<Sala, String> colFilas;

    @FXML
    private TableColumn<Sala, String> colAsientosPorFila;

    @FXML
    private TableColumn<Sala, String> colCapacidad;

    @FXML
    private TableColumn<Sala, String> colEstado;

    /** Sala que se está modificando; null cuando el formulario registra una nueva. */
    private Sala seleccionada;

    @FXML
    private void initialize() {
        colCodigo.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCodigo()));
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colFilas.setCellValueFactory(
                c -> new SimpleStringProperty(String.valueOf(c.getValue().getFilas())));
        colAsientosPorFila.setCellValueFactory(
                c -> new SimpleStringProperty(String.valueOf(c.getValue().getAsientosPorFila())));
        colCapacidad.setCellValueFactory(
                c -> new SimpleStringProperty(String.valueOf(c.getValue().getCapacidad())));
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
        tabla.getItems().setAll(Contexto.getCinema().getSalas());
        tabla.refresh();
        limpiar();
    }

    /** Registra una sala nueva o guarda los cambios de la seleccionada. */
    @FXML
    private void guardar() {
        Cinema cinema = Contexto.getCinema();
        try {
            int filas = Integer.parseInt(txtFilas.getText().trim());
            int asientosPorFila = Integer.parseInt(txtAsientosPorFila.getText().trim());
            if (seleccionada == null) {
                cinema.registrarSala(
                        new Sala(txtCodigo.getText(), txtNombre.getText(), filas, asientosPorFila));
                Alertas.informacion("Sala registrada");
            } else {
                cinema.modificarSala(seleccionada.getCodigo(), txtNombre.getText(), filas,
                        asientosPorFila);
                Alertas.informacion("Sala modificada");
            }
            recargar();
        } catch (NumberFormatException e) {
            Alertas.error("Ingresa un valor numérico válido");
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }

    /** Vacía el formulario para registrar una sala nueva. */
    @FXML
    private void nuevo() {
        limpiar();
    }

    /** Deja la sala seleccionada fuera de servicio, previa confirmación. */
    @FXML
    private void deshabilitar() {
        if (seleccionada == null) {
            Alertas.error("Selecciona una sala de la tabla");
            return;
        }
        if (!Alertas.confirmar("¿Deshabilitar la sala " + seleccionada.getNombre() + "?")) {
            return;
        }
        try {
            Contexto.getCinema().deshabilitarSala(seleccionada.getCodigo());
            Alertas.informacion("Sala deshabilitada");
            recargar();
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }

    /** Habilita la sala seleccionada, previa confirmación. */
    @FXML
    private void habilitar() {
        if (seleccionada == null) {
            Alertas.error("Selecciona una sala de la tabla");
            return;
        }
        if (!Alertas.confirmar("¿Habilitar la sala " + seleccionada.getNombre() + "?")) {
            return;
        }
        try {
            Contexto.getCinema().habilitarSala(seleccionada.getCodigo());
            Alertas.informacion("Sala habilitada");
            recargar();
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }

    private void cargarFormulario(Sala sala) {
        seleccionada = sala;
        txtCodigo.setText(sala.getCodigo());
        txtCodigo.setDisable(true);
        txtNombre.setText(sala.getNombre());
        txtFilas.setText(String.valueOf(sala.getFilas()));
        txtAsientosPorFila.setText(String.valueOf(sala.getAsientosPorFila()));
        btnGuardar.setText("Guardar cambios");
    }

    private void limpiar() {
        seleccionada = null;
        tabla.getSelectionModel().clearSelection();
        txtCodigo.clear();
        txtCodigo.setDisable(false);
        txtNombre.clear();
        txtFilas.clear();
        txtAsientosPorFila.clear();
        btnGuardar.setText("Registrar");
    }
}
