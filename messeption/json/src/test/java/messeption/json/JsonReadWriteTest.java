package messeption.json;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import messeption.core.ForumBoard;

public class JsonReadWriteTest {

  ForumBoard board;

  @BeforeEach
  public void setup() throws IOException {
    String title1 = "POST";
    String text1 = "Lorem ipsum dolor sit amet";
    board = new ForumBoard();
    board.newPost(title1, text1);
    
  }

  @Test
  @DisplayName("Test save and load")
  public void testSaveLoad() throws IOException {
    String path = "json/";
    String fileName = "BoardTest.JSON";
    JsonReadWrite.fileWrite(path,fileName, board);
    ForumBoard board2 = JsonReadWrite.fileRead(path, fileName);
    assertEquals(board, board2, "Post did not save and load properly");
  }


  @AfterAll
  public static void tearDown() {
    File testFile = new File(JsonReadWrite.ROOT_PATH + "json/BoardTest.JSON");
    testFile.delete();
  }
}