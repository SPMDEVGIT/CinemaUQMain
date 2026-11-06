package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.enumeraciones.TipoCliente;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Tarifa;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import co.edu.uniquindio.cinemauq.util.Formato;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

/**
 * Pestaña "Tarifas": ver y modificar el porcentaje de descuento de cada tipo de cliente.
 */
public class TarifasControlador implements Recargable {

    @FXML
    private TableView<Tarifa> tabla;

    @FXML
    private TableColumn<Tarifa, String> colTipo;

    @FXML
    private TableColumn<Tarifa, String> colPorcentaje;

    @FXML
    private Label lblTipo;

    @FXML
    private TextField txtPorcentaje;

    private TipoCliente tipoSeleccionado;

    @FXML
    private void initialize() {
        colTipo.setCellValueFactory(
                c -> new SimpleStringProperty(c.getValue().getTipoCliente().toString()));
        colPorcentaje.setCellValueFactory(c -> new SimpleStringProperty(
                Formato.porcentaje(c.getValue().getPorcentajeDescuento())));
        tabla.getSelectionModel().selectedItemProperty().addListener((obs, anterior, nueva) -> {
            if (nueva != null) {
                tipoSeleccionado = nueva.getTipoCliente();
                lblTipo.setText(tipoSeleccionado.toString());
                txtPorcentaje.setText(String.valueOf(nueva.getPorcentajeDescuento()));
            }
        });
        recargar();
    }

    @Override
    public void recargar() {
        tabla.getItems().clear();
        for (TipoCliente tipo : TipoCliente.values()) {
            tabla.getItems().add(Contexto.getCinema().getTarifa(tipo));
        }
        tabla.refresh();
    }

    /** Cambia el porcentaje de descuento del tipo de cliente seleccionado. */
    @FXML
    private void modificar() {
        if (tipoSeleccionado == null) {
            Alertas.error("Selecciona un tipo de cliente de la tabla");
            return;
        }
        try {
            double porcentaje = Double.parseDouble(txtPorcentaje.getText().trim());
            Contexto.getCinema().modificarTarifa(tipoSeleccionado, porcentaje);
            recargar();
            Alertas.informacion("Tarifa de " + tipoSeleccionado + " actualizada");
        } catch (NumberFormatException e) {
            Alertas.error("Ingresa un valor numérico válido");
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
        }
    }
}
