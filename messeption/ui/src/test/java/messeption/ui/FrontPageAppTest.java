package messeption.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

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
import messeption.core.User;

/**
 * TestFX App test
 */
public class FrontPageAppTest extends ApplicationTest {

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
  private List<User> users;
  private String topPostId;
  

  @Override
  public void start(Stage primaryStage) throws Exception {

    boardAccess.setActiveUser(new User("ADMIN", "POWERS"));
    boardBackup = this.boardAccess.readBoard();

    topPostId = boardAccess.getPosts().get(boardAccess.getPosts().size()-1).getId();

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
    frontPageController.setPrimaryStage(primaryStage);
    createPostPageController = createPostPageLoader.getController();
    postPageController = postPageLoader.getController();

    frontPageController.setPostPageScene(postPageScene);
    frontPageController.setPostPageController(postPageController);

    frontPageController.setBoardAccess(boardAccess);
    createPostPageController.setBoardAccess(boardAccess);
    postPageController.setBoardAccess(boardAccess);

    primaryStage.setScene(frontPageScene);
    primaryStage.setTitle("Messeption");
    primaryStage.setResizable(false);
    primaryStage.show();

    
    createPostPageController.setPrimaryStage(primaryStage);
    postPageController.setPrimaryStage(primaryStage);

    frontPageController.setCreatePostPageScene(createPostPageScene);

    createPostPageController.setFrontPageScene(frontPageScene);
    createPostPageController.setFrontPageController(frontPageController);
    
    postPageController.setFrontPageController(frontPageController);
    postPageController.setFrontPageScene(frontPageScene);

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


  @Test
  @DisplayName("Test deleting a post")
  public void testDeletePost() throws Exception {
    String title = boardAccess.getPost(topPostId).getTitle();
    boardAccess.setActiveUser(new User ("Kenobi", "Hei123"));
    clickOn("Go to thread");
    clickOn("Go back");
    clickOn("#deleteButton");
    click("Yes");
    List<ForumPost> posts = boardAccess.getPosts();
    ForumPost post2 = posts.get(posts.size() - 1);
    assertNotEquals(title, post2.getTitle(), "Post was not successfully deleted");
  }


  @ParameterizedTest
  @MethodSource
  @DisplayName("Test likes and dislikes")
  public void testClickLike(int n) {
    int likes = boardAccess.getPost(topPostId).getLikes();
    int dislikes = boardAccess.getPost(topPostId).getDislikes();
    for (int index = 0; index < n; index++) {
      boardAccess.setActiveUser(users.get(index));
      click("Like");
      boardAccess.setActiveUser(users.get(index + n));
      click("Dislike");
    }
    assertEquals(likes + n, boardAccess.getPost(topPostId).getLikes(), "Like button did not increase likes");
    assertEquals(dislikes + n, boardAccess.getPost(topPostId).getDislikes(), "Dislike button did not increase dislikes");
  }

  private static Stream<Arguments> testClickLike() {
    return Stream.of(Arguments.of(4));
  }

  @Test
  @DisplayName("Navigating to and from the post page")
  public void testNavigatePostPage() {
    clickOn("Go to thread");
    clickOn("Go back");
    assertDoesNotThrow(() -> {clickOn("#sortMenuButton");});
  }

  @Test
  @DisplayName("Navigating to and from the create post page")
  public void testNavigateCreatePostPage() {
    clickOn("Create Post");
    clickOn("Cancel");
    assertDoesNotThrow(() -> {clickOn("#sortMenuButton");});
  }

  @Test
  @DisplayName("Navigating to and from the post page")
  public void testSortPosts() {
    clickOn("#sortMenuButton");
    clickOn("Time");
    clickOn("#sortMenuButton");
    clickOn("Title");
    clickOn("#sortMenuButton");
    clickOn("Author");
    clickOn("#sortMenuButton");
    clickOn("Text");
    clickOn("#sortMenuButton");
    clickOn("Comments");
    clickOn("#sortMenuButton");
  }


  @AfterEach
  public void revertBoard() throws Exception {
    boardAccess.setBoard(boardBackup);
  }

}
