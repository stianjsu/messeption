package ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MesseptionApp extends Application {

    public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
    private Scene frontPageScene;
    private FrontPageController frontPageController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));

        frontPageScene = new Scene(frontPageLoader.load());

        // frontpageController = frontpageLoader.getController();

        primaryStage.setScene(frontPageScene);
        primaryStage.setTitle("Messeption");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(MesseptionApp.class, args);
    }
}
