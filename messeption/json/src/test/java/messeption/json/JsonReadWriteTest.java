package messeption.json;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import messeption.core.ForumBoard;
import messeption.core.UserHandler;
import messeption.core.User;

public class JsonReadWriteTest {

  ForumBoard board;
  UserHandler userHandler;
  JsonReadWrite readWrite;

  @BeforeEach
  public void setup() throws IOException {
    String title1 = "POST";
    String text1 = "Lorem ipsum dolor sit amet";
    User author = new User("Tester1", "test");
    board = new ForumBoard();
    board.newPost(title1, text1, author, true);

    userHandler = new UserHandler();
    try {
      userHandler.addUser("Jonah", "Hei123");
      userHandler.addUser("Stian", "Hei123");
      userHandler.addUser("Mattias", "Hei123");
      userHandler.addUser("Trygve", "Hei123");
    } catch (Exception e) {
      System.err.println(e);
    }
    
    this.readWrite = new JsonReadWrite(this.getClass().getResource("BoardTest.JSON"), this.getClass().getResource("UserMapTest.JSON"));
  }

  @Test
  @DisplayName("Test save and load of ForumBoard")
  public void testfileWriteAndReadForumBoard() throws IOException {
    readWrite.fileWriteForumBoard(board);
    ForumBoard board2 = readWrite.fileReadForumBoard();
    assertEquals(board, board2, "Post did not save and load board properly");
  }

  @Test
  @DisplayName("Test save and load of UserHandler")
  public void testfileWriteAndReadUserHandler() throws IOException {
    readWrite.fileWriteUserHandler(this.userHandler);
    UserHandler userHandler2 = readWrite.fileReadUserHandler();
    assertEquals(userHandler, userHandler2, "Post did not save and load users properly");
  }

  @Test
  @DisplayName("Test failed save and load")
  public void testFailSaveLoad() throws IOException {
    readWrite.setSaveLocationBoard(this.getClass().getResource(""));
    assertThrows(IOException.class, () -> {
      readWrite.fileWriteForumBoard(board);
    }, "Did not throw IOException");
    assertThrows(IOException.class, () -> {
      readWrite.fileReadForumBoard();
    }, "Did not throw IOException");
  }

}