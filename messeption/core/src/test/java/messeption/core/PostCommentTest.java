package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostCommentTest extends UserTextSubmissionTestAbstract {

  private PostComment comment;

  @BeforeEach
  public void setup() {
    text1 = "Lorem ipsum dolor sit amet";
    users = new ArrayList<>();
    author = new User("Tester1", "test");
    comment = new PostComment(text1, author, true);
    textSubmission = comment;
    testDate = new Date();
    users.add(new User("tim", "Tom"));
    users.add(new User("aim", "Tom"));
    users.add(new User("bim", "Tom"));
    users.add(new User("cim", "Tom"));
    users.add(new User("dim", "Tom"));
    users.add(new User("eim", "Tom"));
  }

  @Test
  @DisplayName("Test equals")
  public void testEquals() {
    PostComment comment2 = new PostComment("Hei", author, true);
    assertFalse(comment2.equals(null));
    PostComment comment3 = new PostComment("Hei", author, true);
    assertTrue(comment2.equals(comment3));
    assertFalse(comment2.equals(new PostComment("ieH", author, false)));
    assertFalse(comment2.equals(new ForumBoard()));
  }

  @Test
  @DisplayName("Test toString")
  public void testToString() {
    PostComment comment2 = new PostComment("Hei", author, false);
    PostComment comment3 = new PostComment("Hei", author, false);
    assertTrue(comment2.toString().equals(comment3.toString()));
  }
}
