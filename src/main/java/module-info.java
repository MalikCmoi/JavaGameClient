module com.tcpgame.clientgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;


    opens com.tcpgame.clientgame to javafx.fxml;
    exports com.tcpgame.clientgame;
    opens com.tcpgame.clientgame.module to com.google.gson;
}

