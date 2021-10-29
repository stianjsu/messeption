package messeption.core;

/**
 * Extends UserTextSubmission and acts as a comment to a post.
 */
public class PostComment extends UserTextSubmission {

  public PostComment(String text) {
    super(text);
  }

  public PostComment(String text, User author) {
    super(text, author);
  }

  @Override
  public String toString() {
    return "\tText: " + this.text + "\nLikes: " + this.likes + "\t Dislikes: "
        + this.dislikes + "\n TimeStamp: " + this.timeStamp;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PostComment) {
      PostComment o = (PostComment) obj;
      boolean equalHashCode = this.hashCode() == o.hashCode();
      boolean equalText = this.getText().equals(o.getText());
      boolean equalLikes = this.getLikes() == (o.getLikes());
      boolean equalDislikes = this.getDislikes() == (o.getDislikes());
      boolean equalTimeStamp = this.getTimeStamp().equals(o.getTimeStamp());

      return (equalText && equalLikes && equalDislikes && equalTimeStamp && equalHashCode);
    } else {
      return false;
    }    
  }

  @Override
  public int hashCode() {
    return this.text.length() * 5 + this.likes * 7 + this.dislikes * 11;
  }
}
