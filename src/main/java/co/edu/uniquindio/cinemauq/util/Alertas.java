package co.edu.uniquindio.cinemauq.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;

/**
 * Ventanas de mensaje reutilizables para toda la interfaz.
 */
public final class Alertas {

    private Alertas() {
    }

    /**
     * Muestra un mensaje de error.
     *
     * @param mensaje texto que se muestra al usuario
     */
    public static void error(String mensaje) {
        mostrar(Alert.AlertType.ERROR, "Error", mensaje);
    }

    /**
     * Muestra un mensaje informativo.
     *
     * @param mensaje texto que se muestra al usuario
     */
    public static void informacion(String mensaje) {
        mostrar(Alert.AlertType.INFORMATION, "Información", mensaje);
    }

    /**
     * Pide confirmación al usuario.
     *
     * @param mensaje pregunta que se muestra
     * @return true si el usuario aceptó
     */
    public static boolean confirmar(String mensaje) {
        ButtonType aceptar = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelar = new ButtonType("Cancelar", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION, mensaje, aceptar, cancelar);
        alerta.setTitle("Confirmación");
        alerta.setHeaderText(null);
        return alerta.showAndWait().orElse(cancelar) == aceptar;
    }

    private static void mostrar(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alerta = new Alert(tipo, mensaje, ButtonType.OK);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.showAndWait();
    }
}
