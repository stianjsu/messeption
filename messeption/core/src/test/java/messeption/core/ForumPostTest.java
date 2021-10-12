package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;

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


  @BeforeEach
  public void setup() {
    title1 = "POST";
    text1 = "Lorem ipsum dolor sit amet";

    post = new ForumPost(title1, text1);
    testDate = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
  }

  @Test
  @DisplayName("Test getters")
  public void testGetter() {
    assertEquals(title1, post.getTitle(), "Wrong title from getter");
    assertEquals(text1, post.getText(), "Wrong text from getter");

    assertEquals(0, post.getLikes(), "Wrong likes value from getter");
    assertEquals(0, post.getDislikes(), "Wrong dislikes value likes getter");
  }

  @Test
  @DisplayName("Test set likes and dislikes")
  public void testSetLikesDislikes() {

    post.setLikes(3);
    post.setDislikes(5);

    assertEquals(3, post.getLikes(), "Wrong likes value after setter");
    assertEquals(5, post.getDislikes(), "Wrong dislikes value after setter");

    assertThrows(IllegalArgumentException.class, () -> post.setLikes(-5),
            "No exception was thrown when negative input was set");
    assertThrows(IllegalArgumentException.class, () -> post.setDislikes(-9),
            "No exception was thrown when negative input was set");


  }

  @Test
  @DisplayName("Test increment likes and dislikes")
  public void testIncrementLikesDislikes() {

    for (int i = 0; i < 3; i++) {
      post.incrementLikes();
    }
    for (int i = 0; i < 5; i++) {
      post.incrementDislikes();
    }

    assertEquals(3, post.getLikes(), "Wrong likes value from getter");
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
}
