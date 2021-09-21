module messeption.ui {
    requires messeption.core;
    requires javafx.controls;
    requires javafx.fxml;

    opens ui to javafx.graphics, javafx.fxml;

}
