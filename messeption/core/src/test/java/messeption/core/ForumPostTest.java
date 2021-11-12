package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
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
  User author;
  List<User> users;


  @BeforeEach
  public void setup() {
    title1 = "POST";
    text1 = "Lorem ipsum dolor sit amet";
    users = new ArrayList<>();
    author = new User("Tester1", "test");
    post = new ForumPost(title1, text1, author, true);
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
    assertEquals(author, post.getAuthor(), "Wrong author from author getter");
    assertEquals(true, post.isAnonymous(), "Wrong bool from is anonymous getter");
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
  public void testEquals() {
    ForumPost post2 = post;
    assertTrue(post.equals(post2));
    assertFalse(post.equals(new Object()));
  }

  @Test
  @DisplayName("Test compare to")
  public void testCompareTo() {
    ForumPost post2 = post;
    assertEquals(0, post.compareTo(post2));
  }
}
