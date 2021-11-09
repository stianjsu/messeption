module messeption.ui {
    requires messeption.core;
    requires messeption.json;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;
    requires jakarta.ws.rs;

    opens messeption.ui to javafx.graphics, javafx.fxml;

}
