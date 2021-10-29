package messeption.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;

/**
 * TestFX App test
 */
public class LoginMesseptionAppTest extends ApplicationTest {

  private BoardAccessDirect boardAccess = new BoardAccessDirect();

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

  private static ForumBoard boardBackup;

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

    boardBackup = this.boardAccess.readBoard();

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
    assertEquals(boardAccess.getActiveUser(), null, "Login was successful when it should have failed");
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
  public void revertBoard() throws Exception {
    boardAccess.setBoard(boardBackup);
  }

}
