package messeption.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlls and starts the applicatuin UI. Also controlls scene switches.
 */
public class MesseptionApp extends Application {

  private BoardAccessInterface boardAccess = new BoardAccessDirect();

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
    loginPageScene = new Scene(loginPageLoader.load());
    loginPageController = loginPageLoader.getController();
    loginPageController.setBoardAccess(boardAccess);


    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    frontPageScene = new Scene(frontPageLoader.load());
    frontPageController = frontPageLoader.getController();
    frontPageController.setBoardAccess(boardAccess);


    FXMLLoader createPostPageLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));
    createPostPageScene = new Scene(createPostPageLoader.load());
    createPostPageController = createPostPageLoader.getController();
    createPostPageController.setBoardAccess(boardAccess);

    FXMLLoader postPageLoader = new FXMLLoader(getClass().getResource(POST_PAGE_PATH));
    postPageScene = new Scene(postPageLoader.load());
    postPageController = postPageLoader.getController();
    postPageController.setBoardAccess(boardAccess);

    
    frontPageController.setPostCommentsScene(postPageScene);
    frontPageController.setPostPageController(postPageController);
    loginPageController.setFrontPageScene(frontPageScene);
    

    primaryStage.setScene(loginPageScene);
    primaryStage.setTitle("Messeption");
    primaryStage.setResizable(false);
    primaryStage.show();

    frontPageController.createPostButton.setOnAction(event -> {
      primaryStage.setScene(createPostPageScene);
    });

    createPostPageController.cancelButton.setOnAction(event -> {
      createPostPageController.reloadPage();
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (Exception e) {
        UiUtils.exceptionAlert(e).show();
      }
    });

    postPageController.cancelButton.setOnAction(event -> {
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (Exception e) {
        UiUtils.exceptionAlert(e).show();
      }
    });
  }

  public static void main(String[] args) {
    launch(MesseptionApp.class, args);
  }
}
