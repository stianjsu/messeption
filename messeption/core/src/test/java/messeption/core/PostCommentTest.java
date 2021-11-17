package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PostCommentTest {
  PostComment comment;
  String text1;
  Date testDate;
  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
  User author;
  List<User> users;


  @BeforeEach
  public void setup() {
    text1 = "Lorem ipsum dolor sit amet";
    users = new ArrayList<>();
    author = new User("Tester1", "test");
    comment = new PostComment(text1, author, true);
    testDate = new Date();
    users.add(new User("tim", "Tom"));
    users.add(new User("aim", "Tom"));
    users.add(new User("bim", "Tom"));
    users.add(new User("cim", "Tom"));
    users.add(new User("dim", "Tom"));
    users.add(new User("eim", "Tom"));
  }

  @Test
  @DisplayName("Test getters")
  public void testGetter() {
    assertEquals(text1, comment.getText(), "Wrong text from getter");
    assertEquals(0, comment.getLikes(), "Wrong likes value from getter");
    assertEquals(0, comment.getDislikes(), "Wrong dislikes value likes getter");
    assertEquals(author, comment.getAuthor(), "Wrong author from author getter");
    assertEquals(true, comment.isAnonymous(), "Wrong bool from is anonymous getter");
  }

  @Test
  @DisplayName("Test increment likes and dislikes")
  public void testIncrementLikesDislikes() {

    for (int i = 0; i < 3; i++) {
      comment.like(users.get(i));
    }
    assertEquals(3, comment.getLikes(), "Wrong likes value from getter");
    for (int i = 0; i < 5; i++) {
      comment.dislike(users.get(i));
    }

    assertEquals(0, comment.getLikes(), "Wrong likes value after liked comments were disliked");
    assertEquals(5, comment.getDislikes(), "Wrong dislikes value from getter");

  }

  @Test
  @DisplayName("Test time getters")
  public void testTimeGetter() {
    Date commentDate = comment.getTimeStamp();
    assertTrue(testDate.getTime()-commentDate.getTime() < 500, "Difference in time from comment and testTime is more than 500");
  }
}
