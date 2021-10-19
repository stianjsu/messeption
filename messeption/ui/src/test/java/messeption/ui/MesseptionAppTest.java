package messeption.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.List;
import java.util.stream.Stream;
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
import messeption.json.JsonReadWrite;

/**
 * TestFX App test
 */
public class MesseptionAppTest extends ApplicationTest {

  public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
  public static final String CREATE_POST_PAGE_PATH = "CreatePostPage.fxml";
  public static final String POST_PAGE_PATH = "PostPage.fxml";

  private Scene frontPageScene;
  private Scene createPostPageScene;
  private Scene postPageScene;

  private FrontPageController frontPageController;
  private CreatePostPageController createPostPageController;
  private PostPageController postPageController;

  private ForumBoard board;
  private static ForumBoard boardBackup;

  @Override
  public void start(Stage primaryStage) throws Exception {

    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    FXMLLoader createPostPageLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));
    FXMLLoader postPageLoader = new FXMLLoader(getClass().getResource(POST_PAGE_PATH));

    frontPageScene = new Scene(frontPageLoader.load());
    createPostPageScene = new Scene(createPostPageLoader.load());
    postPageScene = new Scene(postPageLoader.load());

    frontPageController = frontPageLoader.getController();
    createPostPageController = createPostPageLoader.getController();
    postPageController = postPageLoader.getController();

    frontPageController.setPostPageController(postPageController);
    frontPageController.setPostCommentsScene(postPageScene);

    primaryStage.setScene(frontPageScene);
    primaryStage.setTitle("Messeption");
    primaryStage.setResizable(false);
    primaryStage.show();

    boardBackup = JsonReadWrite.fileRead();

    frontPageController.createPostButton.setOnAction(event -> {
      primaryStage.setScene(createPostPageScene);
      createPostPageController.setBoard(frontPageController.getBoard());
    });

    createPostPageController.cancelButton.setOnAction(event -> {
      createPostPageController.reloadPage();
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (IOException e) {
        frontPageController.exceptionAlert(e).show();
      }
    });

    postPageController.cancelButton.setOnAction(event -> {
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (IOException e) {
        frontPageController.exceptionAlert(e).show();
      }
    });
  }

  private void click(String... labels) {
    for (var label : labels) {
      clickOn(LabeledMatchers.hasText(label));
    }
  }

  public void checkNewPost(String title, String text) {
    ForumBoard board = frontPageController.getBoard();
    List<ForumPost> posts = board.getPosts();
    ForumPost post = posts.get(posts.size() - 1);
    assertEquals(post.getTitle(), title, "Title did not match expected value");
    assertEquals(post.getText(), text, "Text did not match expected value");
  }

  /*
   * @ParameterizedTest
   * 
   * @MethodSource
   * 
   * @DisplayName("Test create a valid post") public void
   * testCreatePostValid(String title, String text) { click("Create Post");
   * clickOn("Title of post").write(title);
   * clickOn("content of post").write(text); click("Publish",
   * "Quit To Front Page"); checkNewPost(title, text); }
   * 
   * private static Stream<Arguments> testCreatePostValid() { return
   * Stream.of(Arguments.of("title", "text"), Arguments.of("hello there",
   * "general kenobi")); }
   * 
   * @ParameterizedTest
   * 
   * @MethodSource
   * 
   * @DisplayName("Test create a post and then another") public void
   * testCreateAnotherPost(String title1, String text1, String title2, String
   * text2) { click("Create Post"); clickOn("Title of post").write(title1);
   * clickOn("content of post").write(text1); click("Publish",
   * "Create another post"); checkNewPost(title1, text1);
   * clickOn("Title of post").write(title2);
   * clickOn("content of post").write(text2); click("Publish",
   * "Quit To Front Page"); checkNewPost(title2, text2); }
   * 
   * private static Stream<Arguments> testCreateAnotherPost() { return Stream
   * .of(Arguments.of("Another title", "Another texttext", "Another hello there",
   * "Another general kenobi")); }
   * 
   * public void checkNewPostFail(String title, String text) { board =
   * frontPageController.getBoard(); List<ForumPost> posts = board.getPosts();
   * ForumPost post = posts.get(posts.size() - 1);
   * assertFalse(post.getTitle().equals(title) && post.getText().equals(text),
   * "A post got accepted but should have failed"); }
   * 
   * @ParameterizedTest
   * 
   * @MethodSource
   * 
   * @DisplayName("Test create an invalid post") public void
   * testCreatePostInvalid(String title, String text) { click("Create Post");
   * clickOn("Title of post").write(title);
   * clickOn("content of post").write(text); click("Publish", "Cancel");
   * checkNewPostFail(title, text); }
   * 
   * private static Stream<Arguments> testCreatePostInvalid() { return
   * Stream.of(Arguments.of("good title, short text", ""), Arguments.of("",
   * "short title, good text")); }
   * 
   * @ParameterizedTest
   * 
   * @MethodSource
   * 
   * @DisplayName("Test likes and dislikes") public void testClickLike(int n) {
   * int likes = frontPageController.getBoard().getPost(0).getLikes(); int
   * dislikes = frontPageController.getBoard().getPost(0).getDislikes(); for (int
   * index = 0; index < n; index++) { click("Like", "Dislike"); }
   * assertEquals(likes + n, frontPageController.getBoard().getPost(0).getLikes(),
   * "Like button did not increase likes"); assertEquals(dislikes + n,
   * frontPageController.getBoard().getPost(0).getDislikes(),
   * "Dislike button did not increase dislikes"); }
   * 
   * private static Stream<Arguments> testClickLike() { return
   * Stream.of(Arguments.of(2), Arguments.of(5)); }
   */
  @ParameterizedTest
  @MethodSource
  @DisplayName("Test creating comments")
  public void testComments(String text) {
    click("Go to thread");
    clickOn("Write a new comment here").write(text);
    click("Comment");

    List<PostComment> comments = frontPageController.getBoard().getPost(0).getComments();
    String commentText = comments.get(comments.size() - 1).getText();
    assertEquals(commentText, text, "New comment was not saved");
  }

  private static Stream<Arguments> testComments() {
    return Stream.of(Arguments.of("CHEEESE FOR EVERYONE!!!"), Arguments.of("Potet"));
  }

  @AfterEach
  public void revertBoard() {
    frontPageController.setBoard(boardBackup);
  }

}
