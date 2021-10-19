module messeption.ui {
    requires messeption.core;
    requires messeption.json;
    requires javafx.controls;
    requires javafx.fxml;

    opens messeption.ui to javafx.graphics, javafx.fxml;

}
