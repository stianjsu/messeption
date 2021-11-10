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
public class MesseptionAppTest extends ApplicationTest {

  private BoardAccessDirect boardAccess = new BoardAccessDirect();

  public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
  public static final String CREATE_POST_PAGE_PATH = "CreatePostPage.fxml";
  public static final String POST_PAGE_PATH = "PostPage.fxml";

  private Scene frontPageScene;
  private Scene createPostPageScene;
  private Scene postPageScene;

  private FrontPageController frontPageController;
  private CreatePostPageController createPostPageController;
  private PostPageController postPageController;

  private static ForumBoard boardBackup;
  List<User> users;
  

  @Override
  public void start(Stage primaryStage) throws Exception {

    boardAccess.setActiveUser(new User("ADMIN", "POWERS"));
    boardBackup = this.boardAccess.readBoard();

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
    FXMLLoader createPostPageLoader = new FXMLLoader(getClass().getResource(CREATE_POST_PAGE_PATH));
    FXMLLoader postPageLoader = new FXMLLoader(getClass().getResource(POST_PAGE_PATH));

    frontPageScene = new Scene(frontPageLoader.load());
    createPostPageScene = new Scene(createPostPageLoader.load());
    postPageScene = new Scene(postPageLoader.load());

    frontPageController = frontPageLoader.getController();
    createPostPageController = createPostPageLoader.getController();
    postPageController = postPageLoader.getController();

    frontPageController.setPostCommentsScene(postPageScene);
    frontPageController.setPostPageController(postPageController);

    frontPageController.setBoardAccess(boardAccess);
    createPostPageController.setBoardAccess(boardAccess);
    postPageController.setBoardAccess(boardAccess);

    primaryStage.setScene(frontPageScene);
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
  @DisplayName("Test create a valid post")
  public void testCreatePostValid(String title, String text) throws Exception {
    click("Create Post");
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
    click("Create Post");
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
    click("Create Post");
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
    click("Create Post");
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

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test likes and dislikes")
  public void testClickLike(int n) {
    int likes = boardAccess.getPosts().get(0).getLikes();
    int dislikes = boardAccess.getPosts().get(0).getDislikes();
    for (int index = 0; index < n; index++) {
      boardAccess.setActiveUser(users.get(index));
      click("Like");
      boardAccess.setActiveUser(users.get(index + n));
      click("Dislike");
    }
    assertEquals(likes + n, boardAccess.getPosts().get(0).getLikes(), "Like button did not increase likes");
    assertEquals(dislikes + n, boardAccess.getPosts().get(0).getDislikes(), "Dislike button did not increase dislikes");
  }

  private static Stream<Arguments> testClickLike() {
    return Stream.of(Arguments.of(4));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test likes and dislikes post from postpage")
  public void testClickLikeFromPost(int n) throws Exception {
    click("Go to thread");
    int likes = boardAccess.getPosts().get(0).getLikes();
    int dislikes = boardAccess.getPosts().get(0).getDislikes();
    for (int index = 0; index < n; index++) {
      boardAccess.setActiveUser(users.get(index));
      click("Like");
      boardAccess.setActiveUser(users.get(index + n));
      click("Dislike");
    }
    assertEquals(likes + n, boardAccess.getPosts().get(0).getLikes(), "Like button did not increase likes from post page");
    assertEquals(dislikes + n, boardAccess.getPosts().get(0).getDislikes(), "Like button did not increase likes from post page");
  }

  private static Stream<Arguments> testClickLikeFromPost() {
    return Stream.of(Arguments.of(2));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test likes and dislikes on comments")
  public void testClickLikeComment(int n) {
    click("Go to thread");
    int likes = boardAccess.getPosts().get(0).getComments().get(0).getLikes();
    int dislikes = boardAccess.getPosts().get(0).getComments().get(0).getDislikes();
    for (int index = 0; index < n; index++) {
      boardAccess.setActiveUser(users.get(index));
      clickOn("#likeCommentButton");
      boardAccess.setActiveUser(users.get(index + n));
      clickOn("#dislikeCommentButton");
    }
    assertEquals(likes + n, boardAccess.getPosts().get(0).getComments().get(0).getLikes(),
        "Like button did not increase likes on comment");
    assertEquals(dislikes + n, boardAccess.getPosts().get(0).getComments().get(0).getDislikes(),
        "Dislike button did not increase dislikes on comment");
  }

  private static Stream<Arguments> testClickLikeComment() {
    return Stream.of(Arguments.of(2));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test creating comments")
  public void testComments(String text) {
    click("Go to thread");
    clickOn("#newCommentTextArea").write(text);
    click("Comment");

    List<PostComment> comments = boardAccess.getPosts().get(0).getComments();
    String commentText = comments.get(comments.size() - 1).getText();
    assertEquals(text, commentText, "New comment was not saved");
  }

  private static Stream<Arguments> testComments() {
    return Stream.of(Arguments.of("CHEEESE!!"));
  }

  @ParameterizedTest
  @MethodSource
  @DisplayName("Test creating comments invalid")
  public void testCommentsInvalid(String text) {
    click("Go to thread");
    clickOn("#newCommentTextArea").write(text);
    click("Comment");

    List<PostComment> comments = boardAccess.getPosts().get(0).getComments();
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
    click("Go to thread");
    clickOn("#anonymousAuthorCheckBox");
    clickOn("#newCommentTextArea").write(text);
    click("Comment");

    List<PostComment> comments = boardAccess.getPosts().get(0).getComments();
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
