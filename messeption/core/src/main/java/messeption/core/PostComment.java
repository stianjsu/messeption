package messeption.core;

/**
 * Extends UserTextSubmission and acts as a comment to a post.
 */
public class PostComment extends UserTextSubmission {

  public PostComment(String text) {
    super(text);
  }

  @Override
  public String toString() {
    return "\tText: " + this.text + "\nLikes: " + this.likes + "\t Dislikes: "
        + this.dislikes + "\n TimeStamp: " + this.timeStamp;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof PostComment){
      PostComment o = (PostComment) obj;
      boolean equalText = this.getText().equals(o.getText());
      boolean equalLikes = this.getLikes() == (o.getLikes());
      boolean equalDislikes = this.getDislikes() == (o.getDislikes());
      boolean equalTimeStamp = this.getTimeStamp() == (o.getTimeStamp());

      return (equalText && equalLikes && equalDislikes && equalTimeStamp);
    }
    else {
      return false;
    }    
  }
}
