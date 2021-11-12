package messeption.core;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * Forum Post is a single post that has text and can have children, in form of
 * comments.
 */
public class ForumPost extends UserTextSubmission implements Comparable<ForumPost> {
  private String title;
  private List<PostComment> comments;

  /**
   * Constructur. Uses UserTextSubmissions constructur for text, author, likes/dislikes
   * and time. Sets the title and initializes empty list of comments.

   * @param title title of post
   * @param text  text in post
   * @param author author of post
   */
  public ForumPost(String title, String text, User author, boolean postedAnonymously) {
    super(text, author, postedAnonymously);
    this.title = title;
    this.comments = new ArrayList<>();
  }

  /**
   * Getter for title.

   * @return Title of post
   */
  public String getTitle() {
    return this.title;
  }

  /**
   * Getter for comments.

   * @return List of comments
   */
  public List<PostComment> getComments() {
    return new ArrayList<>(comments);
  }

  /**
   * Getter for specified post with identity id.

   * @param id unique id
   * @return PostComment with matching id, if none returns null
   */
  public PostComment getComment(String id) {
    for (int index = 0; index < comments.size(); index++) {
      if (comments.get(index).getId().equals(id)) {
        return comments.get(index);
      }
    }
    return null;
  }

  /**
   * Adds a comment object to list of comments.

   * @param comment PostComment
   */
  public void addComment(PostComment comment) {
    this.comments.add(comment);
  }

  /**
   * Removes a comment object from list of comments.

   * @param comment PostComment
   * 
   * @throws IllegalArgumentException Comment not in comments
   */
  public void deleteComment(PostComment comment) throws IllegalArgumentException {
    if (!comments.contains(comment)) {
      throw new IllegalArgumentException("Comment is not in list of comments");
    }
    this.comments.remove(comment);
  }

  /**
   * Removes a comment object from list of comments.

   * @param id string formatted unique id

   * @throws IllegalArgumentException Comment not in comments
   */
  public void deleteComment(String id) throws IllegalArgumentException {
    deleteComment(this.getComment(id));
  }

  /**
   * Returns a string with information about all the fields in post.

   * @return String
   */
  @Override
  public String toString() {
    return "Title: " + this.title + "\tText: " + this.text + "\nLikes: " 
        + this.likeUsers + "\t Dislikes: " + this.dislikeUsers + "\n TimeStamp: " + this.timeStamp;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof ForumPost) {
      ForumPost o = (ForumPost) obj;
      boolean equalHashCode = this.hashCode() == o.hashCode();
      boolean equalTitle = this.getTitle().equals(o.getTitle());
      boolean equalComments = this.getComments().equals(o.getComments());
      boolean equalText = this.getText().equals(o.getText());
      boolean equalLikes = this.getLikes() == (o.getLikes());
      boolean equalDislikes = this.getDislikes() == (o.getDislikes());
      boolean equalTimeStamp = this.getTimeStamp().equals(o.getTimeStamp());
      boolean equalAuthor = this.getAuthor().equals(o.getAuthor());
      boolean equalAnonymous = this.isAnonymous() == o.isAnonymous();
       
      return (equalTitle && equalComments && equalText && equalLikes 
          && equalDislikes && equalTimeStamp && equalHashCode && equalAuthor && equalAnonymous);
    } else {
      return false;
    }    
  }

  @Override
  public int hashCode() {
    return this.text.length() * 5 + this.likeUsers.size() * 7 + this.dislikeUsers.size() * 11;
  }

  @Override
  public int compareTo(ForumPost o) {
    SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
    try {
      return sdf.parse(o.getTimeStamp()).compareTo(sdf.parse(this.getTimeStamp()));
    } catch (ParseException e) {
      return 0;
    }
  }
}
