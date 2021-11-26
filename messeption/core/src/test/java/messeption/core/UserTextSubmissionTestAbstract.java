package messeption.core;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public abstract class UserTextSubmissionTestAbstract {
  UserTextSubmission textSubmission;
  String text1;
  Date testDate;
  User author;
  List<User> users;


  @BeforeEach
  public abstract void setup();

  @Test
  @DisplayName("Test constructor with getters")
  public void testConstructor() {
    assertEquals(text1, textSubmission.getText(), "Wrong text from getter");
    assertEquals(0, textSubmission.getLikes(), "Wrong likes value from getter");
    assertEquals(0, textSubmission.getDislikes(), "Wrong dislikes value likes getter");
    assertEquals(author, textSubmission.getAuthor(), "Wrong author from author getter");
    assertEquals(true, textSubmission.isAnonymous(), "Wrong bool from is anonymous getter");
  }

  @Test
  @DisplayName("Test increment likes and dislikes")
  public void testIncrementLikesDislikes() {

    for (int i = 0; i < 3; i++) {
      textSubmission.like(users.get(i));
    }
    assertEquals(3, textSubmission.getLikes(), "Wrong likes value from getter");
    for (int i = 0; i < 5; i++) {
      textSubmission.dislike(users.get(i));
    }

    assertEquals(0, textSubmission.getLikes(), "Wrong likes value after liked textSubmissions were disliked");
    assertEquals(5, textSubmission.getDislikes(), "Wrong dislikes value from getter");

  }

  @Test
  @DisplayName("Test time getters")
  public void testTimeGetter() {
    Date textSubmissionDate = textSubmission.getTimeStamp();
    assertTrue(testDate.getTime()-textSubmissionDate.getTime() < 500, "Difference in time from textSubmission and testTime is more than 500");
  }

  public abstract void testEquals();

}
