package messeption.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import messeption.core.UserHandler;

/**
 * TestFX App test
 */
public class LoginPageAppTest extends ApplicationTest {

  private static BoardAccessDirect boardAccess = new BoardAccessDirect();

  public static final String LOGIN_PAGE_PATH = "LoginPage.fxml";
  public static final String FRONT_PAGE_PATH = "FrontPage.fxml";

  private Scene loginPageScene;
  private Scene frontPageScene;

  private LoginPageController loginPageController;
  private FrontPageController frontPageController;

  private static UserHandler usersBackup;

  @Override
  public void start(Stage primaryStage) throws Exception {

    FXMLLoader loginPageLoader = new FXMLLoader(getClass().getResource(LOGIN_PAGE_PATH));
    loginPageScene = new Scene(loginPageLoader.load());
    loginPageController = loginPageLoader.getController();
    loginPageController.setPrimaryStage(primaryStage);
    loginPageController.setBoardAccess(boardAccess);


    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    frontPageScene = new Scene(frontPageLoader.load());
    frontPageController = frontPageLoader.getController();
    frontPageController.setPrimaryStage(primaryStage);
    frontPageController.setBoardAccess(boardAccess);
    
    loginPageController.setFrontPageController(frontPageController);
    loginPageController.setFrontPageScene(frontPageScene);

    primaryStage.setScene(loginPageScene);
    primaryStage.setTitle("Messeption");
    primaryStage.setResizable(false);
    primaryStage.show();

  }

  @BeforeAll
  public static void setUpBackups() throws Exception {
    BoardAccessDirect boardAccessInitial = new BoardAccessDirect();
    usersBackup = boardAccessInitial.readUsers();
    boardAccess.setActiveUser(null);
  }

  private void click(String... labels) {
    for (var label : labels) {
      clickOn(LabeledMatchers.hasText(label));
    }
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test login with a valid user")
  public void testValidLogin(String username, String password) throws Exception {
    clickOn("#loginUserTextField").write(username);
    clickOn("#loginPasswordField").write(password);
    clickOn("#loginButton");
    click("Okay");
    assertEquals(boardAccess.getActiveUser().getUsername(), username, "Did not get to front page with correct active user");
  }

  private static Stream<Arguments> testValidLogin() {
    return Stream.of(Arguments.of("Jonah", "Hei123"));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test login with an invalid user")
  public void testInvalidLogin(String username, String password) throws Exception {
    clickOn("#loginUserTextField").write(username);
    clickOn("#loginPasswordField").write(password);
    clickOn("#loginButton");
    click("OK");
    assertEquals(null, boardAccess.getActiveUser(), "Login was successful when it should have failed");
  }

  private static Stream<Arguments> testInvalidLogin() {
    return Stream.of(Arguments.of("NoUser", "no"), Arguments.of("Jonah", "no"));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test create new valid user")
  public void testNewUserValid(String username, String password) throws Exception {
    clickOn("#signUpUserTextField").write(username);
    clickOn("#signUpPasswordField").write(password);
    clickOn("#signUpPasswordFieldCheck").write(password);
    clickOn("#signupButton");
    click("Okay");
    testValidLogin(username, password);
  }

  private static Stream<Arguments> testNewUserValid() {
    return Stream.of(Arguments.of("ValidUserName", "Valid123"));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test create new invalid user")
  public void testNewUserInvalid(String username, String password) throws Exception {
    clickOn("#signUpUserTextField").write(username);
    clickOn("#signUpPasswordField").write(password);
    clickOn("#signUpPasswordFieldCheck").write(password);
    clickOn("#signupButton");
    click("OK");
    testInvalidLogin(username, password);
  }

  private static Stream<Arguments> testNewUserInvalid() {
    return Stream.of(Arguments.of("NoUser", "no"), Arguments.of("No", "No1234"));
  }

  @AfterEach
  public void revertActiveUser() {
    boardAccess.setActiveUser(null);
  }

  @AfterAll
  public static void revertBoard() throws Exception {
    boardAccess.setUserHandler(usersBackup);
    boardAccess.setActiveUser(null);
  }

}
