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

  @BeforeEach
  public void setup() {
    String title1 = "POST";
    String text1 = "Lorem ipsum dolor sit amet";
    User author = new User("Tester1", "test");
    board = new ForumBoard();
    board.newPost(title1, text1, author, true);
    
  }

  @Test
  @DisplayName("Test getter")
  public void testGetter() {
    post = board.getPosts().get(0);
    assertEquals("POST", post.getTitle(), "Post title did not match expected title");
    assertEquals("Lorem ipsum dolor sit amet", post.getText(), "Post text did not match expected text");
  }

  @Test
  @DisplayName("Test delete post")
  public void testDeletePost() {
    post = board.getPosts().get(0);
    board.deletePost(post);
    assertThrows(IndexOutOfBoundsException.class, () -> {
      board.getPosts().get(0);
    }, "Did not throw exception when trying to get a deleted post");
  }

}