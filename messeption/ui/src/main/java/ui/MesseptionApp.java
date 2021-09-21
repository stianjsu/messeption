package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MesseptionApp extends Application {

    public static final String FRONTPAGE_PATH = "Frontpage.fxml";
    private Scene frontpageScene;
    private FrontpageController frontpageController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader frontpageLoader = new FXMLLoader(getClass().getResource(FRONTPAGE_PATH));

        frontpageScene = new Scene(frontpageLoader.load());

        // frontpageController = frontpageLoader.getController();

        primaryStage.setScene(frontpageScene);
        primaryStage.setTitle("Messeption");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(MesseptionApp.class, args);
    }
}