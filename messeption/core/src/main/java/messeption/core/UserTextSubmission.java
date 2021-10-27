package messeption.core;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract object used as a template for user submission to the application.
 */
public abstract class UserTextSubmission {

  protected String author;
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
    this.author = "Anonymous";
    this.text = text;
    this.likes = 0;
    this.dislikes = 0;
    this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
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

  public String getAuthor(){
    return this.author;
  }

  public void setAuthor(String username){
    this.author = username;
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

  public String getTimeStamp() {
    return this.timeStamp;
  }

  public abstract String toString();

  public abstract boolean equals(Object obj);
}
