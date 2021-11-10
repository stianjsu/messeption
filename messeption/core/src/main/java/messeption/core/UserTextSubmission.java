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
  protected boolean postedAnonymously;
  protected String text;
  protected Collection<User> likeUsers;
  protected Collection<User> dislikeUsers;
  protected String timeStamp;
  protected String id;
  public static final String ANONYMOUS_NAME = "Anonymous";


  /**
   * Initializes the default values of likes, dislikes and the current time.
   * Also sets the author to be correct and if it shall be shown as anonymous

   * @param text the input text
   * @param author the author of the submission
   * @param postedAnonymously the author of the submission
   */
  public UserTextSubmission(String text, User author, boolean postedAnonymously) {
    this.author = author;
    this.postedAnonymously = postedAnonymously;
    this.text = text;
    this.likeUsers = new ArrayList<>();
    this.dislikeUsers = new ArrayList<>();
    this.timeStamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss dd-MM-yyyy"));
    this.id = timeStamp.replaceAll("\\s+", "") + text.length();
  }

  public String getText() {
    return this.text;
  }

  public int getLikes() {
    return this.likeUsers.size();
  }

  public int getDislikes() {
    return this.dislikeUsers.size();
  }

  public Collection<User> getLikeUsers() {
    return new ArrayList<>(likeUsers);
  }

  public Collection<User> getDislikeUsers() {
    return new ArrayList<>(dislikeUsers);
  }

  public String getId() {
    return this.id;
  }

  /**
  * Likes the post if it is not already likes by the user.

  * @param user the user to like the test
  */
  public void like(User user) {
    if (! this.likeUsers.remove(user)) {
      this.likeUsers.add(user);
      this.dislikeUsers.remove(user);
    }
    
  }

  /**
  * Dislikes the post if it is not already disliked by the user.

  * @param user the user to dislike the test
  */
  public void dislike(User user) {
    if (! this.dislikeUsers.remove(user)) {
      this.dislikeUsers.add(user);
      this.likeUsers.remove(user);
    }
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

  public boolean isAnonymous() {
    return this.postedAnonymously;
  }

  public abstract String toString();

  public abstract boolean equals(Object obj);
}
