module messeption.ui {
    requires messeption.core;
    requires messeption.json;
    requires javafx.controls;
    requires javafx.fxml;
    requires com.google.gson;
    requires java.net.http;

    opens messeption.ui to javafx.graphics, javafx.fxml;

}
