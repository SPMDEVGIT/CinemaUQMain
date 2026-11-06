package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.CinemaUQApp;
import co.edu.uniquindio.cinemauq.enumeraciones.Rol;
import co.edu.uniquindio.cinemauq.modelo.Usuario;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla principal: encabezado con el usuario y pestañas según su rol.
 */
public class PrincipalControlador {

    /** Pestañas del operador: título y archivo FXML de cada una. */
    private static final String[][] PESTANAS_OPERADOR = {
        {"Vender", "Vender.fxml"},
        {"Cancelar venta", "CancelarVenta.fxml"},
        {"Películas", "Peliculas.fxml"},
        {"Funciones", "Funciones.fxml"},
        {"Disponibilidad", "Disponibilidad.fxml"},
        {"Clientes", "Clientes.fxml"}
    };

    /** Pestañas que solo ve el administrador. */
    private static final String[][] PESTANAS_ADMINISTRADOR = {
        {"Gestión de películas", "GestionPeliculas.fxml"},
        {"Gestión de salas", "GestionSalas.fxml"},
        {"Gestión de funciones", "GestionFunciones.fxml"},
        {"Tarifas", "Tarifas.fxml"},
        {"Reportes", "Reportes.fxml"}
    };

    @FXML
    private Label lblUsuario;

    @FXML
    private TabPane tabPane;

    @FXML
    private void initialize() {
        Usuario usuario = Contexto.getUsuario();
        lblUsuario.setText(usuario.getNombre() + " · " + usuario.getRol());
        for (String[] definicion : PESTANAS_OPERADOR) {
            agregarPestana(definicion[0], definicion[1]);
        }
        if (usuario.getRol() == Rol.ADMINISTRADOR) {
            for (String[] definicion : PESTANAS_ADMINISTRADOR) {
                agregarPestana(definicion[0], definicion[1]);
            }
        }
    }

    /** Cierra la sesión y vuelve al login. */
    @FXML
    private void cerrarSesion() {
        Stage escenario = (Stage) tabPane.getScene().getWindow();
        Contexto.cerrarSesion();
        try {
            CinemaUQApp.mostrarLogin(escenario);
        } catch (IOException e) {
            Alertas.error("No se pudo abrir la pantalla de inicio de sesión: " + e.getMessage());
        }
    }

    /** Carga el FXML de una pestaña y hace que recargue sus datos al seleccionarse. */
    private void agregarPestana(String titulo, String vista) {
        try {
            FXMLLoader cargador = new FXMLLoader(CinemaUQApp.class.getResource("vistas/" + vista));
            Node contenido = cargador.load();
            Tab pestana = new Tab(titulo, contenido);
            pestana.setClosable(false);
            Object controlador = cargador.getController();
            if (controlador instanceof Recargable) {
                Recargable recargable = (Recargable) controlador;
                pestana.setOnSelectionChanged(evento -> {
                    if (pestana.isSelected()) {
                        recargable.recargar();
                    }
                });
            }
            tabPane.getTabs().add(pestana);
        } catch (IOException e) {
            Alertas.error("No se pudo cargar la pestaña " + titulo + ": " + e.getMessage());
        }
    }
}
