package messeption.ui;

import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlls and starts the applicatuin UI. Also controlls scene switches.
 */
public class MesseptionApp extends Application {

  public static final String LOGIN_PAGE_PATH = "LoginPage.fxml";
  public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
  public static final String CREATE_POST_PAGE_PATH = "CreatePostPage.fxml";
  public static final String POST_PAGE_PATH = "PostPage.fxml";

  private Scene loginPageScene;
  private Scene frontPageScene;
  private Scene createPostPageScene;
  private Scene postPageScene;

  private LoginPageController loginPageController;
  private FrontPageController frontPageController;
  private CreatePostPageController createPostPageController;
  private PostPageController postPageController;

  @Override
  public void start(Stage primaryStage) throws Exception {

    FXMLLoader loginPageLoader = new FXMLLoader(getClass().getResource(LOGIN_PAGE_PATH));
    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    FXMLLoader createPostPageLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));
    FXMLLoader postPageLoader = new FXMLLoader(getClass().getResource(POST_PAGE_PATH));

    loginPageScene = new Scene(loginPageLoader.load());
    frontPageScene = new Scene(frontPageLoader.load());
    createPostPageScene = new Scene(createPostPageLoader.load());
    postPageScene = new Scene(postPageLoader.load());

    loginPageController = loginPageLoader.getController();
    frontPageController = frontPageLoader.getController();
    createPostPageController = createPostPageLoader.getController();
    postPageController = postPageLoader.getController();

    frontPageController.setPostPageController(postPageController);
    frontPageController.setPostCommentsScene(postPageScene);
    loginPageController.setFrontPageScene(frontPageScene);

    primaryStage.setScene(loginPageScene);
    primaryStage.setTitle("Messeption");
    primaryStage.setResizable(false);
    primaryStage.show();

    frontPageController.createPostButton.setOnAction(event -> {
      primaryStage.setScene(createPostPageScene);
      createPostPageController.setBoard(frontPageController.getBoard());
    });

    createPostPageController.cancelButton.setOnAction(event -> {
      createPostPageController.reloadPage();
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (IOException e) {
        UiUtils.exceptionAlert(e).show();
      }
    });

    postPageController.cancelButton.setOnAction(event -> {
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (IOException e) {
        UiUtils.exceptionAlert(e).show();
      }
    });
  }

  public static void main(String[] args) {
    launch(MesseptionApp.class, args);
  }
}
