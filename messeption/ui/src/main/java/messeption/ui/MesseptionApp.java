package messeption.ui;

import java.net.URI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlls and starts the application UI. Also controls scene switches.
 */
public class MesseptionApp extends Application {

  private BoardAccessInterface boardAccess;

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

  /**
   * Method for supporting headless class for integration tests.
   */
  public static void supportHeadless() {
    if (Boolean.getBoolean("headless")) {
      System.setProperty("testfx.robot", "glass");
      System.setProperty("testfx.headless", "true");
      System.setProperty("prism.order", "sw");
      System.setProperty("prism.text", "t2k");
      System.setProperty("java.awt.headless", "true");
    }
  }

  @Override
  public void start(Stage primaryStage) throws Exception {

    if (!Boolean.parseBoolean(System.getProperty("run.local"))) {
      try {
        boardAccess = new BoardAccessRemote(URI.create("http://localhost:8080/board"));
      } catch (Exception e) {
        UiUtils.popupAlert("Could not connect to server from http://localhost:8080/").showAndWait();
        e.printStackTrace();
        Platform.exit();
      }
    } else {
      boardAccess = new BoardAccessDirect();
    }

    FXMLLoader loginPageLoader = new FXMLLoader(getClass().getResource(LOGIN_PAGE_PATH));
    loginPageScene = new Scene(loginPageLoader.load());
    loginPageController = loginPageLoader.getController();
    loginPageController.setBoardAccess(boardAccess);
    loginPageController.setPrimaryStage(primaryStage);
    loginPageController.setLoginPageScene(loginPageScene);

    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    frontPageScene = new Scene(frontPageLoader.load());
    frontPageController = frontPageLoader.getController();
    frontPageController.setBoardAccess(boardAccess);
    frontPageController.setPrimaryStage(primaryStage);
    frontPageController.setLoginPageScene(loginPageScene);

    FXMLLoader createPostPageLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));
    createPostPageScene = new Scene(createPostPageLoader.load());
    createPostPageController = createPostPageLoader.getController();
    createPostPageController.setBoardAccess(boardAccess);
    createPostPageController.setPrimaryStage(primaryStage);
    createPostPageController.setLoginPageScene(loginPageScene);

    FXMLLoader postPageLoader = new FXMLLoader(getClass().getResource(POST_PAGE_PATH));
    postPageScene = new Scene(postPageLoader.load());
    postPageController = postPageLoader.getController();
    postPageController.setBoardAccess(boardAccess);
    postPageController.setPrimaryStage(primaryStage);
    postPageController.setLoginPageScene(loginPageScene);
  
    primaryStage.setScene(loginPageScene);
    primaryStage.setTitle("Messeption");
    primaryStage.setResizable(false);
    primaryStage.show();

    loginPageController.setFrontPageController(frontPageController);
    loginPageController.setFrontPageScene(frontPageScene);

    frontPageController.setCreatePostPageScene(createPostPageScene);
    frontPageController.setPostPageScene(postPageScene);
    frontPageController.setPostPageController(postPageController);

    createPostPageController.setFrontPageScene(frontPageScene);
    createPostPageController.setFrontPageController(frontPageController);
    
    postPageController.setFrontPageScene(frontPageScene);
    postPageController.setFrontPageController(frontPageController);
  }

  /**
   * Checks whether app is set to run locally. Connect remotely by default

   * @param args args[0] defined in pom as run.local property's value
   */
  public static void main(String[] args) {
    if (args.length > 0 && args[0].equals("true")) {
      System.setProperty("run.local", "true");
    }

    launch(MesseptionApp.class, args);
  }
}
