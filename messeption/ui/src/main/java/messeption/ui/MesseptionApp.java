package messeption.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;

public class MesseptionApp extends Application {

    public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
    public static final String CREATE_POST_PAGE_PATH = "CreatePost.fxml";

    private Scene frontPageScene;
    private Scene createPostScene;

    private FrontPageController frontPageController;
    private CreatePostController createPostController;

    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
        FXMLLoader createPostLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));

        frontPageScene = new Scene(frontPageLoader.load());
        createPostScene = new Scene(createPostLoader.load());

        frontPageController = frontPageLoader.getController();
        createPostController = createPostLoader.getController();

        primaryStage.setScene(frontPageScene);
        primaryStage.setTitle("Messeption");
        primaryStage.show();

        frontPageController.createPostButton.setOnAction(event -> {
            primaryStage.setScene(createPostScene);
            createPostController.setBoard(frontPageController.getBoard());
        });

        createPostController.cancelButton.setOnAction(event -> {
            primaryStage.setScene(frontPageScene);
            try { frontPageController.drawPosts(); }
            catch (IOException e) { frontPageController.exceptionAlert(e).show();}
            
        });

    }

    public static void main(String[] args) {
        launch(MesseptionApp.class, args);
    }
}
