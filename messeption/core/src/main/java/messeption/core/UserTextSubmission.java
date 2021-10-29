package messeption.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Abstract object used as a template for user submission to the application.
 */
public abstract class UserTextSubmission {

  protected User author;
  protected String text;
  protected int likes;
  protected int dislikes;
  protected String timeStamp;

  /**
   * Initializes the default values of likes, dislikes and the current time.
   * Also sets the text to the input.

   * @param text the input text
   */
  public UserTextSubmission(String text) {
    this.author = User.getAnonymousUser();
    this.text = text;
    this.likes = 0;
    this.dislikes = 0;
    this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
  }

  /**
   * Initializes the default values of likes, dislikes and the current time.
   * Also sets the author to be correct.

   * @param text the input text
   * @param author the author of the submission
   */
  public UserTextSubmission(String text, User author) {
    this(text);
    this.author = author;
  }

  public String getText() {
    return this.text;
  }

  public int getLikes() {
    return this.likes;
  }

  public int getDislikes() {
    return this.dislikes;
  }

  public void incrementLikes() {
    this.likes++;
  }

  public void incrementDislikes() {
    this.dislikes++;
  }


  /**
   * Sets the current number of likes to a specific number.

   * @param likes the specific number of likes
   */
  public void setLikes(int likes) {
    if (likes < 0) {
      throw new IllegalArgumentException("Can't set negative likes");
    }
    this.likes = likes;
  }

  /**
   * Sets the current number of dislikes to a specific number.

   * @param dislikes the specific number of dislikes
   */
  public void setDislikes(int dislikes) {
    if (dislikes < 0) {
      throw new IllegalArgumentException("Can't set negative dislikes");
    }
    this.dislikes = dislikes;
  }

  public User getAuthor() {
    return this.author;
  }

  public void setAuthor(User user) {
    this.author = user;
  }

  public String getTimeStamp() {
    return this.timeStamp;
  }

  public abstract String toString();

  public abstract boolean equals(Object obj);
}
