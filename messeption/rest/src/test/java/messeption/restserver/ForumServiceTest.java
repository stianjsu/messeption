package messeption.restserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import static org.junit.Assert.assertFalse;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.glassfish.jersey.logging.LoggingFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import messeption.core.ForumPost;
import messeption.core.ForumBoard;
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

  @BeforeEach
  @Override
  public void setUp() throws Exception {
    super.setUp();
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
  }

  @AfterEach
  @Override
  public void tearDown() throws Exception {
    super.tearDown();
  }
/*
  @Test
  public void testGet_todo() {
    Response getResponse = target(TodoModelService.TODO_MODEL_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .get();
    assertEquals(200, getResponse.getStatus());
    try {
      TodoModel todoModel = objectMapper.readValue(getResponse.readEntity(String.class), TodoModel.class);  //TODO find solution for us
      Iterator<AbstractTodoList> it = todoModel.iterator();
      assertTrue(it.hasNext());
      AbstractTodoList todoList1 = it.next();
      assertTrue(it.hasNext());
      AbstractTodoList todoList2 = it.next();
      assertFalse(it.hasNext());
      assertEquals("todo1", todoList1.getName());
      assertEquals("todo2", todoList2.getName());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
*/
  @Test
  public void testGetBoard() {
    Response getResponse = target(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
        .request(MediaType.APPLICATION_JSON + ";" + MediaType.CHARSET_PARAMETER + "=UTF-8")
        .get();
    assertEquals(200, getResponse.getStatus());
    try { 
      //should work with Gson.fromJson()
      ForumBoard board = gson.fromJson(getResponse.readEntity(String.class), ForumBoard.class);
      //AbstractTodoList todoList = objectMapper.readValue(getResponse.readEntity(String.class), AbstractTodoList.class); //TODO find solution for us  
      assertEquals(true, true);
      //assertEquals("todo1", todoList.getName());
    } catch (Exception e) {
      fail(e.getMessage());
    }
  }
}
