package messeption.ui;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messeption.core.UserHandler;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


public class LoginTestIT extends ApplicationTest {

  private BoardAccessRemote boardAccess;
  private static BoardAccessRemote boardAccessInitial;
  private static UserHandler usersBackup;
  

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

  private LoginPageController loginPageController;
  private FrontPageController frontPageController;

  private static URI baseUri;

  //private static ForumBoard boardBackup;

  @Override
  public void start(Stage primaryStage) throws Exception {

    FXMLLoader loginPageLoader = new FXMLLoader(getClass().getResource(LOGIN_PAGE_PATH));
    loginPageScene = new Scene(loginPageLoader.load());
    loginPageController = loginPageLoader.getController();
    loginPageController.setPrimaryStage(primaryStage);


    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    frontPageScene = new Scene(frontPageLoader.load());
    frontPageController = frontPageLoader.getController();

    loginPageController.setFrontPageController(frontPageController);
    loginPageController.setFrontPageScene(frontPageScene);


    primaryStage.setScene(loginPageScene);
    primaryStage.setTitle("Messeption Integration Test");
    primaryStage.setResizable(false);
    primaryStage.show();
  }

  @BeforeAll
  public static void initializeTests() throws Exception{
    String port = System.getProperty("messeption.port"); 
    assertNotNull(port, "No messeption.port system property set");
    baseUri = new URI("http://localhost:" + port + "/board");
    System.out.println("Base BoardAccesRemote URI: " + baseUri);
    boardAccessInitial = new BoardAccessRemote(baseUri);
    usersBackup = boardAccessInitial.readUsers();
  }

  @BeforeEach
  public void setUp() throws Exception {
    this.boardAccess = new BoardAccessRemote(baseUri);
    loginPageController.setBoardAccess(this.boardAccess);
    frontPageController.setBoardAccess(this.boardAccess);
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

  @Test
  @DisplayName("Test login with an invalid user")
  public void testInvalidLogin() throws Exception {
    String username = "NoUser";
    String password = "NoPass123";
    clickOn("#loginUserTextField").write(username);
    clickOn("#loginPasswordField").write(password);
    clickOn("#loginButton");
    clickOn("OK");
    assertEquals(boardAccess.getActiveUser(), null, "Login was successful when it should have failed");
  }

  @Test
  @DisplayName("Test create new valid user")
  public void testNewUserValid() throws Exception {
    String username = "ValidUserIT";
    String password = "Valid123";
    clickOn("#signUpUserTextField").write(username);
    clickOn("#signUpPasswordField").write(password);
    clickOn("#signUpPasswordFieldCheck").write(password);
    clickOn("#signupButton");
    clickOn("Okay");
    
    clickOn("#loginUserTextField").write(username);
    clickOn("#loginPasswordField").write(password);
    clickOn("#loginButton");
    clickOn("Okay");
    assertEquals(boardAccess.getActiveUser().getUsername(), username, "Did not get to front page with correct active user");
  }

  @Test
  @DisplayName("Test create new invalid valid user")
  public void testNewUserInvalid() throws Exception {

    String username = "BadUser";
    String password = "bad";
    clickOn("#signUpUserTextField").write(username);
    clickOn("#signUpPasswordField").write(password);
    clickOn("#signUpPasswordFieldCheck").write(password);
    clickOn("#signupButton");
    clickOn("OK");
    
    clickOn("#loginUserTextField").write(username);
    clickOn("#loginPasswordField").write(password);
    clickOn("#loginButton");
    clickOn("OK");
    assertEquals(boardAccess.getActiveUser(), null, "Login was successful when it should have failed");
  }

  @AfterEach
  public void revertActiveUser() {
    boardAccess.setActiveUser(null);
  }

  @AfterAll
  public static void revertUsers() throws Exception {
    try {
      boardAccessInitial.setUserHandler(usersBackup);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
