package messeption.core;

/**
 * Extends UserTextSubmission and acts as a comment to a post.
 */
public class PostComment extends UserTextSubmission {

  public PostComment(String text, User author, boolean postedAnonymously) {
    super(text, author, postedAnonymously);
  }

  @Override
  public String toString() {
    return "\tText: " + this.text + "\nLikes: " + this.likeUsers + "\t Dislikes: "
        + this.dislikeUsers + "\n TimeStamp: " + this.timeStamp;
  }

  /**
   * Custom defined equals method for use when comparing with this objects serialized
   * and deserialized clone. Follows equals contract of reflexitivity, symmetry,
   * transitivity and consitancy

   * @param obj Other object to compare
   * @return true if objects have the same properties 
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (obj instanceof PostComment) {
      PostComment o = (PostComment) obj;
      boolean equalHashCode = (this.hashCode() == o.hashCode());
      boolean equalText = this.getText().equals(o.getText());
      boolean equalLikes = (this.getLikes() == (o.getLikes()));
      boolean equalDislikes = (this.getDislikes() == (o.getDislikes()));
      boolean equalTimeStamp = Math.pow(this.getTimeStamp().getTime() - o.getTimeStamp().getTime(),
          2) < Math.pow(1000, 2);
      boolean equalAuthor = this.getAuthor().equals(o.getAuthor());
      boolean equalAnonymous = (this.isAnonymous() == o.isAnonymous());

      return (equalText && equalLikes && equalDislikes && equalTimeStamp
        && equalHashCode && equalAuthor && equalAnonymous);
    } else {
      return false;
    }    
  }

  @Override
  public int hashCode() {
    return this.text.length() * this.likeUsers.size() * this.dislikeUsers.size();
  }
}
