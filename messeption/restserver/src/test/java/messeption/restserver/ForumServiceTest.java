package messeption.restserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.User;
import messeption.core.UserHandler;
import messeption.json.JsonReadWrite;
import messeption.restapi.ForumBoardService;


public class ForumServiceTest extends JerseyTest {

  protected boolean shouldLog() {
    return true;
  }

  private String getCustomResponseStatus(Response resp){
    //return gson.fromJson(resp.readEntity(String.class), String.class).split(";")[0];
    return (resp.readEntity(String.class)).split(";")[0];
  }

  @Override
  protected ResourceConfig configure() {
    final ForumConfig config = new ForumConfig();
    if (shouldLog()) {
      enable(TestProperties.LOG_TRAFFIC);
      enable(TestProperties.DUMP_ENTITY);
      config.property(LoggingFeature.LOGGING_FEATURE_LOGGER_LEVEL_SERVER, "WARNING");
    }
    return config;
  }

  private Gson gson;
  private ForumBoard board;
  private String postId;
  private JsonReadWrite boardReadWrite;
  private UserHandler userHandler;
  private JsonReadWrite userReadWrite;


  @BeforeEach
  @Override
  public void setUp() throws Exception {
    super.setUp();
    gson = new GsonBuilder().create();
    boardReadWrite = new JsonReadWrite(ForumConfig.class.getResource("Board.JSON"));
    board = boardReadWrite.fileReadForumBoard();  //ensures proper board state compared to server before each test
    postId = board.getPosts().get(0).getId();
    
    userReadWrite = new JsonReadWrite(ForumConfig.class.getResource("Users.JSON"));
    userHandler = userReadWrite.fileReadUserHandler();
  }

  // properly resets serverside board state after each test.
  @AfterEach
  @Override
  public void tearDown() throws Exception {
    System.out.print("Teardown: ");
    Entity payload = Entity.json(board);
    Response postResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/set")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(payload);
    if (postResponse.getStatus() != 200) {
      throw new IllegalStateException(postResponse.getEntity().toString());
    }
    super.tearDown();
  }


  @Test
  public void testGetBoard() {
    Response getResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .get();
    assertEquals(200, getResponse.getStatus());
    try { 
      ForumBoard getBoard = gson.fromJson(getResponse.readEntity(String.class), ForumBoard.class);
      assertTrue(board.equals(getBoard));
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSetBoard() {
    ForumBoard setBoard = new ForumBoard();
    setBoard.newPost("Beep", "Boop");
    Entity payload = Entity.json(setBoard);
    Response putResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/set")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(payload);
    assertEquals("200", getCustomResponseStatus(putResponse));
  }

  @Test
  public void testAddPost() {
    ForumPost post = new ForumPost("Big title", "smol text");
    String id = post.getId();
    Entity payload = Entity.json(post);
    Response postResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/posts/addPost")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .post(payload);
    assertEquals("200", getCustomResponseStatus(postResponse));
  }

  @Test
  public void testAddComment() {
    PostComment comment = new PostComment("I like cheeze");
    Entity payload = Entity.json(comment);
    Response postResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/comments/addComment/")
        .path(postId)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .post(payload);
    assertEquals("200", getCustomResponseStatus(postResponse));
  }

  @Test
  public void testLikePost() {
    User user = new User("Madddd", "Lad123");
    Entity payload = Entity.json(user);
    Response putResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/posts/likePost/")
        .path(postId)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(payload);
    assertEquals("200", getCustomResponseStatus(putResponse));
  }

  @Test
  public void testDislikePost() {
    User user = new User("Madddd", "Lad123");
    Entity sent = Entity.json(user);
    Response putResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/posts/dislikePost/")
        .path(postId)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(sent);
    assertEquals("200", getCustomResponseStatus(putResponse));
  }

  @Test
  public void testLikeComment() {
    User user = new User("Madddd", "Lad123");
    Entity payload = Entity.json(user);
    String commentId = board.getPost(postId).getComments().get(0).getId();
    Response putResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/comments/likeComment/")
        .path(postId)
        .path(commentId)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(payload);
    assertEquals("200", getCustomResponseStatus(putResponse));
  }

  @Test
  public void testDislikeComment() {
    User user = new User("Madddd", "Lad123");
    Entity payload = Entity.json(user);
    String commentId = board.getPost(postId).getComments().get(0).getId();
    Response putResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/comments/dislikeComment/")
        .path(postId)
        .path(commentId)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(payload);
    assertEquals("200", getCustomResponseStatus(putResponse));
  }

  @Test
  public void testGetUsers() {
    Response getResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path(ForumBoardService.USER_RESOURCE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .get();
    assertEquals(200, getResponse.getStatus());
    try { 
      UserHandler loadedHandler = gson.fromJson(getResponse.readEntity(String.class), UserHandler.class);
      assertEquals(userHandler, loadedHandler);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testAddUsers() {
    User newUser = new User("Genral Kenobi", "HelloThere1");
    userHandler.addUser(newUser);
    Entity payload = Entity.json(newUser);
    Response postResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path(ForumBoardService.USER_RESOURCE_PATH)
        .path("/addUser")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .post(payload);
    assertEquals("200", getCustomResponseStatus(postResponse));

    Response getResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path(ForumBoardService.USER_RESOURCE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .get();
    assertEquals(200, getResponse.getStatus());
    try { 
      UserHandler loadedHandler = gson.fromJson(getResponse.readEntity(String.class), UserHandler.class);
      assertEquals(userHandler, loadedHandler);
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
