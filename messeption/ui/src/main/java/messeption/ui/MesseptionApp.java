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

  public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
  public static final String CREATE_POST_PAGE_PATH = "CreatePost.fxml";
  public static final String POST_COMMENTS_PAGE_PATH = "PostComments.fxml";

  private Scene frontPageScene;
  private Scene createPostScene;
  private Scene postCommentsScene;

  private FrontPageController frontPageController;
  private CreatePostController createPostController;
  private PostCommentsController postCommentsController;

  @Override
  public void start(Stage primaryStage) throws Exception {

    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    FXMLLoader createPostLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));
    FXMLLoader postCommentsLoader = new FXMLLoader(getClass().getResource(POST_COMMENTS_PAGE_PATH));

    frontPageScene = new Scene(frontPageLoader.load());
    createPostScene = new Scene(createPostLoader.load());
    postCommentsScene = new Scene(postCommentsLoader.load());

    frontPageController = frontPageLoader.getController();
    createPostController = createPostLoader.getController();
    postCommentsController = postCommentsLoader.getController();

    frontPageController.setPostControllerController(postCommentsController);
    frontPageController.setPostCommentsScene(postCommentsScene);

    primaryStage.setScene(frontPageScene);
    primaryStage.setTitle("Messeption");
    primaryStage.setResizable(false);
    primaryStage.show();

    frontPageController.createPostButton.setOnAction(event -> {
      primaryStage.setScene(createPostScene);
      createPostController.setBoard(frontPageController.getBoard());
    });

    createPostController.cancelButton.setOnAction(event -> {
      createPostController.reloadPage();
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (IOException e) {
        frontPageController.exceptionAlert(e).show();
      }
    });

    postCommentsController.cancelButton.setOnAction(event -> {
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (IOException e) {
        frontPageController.exceptionAlert(e).show();
      }
    });
  }

  public static void main(String[] args) {
    launch(MesseptionApp.class, args);
  }
}
