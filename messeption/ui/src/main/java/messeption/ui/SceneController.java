package messeption.ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * Abstract controller class for easier scene changes between controllers.
 */
public class SceneController {

  protected Stage primaryStage;
  protected Scene frontPageScene;
  protected Scene loginPageScene;
  protected Scene postPageScene;
  protected Scene createPostPageScene;

  protected FrontPageController frontPageController;
  protected BoardAccessInterface boardAccess;
  
  @FXML
  protected Button logOutButton;
  @FXML
  protected MenuItem menuQuit;
  @FXML
  protected MenuItem menuLogOut;
  @FXML
  protected MenuItem menuAbout;

  public FrontPageController getFrontPageController() {
    return this.frontPageController;
  }

  public void setFrontPageController(FrontPageController frontPageController) {
    this.frontPageController = frontPageController;
  }

  public BoardAccessInterface getBoardAccess() {
    return this.boardAccess;
  }

  public void setBoardAccess(BoardAccessInterface boardAccess) throws Exception {
    this.boardAccess = boardAccess;
  }

  public Stage getPrimaryStage() {
    return this.primaryStage;
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public Scene getFrontPageScene() {
    return this.frontPageScene;
  }

  public void setFrontPageScene(Scene frontPageScene) {
    this.frontPageScene = frontPageScene;
  }

  public Scene getLoginPageScene() {
    return this.loginPageScene;
  }

  public void setLoginPageScene(Scene loginPageScene) {
    this.loginPageScene = loginPageScene;
  }

  public Scene getPostPageScene() {
    return this.postPageScene;
  }

  public void setPostPageScene(Scene postPageScene) {
    this.postPageScene = postPageScene;
  }

  public Scene getCreatePostPageScene() {
    return this.createPostPageScene;
  }

  public void setCreatePostPageScene(Scene createPostPageScene) {
    this.createPostPageScene = createPostPageScene;
  }
}
