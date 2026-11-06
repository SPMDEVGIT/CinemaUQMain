package co.edu.uniquindio.cinemauq;

import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Cinema;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import co.edu.uniquindio.cinemauq.util.DatosPrueba;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase de arranque de la aplicación CinemaUQ. Crea el cinema, carga los datos de
 * prueba y muestra el login. También cambia entre las pantallas principales.
 */
public class CinemaUQApp extends Application {

    private static final double ANCHO_MINIMO = 1100;
    private static final double ALTO_MINIMO = 700;

    @Override
    public void start(Stage escenario) throws IOException {
        try {
            Cinema cinema = new Cinema("CinemaUQ", "Universidad del Quindío, Armenia");
            Contexto.setCinema(cinema);
            DatosPrueba.cargar(cinema);
        } catch (CinemaException e) {
            Alertas.error("No se pudieron cargar los datos de prueba: " + e.getMessage());
        }
        escenario.setTitle("CinemaUQ");
        escenario.setMinWidth(ANCHO_MINIMO);
        escenario.setMinHeight(ALTO_MINIMO);
        mostrarLogin(escenario);
        escenario.show();
    }

    /**
     * Muestra la pantalla de inicio de sesión.
     *
     * @param escenario ventana donde se muestra
     * @throws IOException si no se puede cargar la vista
     */
    public static void mostrarLogin(Stage escenario) throws IOException {
        mostrar(escenario, "vistas/Login.fxml");
    }

    /**
     * Muestra la pantalla principal.
     *
     * @param escenario ventana donde se muestra
     * @throws IOException si no se puede cargar la vista
     */
    public static void mostrarPrincipal(Stage escenario) throws IOException {
        mostrar(escenario, "vistas/Principal.fxml");
    }

    private static void mostrar(Stage escenario, String vista) throws IOException {
        FXMLLoader cargador = new FXMLLoader(CinemaUQApp.class.getResource(vista));
        Parent raiz = cargador.load();
        double ancho = ANCHO_MINIMO;
        double alto = ALTO_MINIMO;
        if (escenario.getScene() != null) {
            ancho = escenario.getScene().getWidth();
            alto = escenario.getScene().getHeight();
        }
        escenario.setScene(new Scene(raiz, ancho, alto));
    }

    public static void main(String[] args) {
        launch(args);
    }
}
