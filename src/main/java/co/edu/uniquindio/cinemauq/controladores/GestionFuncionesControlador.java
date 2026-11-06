package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.modelo.Pelicula;
import co.edu.uniquindio.cinemauq.modelo.Sala;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import co.edu.uniquindio.cinemauq.util.Formato;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.util.StringConverter;

/**
 * Pestaña "Gestión de funciones": programar, finalizar y cancelar funciones.
 */
public class GestionFuncionesControlador implements Recargable {

    @FXML
    private ComboBox<Pelicula> cmbPelicula;

    @FXML
    private ComboBox<Sala> cmbSala;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TextField txtHora;

    @FXML
    private TextField txtValor;

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
    private TableColumn<Funcion, String> colEstado;

    @FXML
    private void initialize() {
        cmbPelicula.setConverter(new StringConverter<>() {
            @Override
            public String toString(Pelicula pelicula) {
                return pelicula == null ? "" : pelicula.getCodigo() + " · " + pelicula.getNombre();
            }

            @Override
            public Pelicula fromString(String texto) {
                return null;
            }
        });
        cmbSala.setConverter(new StringConverter<>() {
            @Override
            public String toString(Sala sala) {
                return sala == null ? "" : sala.getCodigo() + " · " + sala.getNombre();
            }

            @Override
            public Sala fromString(String texto) {
                return null;
            }
        });
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
        colEstado.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getEstado().toString()));
        recargar();
    }

    @Override
    public void recargar() {
        cmbPelicula.getItems().setAll(Contexto.getCinema().listarPeliculasDisponibles());
        cmbPelicula.setValue(null);
        cmbSala.getItems().clear();
        for (Sala sala : Contexto.getCinema().getSalas()) {
            if (sala.estaDisponible()) {
                cmbSala.getItems().add(sala);
            }
        }
        cmbSala.setValue(null);
        refrescarTabla();
    }

    /** Programa una función con los datos del formulario. */
    @FXML
    private void programar() {
        Pelicula pelicula = cmbPelicula.getValue();
        Sala sala = cmbSala.getValue();
        if (pelicula == null) {
            Alertas.error("Selecciona una película");
            return;
        }
        if (sala == null) {
            Alertas.error("Selecciona una sala");
            return;
        }
        try {
            LocalTime hora = LocalTime.parse(txtHora.getText().trim());
            double valor = Double.parseDouble(txtValor.getText().trim());
            Funcion funcion = Contexto.getCinema().programarFuncion(
                    pelicula.getCodigo(), sala.getCodigo(), dpFecha.getValue(), hora, valor);
            txtHora.clear();
            txtValor.clear();
            refrescarTabla();
            Alertas.informacion("Función " + funcion.getCodigo() + " programada");
        } catch (DateTimeParseException e) {
            Alertas.error("La hora debe tener el formato HH:mm");
        } catch (NumberFormatException e) {
            Alertas.error("Ingresa un valor numérico válido");
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }

    /** Finaliza la función seleccionada, previa confirmación. */
    @FXML
    private void finalizar() {
        Funcion funcion = tabla.getSelectionModel().getSelectedItem();
        if (funcion == null) {
            Alertas.error("Selecciona una función de la tabla");
            return;
        }
        if (!Alertas.confirmar("¿Finalizar la función " + funcion.getCodigo()
                + "? Sus ventas seguirán activas.")) {
            return;
        }
        try {
            Contexto.getCinema().finalizarFuncion(funcion.getCodigo());
            Alertas.informacion("Función " + funcion.getCodigo() + " finalizada");
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
        refrescarTabla();
    }

    /** Cancela la función seleccionada y sus ventas activas, previa confirmación. */
    @FXML
    private void cancelar() {
        Funcion funcion = tabla.getSelectionModel().getSelectedItem();
        if (funcion == null) {
            Alertas.error("Selecciona una función de la tabla");
            return;
        }
        if (!Alertas.confirmar("¿Cancelar la función " + funcion.getCodigo()
                + "? También se cancelarán todas sus ventas activas.")) {
            return;
        }
        try {
            Contexto.getCinema().cancelarFuncion(funcion.getCodigo());
            Alertas.informacion("Función " + funcion.getCodigo() + " cancelada");
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
        refrescarTabla();
    }

    private void refrescarTabla() {
        tabla.getItems().setAll(Contexto.getCinema().getFunciones());
        tabla.refresh();
    }
}
