package messeption.core;

import java.util.ArrayList;
import java.util.List;

/**
 * Forum Post is a single post that has text and can have children, in form of
 * comments.
 */
public class ForumPost extends UserTextSubmission {
  private String title;
  private List<PostComment> comments;

  /**
   * Constructur. Uses UserTextSubmissions constructur for text, likes/dislikes
   * and time. Sets the title and initializes empty list of comments.
   * 
   * @param title title of post
   * @param text  text in post
   */
  public ForumPost(String title, String text) {
    super(text);
    this.title = title;
    this.comments = new ArrayList<>();
  }

  /**
   * Getter for title.
   * 
   * @return Title of post
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Getter for comments.
   * 
   * @return List of comments
   */
  public List<PostComment> getComments() {
    return new ArrayList<>(comments);
  }

  /**
   * Adds a comment object to list of comments.
   * 
   * @param comment PostComment
   */
  public void addComment(PostComment comment) {
    this.comments.add(comment);
  }

  /**
   * Removes a comment object from list of comments.
   * 
   * @param comment PostComment
   * 
   * @throws IllegalArgumentException Comment not in comments
   */
  public void removeComment(PostComment comment) throws IllegalArgumentException {
    if (!comments.contains(comment)) {
      throw new IllegalArgumentException("Comment is not in list of comments");
    }
    this.comments.remove(comment);
  }

  /**
   * Returns a string with information about all the fields in post.
   * 
   * @return String
   */
  @Override
  public String toString() {
    return "Title: " + this.title + "\tText: " + this.text + "\nLikes: " + this.likes + "\t Dislikes: " + this.dislikes
        + "\n TimeStamp: " + this.timeStamp;
  }
}
