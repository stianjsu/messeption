package messeption.ui;

import java.net.URI;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Controlls and starts the application UI. Also controls scene switches.
 */
public class MesseptionRemoteApp extends Application {

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

  @Override
  public void start(Stage primaryStage) throws Exception {

    boardAccess = new BoardAccessRemote(URI.create("http://localhost:8999/board"));

    FXMLLoader loginPageLoader = new FXMLLoader(getClass().getResource(LOGIN_PAGE_PATH));
    loginPageScene = new Scene(loginPageLoader.load());
    loginPageController = loginPageLoader.getController();
    loginPageController.setBoardAccess(boardAccess);
    UiUtils.setNavBarButtons(loginPageController.menuQuit, loginPageController.menuLogOut,
        loginPageController.menuAbout, primaryStage, loginPageScene);

    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    frontPageScene = new Scene(frontPageLoader.load());
    frontPageController = frontPageLoader.getController();
    frontPageController.setBoardAccess(boardAccess);
    UiUtils.setNavBarButtons(frontPageController.menuQuit, frontPageController.menuLogOut,
        frontPageController.menuAbout, primaryStage, loginPageScene);
    UiUtils.setLogOutButton(frontPageController.logOutButton, primaryStage, loginPageScene);

    FXMLLoader createPostPageLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));
    createPostPageScene = new Scene(createPostPageLoader.load());
    createPostPageController = createPostPageLoader.getController();
    createPostPageController.setBoardAccess(boardAccess);
    UiUtils.setNavBarButtons(createPostPageController.menuQuit, createPostPageController.menuLogOut,
        createPostPageController.menuAbout, primaryStage, loginPageScene);
    UiUtils.setLogOutButton(createPostPageController.logOutButton, primaryStage, loginPageScene);

    FXMLLoader postPageLoader = new FXMLLoader(getClass().getResource(POST_PAGE_PATH));
    postPageScene = new Scene(postPageLoader.load());
    postPageController = postPageLoader.getController();
    postPageController.setBoardAccess(boardAccess);
    UiUtils.setNavBarButtons(postPageController.menuQuit, postPageController.menuLogOut,
        postPageController.menuAbout, primaryStage, loginPageScene);
    UiUtils.setLogOutButton(postPageController.logOutButton, primaryStage, loginPageScene);
    
    frontPageController.setPostCommentsScene(postPageScene);
    frontPageController.setPostPageController(postPageController);
    loginPageController.setFrontPageController(frontPageController);
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
        frontPageController.sortMenuButton.setText("Time");
      } catch (Exception e) {
        UiUtils.exceptionAlert(e).show();
      }
    });

    postPageController.cancelButton.setOnAction(event -> {
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
        frontPageController.sortMenuButton.setText("Time");
      } catch (Exception e) {
        UiUtils.exceptionAlert(e).show();
      }
    });
    
  }

  public static void main(String[] args) {
    launch(MesseptionRemoteApp.class, args);
  }
}
