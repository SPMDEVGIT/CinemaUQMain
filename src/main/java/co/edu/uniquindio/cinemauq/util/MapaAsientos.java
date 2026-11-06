package co.edu.uniquindio.cinemauq.util;

import co.edu.uniquindio.cinemauq.enumeraciones.EstadoAsiento;
import co.edu.uniquindio.cinemauq.modelo.Asiento;
import co.edu.uniquindio.cinemauq.modelo.Funcion;
import java.util.function.Consumer;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * Mapa de asientos de una función, dibujado como una cuadrícula de botones.
 * Verde: disponible, rojo: ocupado, azul: seleccionado.
 * En modo selección el usuario elige un asiento disponible; en modo solo lectura solo se mira.
 */
public class MapaAsientos extends GridPane {

    private final boolean seleccionable;
    private Button botonSeleccionado;
    private String codigoSeleccionado;
    private Consumer<String> alCambiarSeleccion;

    /**
     * Crea un mapa vacío.
     *
     * @param seleccionable true para permitir elegir un asiento; false para solo lectura
     */
    public MapaAsientos(boolean seleccionable) {
        this.seleccionable = seleccionable;
        setHgap(6);
        setVgap(6);
        setAlignment(Pos.CENTER);
    }

    /**
     * Dibuja los asientos de una función. Si es nula, deja el mapa vacío.
     *
     * @param funcion función cuyos asientos se muestran
     */
    public void mostrar(Funcion funcion) {
        getChildren().clear();
        botonSeleccionado = null;
        codigoSeleccionado = null;
        if (funcion != null) {
            int columnas = 1;
            for (Asiento asiento : funcion.getAsientos()) {
                columnas = Math.max(columnas, asiento.getNumero());
            }
            Label pantalla = new Label("PANTALLA");
            pantalla.getStyleClass().add("pantalla");
            pantalla.setMaxWidth(Double.MAX_VALUE);
            pantalla.setAlignment(Pos.CENTER);
            add(pantalla, 0, 0, columnas, 1);
            for (Asiento asiento : funcion.getAsientos()) {
                add(crearBoton(asiento), asiento.getNumero() - 1, asiento.getFila() - 'A' + 1);
            }
        }
        notificarCambio();
    }

    /** @return el código del asiento seleccionado, o null si no hay ninguno */
    public String getCodigoSeleccionado() {
        return codigoSeleccionado;
    }

    /**
     * Define qué hacer cuando cambia el asiento seleccionado (recibe el código, o null).
     *
     * @param accion acción a ejecutar
     */
    public void setAlCambiarSeleccion(Consumer<String> accion) {
        this.alCambiarSeleccion = accion;
    }

    /**
     * Crea una leyenda con los tres colores del mapa.
     *
     * @return fila con las etiquetas Disponible, Ocupado y Seleccionado
     */
    public static HBox crearLeyenda() {
        HBox leyenda = new HBox(12,
                crearEtiquetaLeyenda("Disponible", "asiento-disponible"),
                crearEtiquetaLeyenda("Ocupado", "asiento-ocupado"),
                crearEtiquetaLeyenda("Seleccionado", "asiento-seleccionado"));
        leyenda.setAlignment(Pos.CENTER);
        return leyenda;
    }

    private static Label crearEtiquetaLeyenda(String texto, String claseColor) {
        Label etiqueta = new Label(texto);
        etiqueta.getStyleClass().addAll("leyenda", claseColor);
        return etiqueta;
    }

    private Button crearBoton(Asiento asiento) {
        Button boton = new Button(asiento.getCodigo());
        boton.getStyleClass().add("asiento");
        if (asiento.getEstado() == EstadoAsiento.OCUPADO) {
            boton.getStyleClass().add("asiento-ocupado");
            boton.setDisable(true);
        } else {
            boton.getStyleClass().add("asiento-disponible");
            if (seleccionable) {
                boton.setOnAction(e -> alternarSeleccion(boton, asiento.getCodigo()));
            }
        }
        if (!seleccionable) {
            boton.setMouseTransparent(true);
            boton.setFocusTraversable(false);
        }
        return boton;
    }

    private void alternarSeleccion(Button boton, String codigo) {
        boolean eraElMismo = boton == botonSeleccionado;
        if (botonSeleccionado != null) {
            marcar(botonSeleccionado, false);
            botonSeleccionado = null;
            codigoSeleccionado = null;
        }
        if (!eraElMismo) {
            marcar(boton, true);
            botonSeleccionado = boton;
            codigoSeleccionado = codigo;
        }
        notificarCambio();
    }

    private void marcar(Button boton, boolean seleccionado) {
        boton.getStyleClass().removeAll("asiento-disponible", "asiento-seleccionado");
        boton.getStyleClass().add(seleccionado ? "asiento-seleccionado" : "asiento-disponible");
    }

    private void notificarCambio() {
        if (alCambiarSeleccion != null) {
            alCambiarSeleccion.accept(codigoSeleccionado);
        }
    }
}
