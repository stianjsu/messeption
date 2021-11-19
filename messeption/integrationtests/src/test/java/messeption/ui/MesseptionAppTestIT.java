package messeption.ui;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.matcher.control.LabeledMatchers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


public class MesseptionAppTestIT extends ApplicationTest {

  private static BoardAccessRemote boardAccess;

  @BeforeAll
  public static void setupHeadless() {
    MesseptionApp.supportHeadless();
  }

  public static final String FRONT_PAGE_PATH = "FrontPage.fxml";
  public static final String CREATE_POST_PAGE_PATH = "CreatePostPage.fxml";
  public static final String POST_PAGE_PATH = "PostPage.fxml";

  private Scene frontPageScene;
  private Scene createPostPageScene;
  private Scene postPageScene;

  private FrontPageController frontPageController;
  private CreatePostPageController createPostPageController;
  private PostPageController postPageController;

  private static URI baseUri;
  private static ForumBoard boardBackup;
  private List<User> users;
  private static final User ADMIN_USER = new User("ADMIN", "POWERS");
  


  @Override
  public void start(Stage primaryStage) throws Exception {

    boardAccess.setActiveUser(ADMIN_USER);

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
    createPostPageController.setPrimaryStage(primaryStage);
    postPageController = postPageLoader.getController();
    postPageController.setPrimaryStage(primaryStage);

    frontPageController.setPostPageScene(postPageScene);
    frontPageController.setPostPageController(postPageController);

    frontPageController.setBoardAccess(boardAccess);
    createPostPageController.setBoardAccess(boardAccess);
    postPageController.setBoardAccess(boardAccess);

    primaryStage.setScene(frontPageScene);
    primaryStage.setTitle("Messeption App Test");
    primaryStage.setResizable(false);
    primaryStage.show();

    frontPageController.setCreatePostPageScene(createPostPageScene);
    
    createPostPageController.setFrontPageScene(frontPageScene);
    createPostPageController.setFrontPageController(frontPageController);

    postPageController.setFrontPageScene(frontPageScene);
    postPageController.setFrontPageController(frontPageController);
  }

  private String getTopPostId() {
    return boardAccess.getPosts().get(boardAccess.getPosts().size()-1).getId();
  }

  @BeforeAll
  public static void initializeTests() throws Exception{
    String port = System.getProperty("jetty.port"); 
    assertNotNull(port, "No messeption.port system property set");
    baseUri = new URI("http://localhost:" + port + "/board");
    System.out.println("Base BoardAccesRemote URI: " + baseUri);
    BoardAccessRemote boardAccessIntialState = new BoardAccessRemote(baseUri);
    boardBackup = boardAccessIntialState.readBoard();
    boardAccess = new BoardAccessRemote(baseUri);
  }

  @Test
  public void testControllerInitial() {
    assertNotNull(this.frontPageController);
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
  @DisplayName("Test likes and dislikes")
  public void testClickLike() {
    int n = 4;
    int likes = boardAccess.getPost(getTopPostId()).getLikes();
    int dislikes = boardAccess.getPost(getTopPostId()).getDislikes();
    for (int index = 0; index < n; index++) {
      boardAccess.setActiveUser(users.get(index));
      clickOn("Like");
      boardAccess.setActiveUser(users.get(index + n));
      clickOn("Dislike");
    }
    assertEquals(likes + n, boardAccess.getPost(getTopPostId()).getLikes(), "Like button did not increase likes");
    assertEquals(dislikes + n, boardAccess.getPost(getTopPostId()).getDislikes(), "Dislike button did not increase dislikes");
  }

  @Test
  @DisplayName("Test creating and deleting a post")
  public void testCreateAndDeletePost() throws Exception {
    String title = "Title";
    String text = "Text";
    click("Create Post");
    clickOn("#postTitleField").write(title);
    clickOn("#postTextArea").write(text);
    click("Publish", "Quit To Front Page");
    checkNewPost(title, text);
    
    clickOn("#deleteButton");
    clickOn("Yes");
    ForumPost post = boardAccess.getPost(getTopPostId());
    assertNotEquals(title, post.getTitle(), "Post was not successfully deleted");
    
  }

  @Test
  @DisplayName("Test creating a comment")
  public void testCreateComment() {
    String text = "Comment";
    click("Go to thread");
    clickOn("#newCommentTextArea").write(text);
    click("Comment");

    List<PostComment> comments = boardAccess.getPost(getTopPostId()).getComments();
    String commentText = comments.get(comments.size() - 1).getText();
    assertEquals(text, commentText, "New comment was not saved");
  }

  @Test
  @DisplayName("Test likes and dislikes on comments")
  public void testClickLikeComment() {
    int n = 4;
    click("Go to thread");
    int likes = boardAccess.getPost(getTopPostId()).getComments().get(0).getLikes();
    int dislikes = boardAccess.getPost(getTopPostId()).getComments().get(0).getDislikes();
    for (int index = 0; index < n; index++) {
      boardAccess.setActiveUser(users.get(index));
      clickOn("#likeCommentButton");
      boardAccess.setActiveUser(users.get(index + n));
      clickOn("#dislikeCommentButton");
    }
    assertEquals(likes + n, boardAccess.getPost(getTopPostId()).getComments().get(0).getLikes(),
        "Like button did not increase likes on comment");
    assertEquals(dislikes + n, boardAccess.getPost(getTopPostId()).getComments().get(0).getDislikes(),
        "Dislike button did not increase dislikes on comment");
  }

  @AfterAll
  public static void resetBoard() throws Exception {
    boardAccess.setActiveUser(ADMIN_USER);
    boardAccess.setBoard(boardBackup);
  }

}
