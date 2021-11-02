package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ForumPostTest {
  ForumPost post;
  String title1;
  String text1;
  String testDate;
  SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss dd-MM-yyyy");
  List<User> users;


  @BeforeEach
  public void setup() {
    title1 = "POST";
    text1 = "Lorem ipsum dolor sit amet";
    users = new ArrayList<>();
    post = new ForumPost(title1, text1);
    testDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
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
    assertEquals(title1, post.getTitle(), "Wrong title from getter");
    assertEquals(text1, post.getText(), "Wrong text from getter");

    assertEquals(0, post.getLikes(), "Wrong likes value from getter");
    assertEquals(0, post.getDislikes(), "Wrong dislikes value likes getter");
    assertEquals(new ArrayList<>(), post.getComments(), "Wrong comment list from getter");
  }

  @Test
  @DisplayName("Test increment likes and dislikes")
  public void testIncrementLikesDislikes() {

    for (int i = 0; i < 3; i++) {
      post.like(users.get(i));
    }
    assertEquals(3, post.getLikes(), "Wrong likes value from getter");
    for (int i = 0; i < 5; i++) {
      post.dislike(users.get(i));
    }
    assertEquals(0, post.getLikes(), "Wrong likes value after liked posts were disliked");
    assertEquals(5, post.getDislikes(), "Wrong dislikes value from getter");
  }

  @Test
  @DisplayName("Test time getters")
  public void testTimeGetter() {

    try {
      Date postDate = sdf.parse(post.getTimeStamp());
      Date testDateFormated = sdf.parse(testDate);

      long diff = Math.abs(postDate.getTime() - testDateFormated.getTime());

      assertTrue(diff < 10000, "Time difference is more than 10 seconds");

    } catch (ParseException e) {
      assertTrue(false, "Exception thrown when parsing dates from string. \n" + e.getMessage());
    }
  }

  @Test
  @DisplayName("Test adding and getting comments")
  public void testComments() {
    PostComment comment1= new PostComment("comment");
    post.addComment(comment1);

    assertEquals(comment1, post.getComments().get(0), "comment was not correct after add comment");

    PostComment comment2 = new PostComment("comment2");
    post.addComment(comment2);

    assertEquals(new ArrayList<>(Arrays.asList(comment1,comment2)), post.getComments(), "Wrong comment list from getComments");
  }
}
