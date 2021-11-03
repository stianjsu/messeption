package messeption.restserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

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
import messeption.restapi.ForumBoardService;


public class ForumServiceTest extends JerseyTest {

  protected boolean shouldLog() {
    return true;
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

  private ForumBoard board = new ForumBoard();

  @BeforeEach
  @Override
  public void setUp() throws Exception {
    super.setUp();
    gson = new GsonBuilder().create();
    board.newPost("Test", "Post");
  }

/*
  @AfterEach
  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }
*/

  @Test
  public void testGetBoard() {
    Response getResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .get();
    assertEquals(200, getResponse.getStatus());
    try { 
      ForumBoard board = gson.fromJson(getResponse.readEntity(String.class), ForumBoard.class);
      assertEquals("Test", board.getPosts().get(0).getTitle());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }

  @Test
  public void testSetBoard() {
    board.newPost("Beep", "Boop");
    Entity e = Entity.json(board);
    Response postResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/set")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(e);
    assertEquals(200, postResponse.getStatus());
  }

  @Test
  public void testAddPost() {
    ForumPost post = new ForumPost("Big title", "smol text");
    Entity e = Entity.json(post);
    Response postResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/posts/addPost")
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(e);
    assertEquals(200, postResponse.getStatus());
  }

  @Test
  public void testAddComment() {
    PostComment comment = new PostComment("I like cheeze");
    Entity e = Entity.json(comment);
    String id = board.getPosts().get(0).getId();
    String[] idd = id.split(" ");
    Response postResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .path("/comments/addComment/")
        .path(idd[0] + "_" + idd[1])
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .put(e);
    assertEquals(200, postResponse.getStatus());
  }

}
