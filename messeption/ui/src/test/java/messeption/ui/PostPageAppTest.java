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
import org.junit.jupiter.api.Test;
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
public class PostPageAppTest extends ApplicationTest {

  private BoardAccessDirect boardAccess = new BoardAccessDirect();

  public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
  public static final String POST_PAGE_PATH = "PostPage.fxml";

  private Scene frontPageScene;
  private Scene postPageScene;

  private FrontPageController frontPageController;
  private PostPageController postPageController;

  private static ForumBoard boardBackup;
  private List<User> users;
  private String topPostId;
  

  @Override
  public void start(Stage primaryStage) throws Exception {

    boardAccess.setActiveUser(new User("ADMIN", "POWERS"));
    boardBackup = this.boardAccess.readBoard();

    topPostId = "7";

    users = new ArrayList<>();
    users.add(new User("tim", "Tom"));
    users.add(new User("aim", "Tom"));
    users.add(new User("bim", "Tom"));
    users.add(new User("cim", "Tom"));
    users.add(new User("dim", "Tom"));
    users.add(new User("eim", "Tom"));
    users.add(new User("Kim", "Tom"));
    users.add(new User("Wim", "Tom"));
    users.add(new User("Tommelim", "Tom"));
    users.add(new User("linn", "Tom"));
    users.add(new User("kinn", "Tom"));
    users.add(new User("plim", "Tom"));

    FXMLLoader frontPageLoader = new FXMLLoader(getClass().getResource(FRONT_PAGE_PATH));
    FXMLLoader postPageLoader = new FXMLLoader(getClass().getResource(POST_PAGE_PATH));

    frontPageScene = new Scene(frontPageLoader.load());
    postPageScene = new Scene(postPageLoader.load());

    frontPageController = frontPageLoader.getController();
    postPageController = postPageLoader.getController();

    frontPageController.setPostCommentsScene(postPageScene);
    frontPageController.setPostPageController(postPageController);

    frontPageController.setBoardAccess(boardAccess);
    postPageController.setBoardAccess(boardAccess);

    
    String id = boardAccess.getPosts().get(boardAccess.getPosts().size() - 1).getId();
    postPageController.drawComments(id);

    primaryStage.setScene(postPageScene);
    primaryStage.setTitle("Messeption");
    primaryStage.setResizable(false);
    primaryStage.show();

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
  @DisplayName("Test likes and dislikes post from postpage")
  public void testClickLikeFromPost(int n) throws Exception {
    int likes = boardAccess.getPost(topPostId).getLikes();
    int dislikes = boardAccess.getPost(topPostId).getDislikes();
    for (int index = 0; index < n; index++) {
      boardAccess.setActiveUser(users.get(index));
      click("Like");
      boardAccess.setActiveUser(users.get(index + n));
      click("Dislike");
    }
    assertEquals(likes + n, boardAccess.getPost(topPostId).getLikes(), "Like button did not increase likes from post page");
    assertEquals(dislikes + n, boardAccess.getPost(topPostId).getDislikes(), "Like button did not increase likes from post page");
  }

  private static Stream<Arguments> testClickLikeFromPost() {
    return Stream.of(Arguments.of(2));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test likes and dislikes on comments")
  public void testClickLikeComment(int n) {
    int likes = boardAccess.getPost(topPostId).getComments().get(0).getLikes();
    int dislikes = boardAccess.getPost(topPostId).getComments().get(0).getDislikes();
    for (int index = 0; index < n; index++) {
      boardAccess.setActiveUser(users.get(index));
      clickOn("#likeCommentButton");
      boardAccess.setActiveUser(users.get(index + n));
      clickOn("#dislikeCommentButton");
    }
    assertEquals(likes + n, boardAccess.getPost(topPostId).getComments().get(0).getLikes(),
        "Like button did not increase likes on comment");
    assertEquals(dislikes + n, boardAccess.getPost(topPostId).getComments().get(0).getDislikes(),
        "Dislike button did not increase dislikes on comment");
  }

  private static Stream<Arguments> testClickLikeComment() {
    return Stream.of(Arguments.of(2));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test creating comments")
  public void testComments(String text) {
    clickOn("#newCommentTextArea").write(text);
    click("Comment");

    List<PostComment> comments = boardAccess.getPost(topPostId).getComments();
    String commentText = comments.get(comments.size() - 1).getText();
    assertEquals(text, commentText, "New comment was not saved");
  }

  private static Stream<Arguments> testComments() {
    return Stream.of(Arguments.of("CHEEESE!!"));
  }

  @Test
  @DisplayName("Test deleting a comment")
  public void testDeleteComment() {
    boardAccess.setActiveUser(new User("Kenobi", "Hei123"));
    List<ForumPost> posts = boardAccess.getPosts();
    ForumPost post = posts.get(posts.size() - 1);
    PostComment comment = post.getComments().get(0);
    String text = comment.getText();
    click("Go back");
    click("Go to thread");
    click("Delete");
    click("Yes");
    posts = boardAccess.getPosts();
    post = posts.get(posts.size() - 1);
    comment = post.getComments().get(0);
    assertNotEquals(comment.getText(), text, "Comment was not successfully deleted");
  }

  

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test creating comments invalid")
  public void testCommentsInvalid(String text) {
    clickOn("#newCommentTextArea").write(text);
    click("Comment");

    List<PostComment> comments = boardAccess.getPost(topPostId).getComments();
    String commentText = comments.get(comments.size() - 1).getText();
    assertNotEquals(text, commentText, "New comment was saved when it shouldn't have been");
  }

  private static Stream<Arguments> testCommentsInvalid() {
    return Stream.of(Arguments.of("!"));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test creating comments anonymously")
  public void testCommentsAnonymous(String text) {
    clickOn("#anonymousAuthorCheckBox");
    clickOn("#newCommentTextArea").write(text);
    click("Comment");

    List<PostComment> comments = boardAccess.getPost(topPostId).getComments();
    PostComment comment = comments.get(comments.size() - 1);
    assertEquals(text, comment.getText(), "New comment was not saved");
    assertEquals(boardAccess.getActiveUser(), comment.getAuthor(), "Wrong author of comment");
    assertTrue(comment.isAnonymous(), "New comment was not posted anonymously");
  }

  private static Stream<Arguments> testCommentsAnonymous() {
    return Stream.of(Arguments.of("Anonymous"));
  }

  @AfterEach
  public void revertBoard() throws Exception {
    boardAccess.setBoard(boardBackup);
  }

}
