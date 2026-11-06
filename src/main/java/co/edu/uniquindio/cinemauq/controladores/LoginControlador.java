package co.edu.uniquindio.cinemauq.controladores;

import co.edu.uniquindio.cinemauq.CinemaUQApp;
import co.edu.uniquindio.cinemauq.excepciones.CinemaException;
import co.edu.uniquindio.cinemauq.modelo.Usuario;
import co.edu.uniquindio.cinemauq.util.Alertas;
import co.edu.uniquindio.cinemauq.util.Contexto;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador de la pantalla de inicio de sesión.
 */
public class LoginControlador {

    @FXML
    private TextField txtUsuario;

    @FXML
    private PasswordField txtContrasena;

    /** Valida las credenciales y, si son correctas, abre la pantalla principal. */
    @FXML
    private void ingresar() {
        try {
            Usuario usuario = Contexto.getCinema()
                    .iniciarSesion(txtUsuario.getText(), txtContrasena.getText());
            Contexto.setUsuario(usuario);
            CinemaUQApp.mostrarPrincipal((Stage) txtUsuario.getScene().getWindow());
        } catch (CinemaException e) {
            Alertas.error(e.getMessage());
            txtContrasena.clear();
        } catch (IOException e) {
            Contexto.cerrarSesion();
            Alertas.error("No se pudo abrir la pantalla principal: " + e.getMessage());
        }
    }
}
