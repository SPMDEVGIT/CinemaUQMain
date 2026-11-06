package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.modelo.Funcion;
import co.edu.uniquindio.cinemauq.util.Contexto;
import co.edu.uniquindio.cinemauq.util.Formato;
import co.edu.uniquindio.cinemauq.util.MapaAsientos;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

/**
 * Pestaña "Disponibilidad": totales y mapa de solo lectura de una función.
 */
public class DisponibilidadControlador implements Recargable {

    @FXML
    private ComboBox<Funcion> cmbFunciones;

    @FXML
    private Label lblTotal;

    @FXML
    private Label lblDisponibles;

    @FXML
    private Label lblOcupados;

    @FXML
    private ScrollPane scrollMapa;

    private final MapaAsientos mapa = new MapaAsientos(false);

    @FXML
    private void initialize() {
        cmbFunciones.setConverter(Formato.convertidorFuncion());
        VBox contenedor = new VBox(12, MapaAsientos.crearLeyenda(), mapa);
        contenedor.setAlignment(Pos.TOP_CENTER);
        scrollMapa.setContent(contenedor);
        cmbFunciones.valueProperty().addListener((obs, anterior, nueva) -> mostrar());
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
        mostrar();
    }

    private void mostrar() {
        Funcion funcion = cmbFunciones.getValue();
        mapa.mostrar(funcion);
        if (funcion == null) {
            lblTotal.setText("Total: —");
            lblDisponibles.setText("Disponibles: —");
            lblOcupados.setText("Ocupados: —");
        } else {
            lblTotal.setText("Total: " + funcion.getAsientos().size());
            lblDisponibles.setText("Disponibles: " + funcion.contarDisponibles());
            lblOcupados.setText("Ocupados: " + funcion.contarOcupados());
        }
    }
}
