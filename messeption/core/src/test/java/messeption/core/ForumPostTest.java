package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.concurrent.TimeUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ForumPostTest extends UserTextSubmissionTestAbstract {

  ForumPost post;
  String title1;
  
  @BeforeEach
  @Override
  public void setup() {
    title1 = "POST";
    text1 = "Lorem ipsum dolor sit amet";
    users = new ArrayList<>();
    author = new User("Tester1", "test");
    post = new ForumPost(title1, text1, author, true);
    textSubmission = post;
    testDate = new Date();
    users.add(new User("tim", "Tom"));
    users.add(new User("aim", "Tom"));
    users.add(new User("bim", "Tom"));
    users.add(new User("cim", "Tom"));
    users.add(new User("dim", "Tom"));
    users.add(new User("eim", "Tom"));
  }

  @Test
  @DisplayName("Test constructor with getters")
  @Override
  public void testConstructor() {
    assertEquals(title1, post.getTitle(), "Wrong title from getter");
    super.testConstructor();
  }

  @Test
  @DisplayName("Test adding and getting comments")
  public void testComments() {
    User commenter = new User("Commenter1", "test");
    PostComment comment1= new PostComment("comment", commenter, true);
    post.addComment(comment1);

    assertEquals(comment1, post.getComments().get(0), "comment was not correct after add comment");

    User commenter2 = new User("Commenter2", "test");
    PostComment comment2 = new PostComment("comment2", commenter2, true);
    post.addComment(comment2);

    assertEquals(new ArrayList<>(Arrays.asList(comment1, comment2)), post.getComments(), "Wrong comment list from getComments");
  }

  @Test
  @DisplayName("Test deleing a comment")
  public void testDeleteComment(){
    User commenter = new User("Commenter1", "test");
    PostComment comment1= new PostComment("comment", commenter, true);
    post.addComment(comment1);
    assertEquals(comment1, post.getComments().get(0), "comment was not correct after add comment");
    post.deleteComment(comment1.getId());
    assertEquals(null, post.getComment(comment1.getId()));
    assertThrows(IllegalArgumentException.class, () -> {
      post.deleteComment(comment1.getId());
    });
  }

  @Test
  @DisplayName("Test equals")
  @Override
  public void testEquals() {
    assertFalse(post.equals(null));
    assertTrue(post.equals(post));
    ForumPost post2 = post;
    assertTrue(post.equals(post2));
    assertFalse(post.equals(new ForumPost("This is title", "This is text", author, true)));
    assertFalse(post.equals(new Object()));
  }

  @Test
  @DisplayName("Test to string")
  @Override
  public void testToString() {
    ForumPost post2 = post;
    assertEquals(post.toString(), post2.toString());
    assertNotEquals(post.toString(), new Object().toString());
  }

  @Test
  @DisplayName("Test compare to")
  public void testCompareTo() throws Exception {
    ForumPost post2 = post;
    TimeUnit.SECONDS.sleep(1);
    ForumPost post3 = new ForumPost("Hei", "Hallo", author, false);
    assertEquals(0, post.compareTo(post2));
    assertEquals(1, post.compareTo(post3));
  }
}
