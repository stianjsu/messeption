package messeption.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
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
import messeption.core.User;

/**
 * TestFX App test
 */
public class CreatePostPageAppTest extends ApplicationTest {

  private BoardAccessDirect boardAccess = new BoardAccessDirect();

  public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
  public static final String CREATE_POST_PAGE_PATH = "CreatePostPage.fxml";

  private Scene frontPageScene;
  private Scene createPostPageScene;

  private FrontPageController frontPageController;
  private CreatePostPageController createPostPageController;

  private static ForumBoard boardBackup;
  private List<User> users;
  

  @Override
  public void start(Stage primaryStage) throws Exception {

    boardAccess.setActiveUser(new User("ADMIN", "POWERS"));
    boardBackup = this.boardAccess.readBoard();
    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    FXMLLoader createPostPageLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));;

    frontPageScene = new Scene(frontPageLoader.load());
    createPostPageScene = new Scene(createPostPageLoader.load());

    frontPageController = frontPageLoader.getController();
    createPostPageController = createPostPageLoader.getController();

    frontPageController.setBoardAccess(boardAccess);
    createPostPageController.setBoardAccess(boardAccess);

    primaryStage.setScene(createPostPageScene);
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
  }


  private void click(String... labels) {
    for (var label : labels) {
      clickOn(LabeledMatchers.hasText(label));
    }
  }

  public void checkNewPost(String title, String text) throws Exception {
    List<ForumPost> posts = boardAccess.getPosts();
    ForumPost post = posts.get(posts.size() - 1);
    assertEquals(post.getTitle(), title, "Title did not match expected value");
    assertEquals(post.getText(), text, "Text did not match expected value");
  }

  public void checkNewPostAuthor(User author) throws Exception {
    List<ForumPost> posts = boardAccess.getPosts();
    ForumPost post = posts.get(posts.size() - 1);
    assertEquals(post.getAuthor(), author, "Author did not match expected value");
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test create a valid post")
  public void testCreatePostValid(String title, String text) throws Exception {
    clickOn("#postTitleField").write(title);
    clickOn("#postTextArea").write(text);
    click("Publish", "Quit To Front Page");
    checkNewPost(title, text);
  }

  private static Stream<Arguments> testCreatePostValid() {
    return Stream.of(Arguments.of("title", "text"), Arguments.of("there", "kenobi"));
  }


  @ParameterizedTest
  @MethodSource
  @DisplayName("Test create a valid post anonymously")
  public void testCreatePostValidAnonomous(String title, String text) throws Exception {
    clickOn("#anonymousAuthorCheckBox");
    clickOn("#postTitleField").write(title);
    clickOn("#postTextArea").write(text);
    click("Publish", "Quit To Front Page");
    checkNewPost(title, text);
    checkNewPostAuthor(boardAccess.getActiveUser());
  }

  private static Stream<Arguments> testCreatePostValidAnonomous() {
    return Stream.of(Arguments.of("title", "text"));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test create a post and then another")
  public void testCreateAnotherPost(String title1, String text1, String title2, String text2) throws Exception {
    clickOn("#postTitleField").write(title1);
    clickOn("#postTextArea").write(text1);
    click("Publish", "Create another post");
    checkNewPost(title1, text1);
    clickOn("#postTitleField").write(title2);
    clickOn("#postTextArea").write(text2);
    click("Publish", "Quit To Front Page");
    checkNewPost(title2, text2);
  }

  private static Stream<Arguments> testCreateAnotherPost() {
    return Stream.of(Arguments.of("Title", "texttext", "Hello", "General"));
  }

  public void checkNewPostFail(String title, String text) throws Exception {
    List<ForumPost> posts = boardAccess.getPosts();
    ForumPost post = posts.get(posts.size() - 1);
    assertFalse(post.getTitle().equals(title) && post.getText().equals(text),
        "A post got accepted but should have failed");
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test create an invalid post")
  public void testCreatePostInvalid(String title, String text) throws Exception {
    clickOn("#postTitleField").write(title);
    clickOn("#postTextArea").write(text);
    click("Publish");
    click("OK");
    click("Cancel");
    checkNewPostFail(title, text);
  }

  private static Stream<Arguments> testCreatePostInvalid() {
    return Stream.of(Arguments.of("short text", ""), Arguments.of("", "short title"));
  }

  @AfterEach
  public void revertBoard() throws Exception {
    boardAccess.setBoard(boardBackup);
  }

}
