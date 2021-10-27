package messeption.json;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import messeption.core.ForumBoard;
import messeption.core.UserHandler;

public class JsonReadWrite2Test {

  ForumBoard board;
  UserHandler userHandler;
  JsonReadWrite2 readWrite;

  @BeforeEach
  public void setup() throws IOException {
    String title1 = "POST";
    String text1 = "Lorem ipsum dolor sit amet";
    board = new ForumBoard();
    board.newPost(title1, text1);

    userHandler = new UserHandler();
    try {
      userHandler.addUser("Jonah", "Hei123");
      userHandler.addUser("Stian", "Hei123");
      userHandler.addUser("Mattias", "Hei123");
      userHandler.addUser("Trygve", "Hei123");
    } catch (Exception e) {
      System.err.println(e);
    }
    

    this.readWrite = new JsonReadWrite2(this.getClass().getResource("BoardTest2.JSON"));
    
  }

  @Test
  @DisplayName("Test save and load of ForumBoard")
  public void testfileWriteAndReadForumBoard() throws IOException {
    //String path = "json/";
    //String fileName = "BoardTest.JSON";
    readWrite.fileWriteForumBoard(board);
    ForumBoard board2 = readWrite.fileReadForumBoard();
    assertEquals(board, board2, "Post did not save and load properly");
  }

  @Test
  @DisplayName("Test save and load of UserHandler")
  public void testfileWriteAndReadUserHandler() throws IOException {
    //String path = "json/";
    //String fileName = "BoardTest.JSON";
    readWrite.setSaveLocationRecource(this.getClass().getResource("UserMapTest.JSON"));
    readWrite.fileWriteUserHandler(this.userHandler);
    UserHandler userHandler2 = readWrite.fileReadUserHandler();
    System.out.println(userHandler);
    System.out.println(userHandler2);
    assertEquals(userHandler, userHandler2, "Post did not save and load properly");
  }

  @Test
  @DisplayName("Test failed save and load")
  public void testFailSaveLoad() throws IOException {
    readWrite.setSaveLocationRecource(this.getClass().getResource(""));
    assertThrows(IOException.class, () -> {
      readWrite.fileWriteForumBoard(board);
    }, "Did not throw IOException");
    assertThrows(IOException.class, () -> {
      ForumBoard board2 = readWrite.fileReadForumBoard();
    }, "Did not throw IOException");
  }

}