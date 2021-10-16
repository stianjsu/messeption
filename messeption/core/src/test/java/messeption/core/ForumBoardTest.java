package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ForumBoardTest {
  ForumPost post;
  ForumBoard board;
  JsonReadWrite readWrite;

  @BeforeEach
  public void setup() throws IOException {
    String title1 = "POST";
    String text1 = "Lorem ipsum dolor sit amet";
    board = new ForumBoard();
    board.newPost(title1, text1);
    readWrite = new JsonReadWrite();
  }

  @Test
  @DisplayName("Test getter")
  public void testGetter() {
    post = board.getPost(0);
    assertEquals("POST", post.getTitle(), "Post title did not match expected title");
    assertEquals("Lorem ipsum dolor sit amet", post.getText(), "Post text did not match expected text");
  }

  @Test
  @DisplayName("Test delete post")
  public void testDeletePost() {
    post = board.getPost(0);
    board.deletePost(post);
    assertThrows(IndexOutOfBoundsException.class, () -> {
      board.getPost(0);
    }, "Did not throw exception when trying to get a deleted post");
  }

  @Test
  @DisplayName("Test save and load")
  public void testSaveLoad() throws IOException {
    String path = "core/";
    String fileName = "BoardTest.JSON";
    board.savePosts(path, fileName);
    ForumBoard board2 = new ForumBoard();
    board2.loadPosts(path, fileName);
    assertEquals(board.toString(), board2.toString(), "Post did not save and load properly");
  }



  @AfterAll
  public static void tearDown() {
    File testFile = new File(JsonReadWrite.ROOT_PATH + "core/BoardTest.JSON");
    testFile.delete();
  }

}
