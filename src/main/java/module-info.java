module co.edu.uniquindio.cinemauq {
    requires javafx.controls;
    requires javafx.fxml;

    exports co.edu.uniquindio.cinemauq;
    exports co.edu.uniquindio.cinemauq.enumeraciones;
    exports co.edu.uniquindio.cinemauq.excepciones;
    exports co.edu.uniquindio.cinemauq.modelo;
    exports co.edu.uniquindio.cinemauq.util;

    opens co.edu.uniquindio.cinemauq.controladores to javafx.fxml;
}
