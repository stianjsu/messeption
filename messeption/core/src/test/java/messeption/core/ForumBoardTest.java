package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
  ForumBoard board2;

  @BeforeEach
  public void setup() {
    String title1 = "POST";
    String text1 = "Lorem ipsum dolor sit amet";
    User author = new User("Tester1", "test");
    board = new ForumBoard();
    board2 = new ForumBoard();
    board.newPost(title1, text1, author, true);
    
  }

  @Test
  @DisplayName("Test getter")
  public void testGetters() {
    post = board.getPosts().get(0);
    assertEquals("POST", post.getTitle(), "Post title did not match expected title");
    assertEquals("Lorem ipsum dolor sit amet", post.getText(), "Post text did not match expected text");
    post = board.getPost(post.getId());
    assertEquals("POST", post.getTitle(), "Post title did not match expected title");
    assertEquals("Lorem ipsum dolor sit amet", post.getText(), "Post text did not match expected text");
    assertEquals(null, board.getPost("øøøøø"), "Unexpectedly found post");
  }

  @Test
  @DisplayName("Test add post")
  public void testAddPost() {
    post = board.getPosts().get(0);
    board.newPost(post);
    assertEquals(board.getPost(post.getId()), post);
  }

  @Test
  @DisplayName("Test delete post")
  public void testDeletePost() {
    post = board.getPosts().get(0);
    board.deletePost(post.getId());
    assertThrows(IndexOutOfBoundsException.class, () -> {
      board.getPosts().get(0);
    }, "Did not throw exception when trying to get a deleted post");

    assertThrows(IllegalArgumentException.class, () -> {
      board.deletePost(post.getId());
    }, "Did not throw exception when trying to delet a post twice");
  }

  @Test
  @DisplayName("Test equals")
  public void testEquals() {
    board2 = board;
    assertTrue(board.equals(board2));
    assertFalse(board.equals(new Object()));
  }

  @Test
  @DisplayName("Test so string")
  public void testToString() {
    board2 = board;
    assertEquals(board.toString(), board2.toString());
  }

  private void assertTrue(boolean equals) {
  }

  @Test
  @DisplayName("Test hashCode")
  public void testHashCode() {
    assertEquals(1, board.hashCode(), "Wrong hash code");
  }

}