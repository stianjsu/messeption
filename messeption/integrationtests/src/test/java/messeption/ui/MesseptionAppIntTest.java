package messeption.ui;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


public class MesseptionAppIntTest extends ApplicationTest {


  private BoardAccessRemote boardAccess;

  @BeforeAll
  public static void setupHeadless() {
    MesseptionApp.supportHeadless();
  }


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

  //private static ForumBoard boardBackup;

  @Override
  public void start(Stage primaryStage) throws Exception {

    FXMLLoader loginPageLoader = new FXMLLoader(getClass().getResource(LOGIN_PAGE_PATH));
    loginPageScene = new Scene(loginPageLoader.load());
    loginPageController = loginPageLoader.getController();



    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    frontPageScene = new Scene(frontPageLoader.load());
    frontPageController = frontPageLoader.getController();



    FXMLLoader createPostPageLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));
    createPostPageScene = new Scene(createPostPageLoader.load());
    createPostPageController = createPostPageLoader.getController();


    FXMLLoader postPageLoader = new FXMLLoader(getClass().getResource(POST_PAGE_PATH));
    postPageScene = new Scene(postPageLoader.load());
    postPageController = postPageLoader.getController();


    
    frontPageController.setPostCommentsScene(postPageScene);
    frontPageController.setPostPageController(postPageController);
    loginPageController.setFrontPageController(frontPageController);
    loginPageController.setFrontPageScene(frontPageScene);
    

    primaryStage.setScene(loginPageScene);
    primaryStage.setTitle("Messeption");
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  @BeforeEach
  public void setUp() throws Exception {
    String port = "8999"; //TODO remove hardcoded port
    assertNotNull(port, "No messeption.port system property set");
    URI baseUri = new URI("http://localhost:" + port + "/board");
    System.out.println("Base BoardAccesRemote URI: " + baseUri);
    this.boardAccess = new BoardAccessRemote(baseUri);
    loginPageController.setBoardAccess(this.boardAccess);
    frontPageController.setBoardAccess(this.boardAccess);
    createPostPageController.setBoardAccess(this.boardAccess);
    postPageController.setBoardAccess(this.boardAccess);
    //boardBackup = this.boardAccess.readBoard(); //todo implement board rewind
  }

  @Test
  public void testControllerInitial() {
    assertNotNull(this.loginPageController);
  }


  @Test
  @DisplayName("Test login with a valid user")
  public void testValidLogin() throws Exception {
    String username = "Jonah";
    String password = "Hei123";
    clickOn("#loginUserTextField").write(username);
    clickOn("#loginPasswordField").write(password);
    clickOn("#loginButton");
    clickOn("Okay");
    assertEquals(boardAccess.getActiveUser().getUsername(), username, "Did not get to front page with correct active user");
  }

}


