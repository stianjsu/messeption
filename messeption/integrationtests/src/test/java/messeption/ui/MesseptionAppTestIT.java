package messeption.ui;

import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import messeption.core.ForumBoard;
import messeption.core.User;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;


public class MesseptionAppTestIT extends ApplicationTest {

  private BoardAccessRemote boardAccess;

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
  private String topPostId;
  


  @Override
  public void start(Stage primaryStage) throws Exception {


    this.boardAccess = new BoardAccessRemote(baseUri);
    this.boardAccess.setActiveUser(new User("ADMIN", "POWERS"));
    //boardBackup = boardAccess.readBoard();

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

    frontPageController.setBoardAccess(this.boardAccess);
    createPostPageController.setBoardAccess(this.boardAccess);
    postPageController.setBoardAccess(this.boardAccess);

    primaryStage.setScene(frontPageScene);
    primaryStage.setTitle("Messeption App Test");
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

  @BeforeAll
  public static void initializeTests() throws Exception{
    String port = System.getProperty("jetty.port"); 
    assertNotNull(port, "No messeption.port system property set");
    baseUri = new URI("http://localhost:" + port + "/board");
    System.out.println("Base BoardAccesRemote URI: " + baseUri);
    BoardAccessRemote boardAccessIntialState = new BoardAccessRemote(baseUri);
    boardBackup = boardAccessIntialState.readBoard();

  }

  @Test
  public void testControllerInitial() {
    assertNotNull(this.frontPageController);
  }

}
