package messeption.core;

public class PostComment extends UserTextSubmission {

  public PostComment(String text) {
    super(text);
  }

  @Override
  public String toString() {
    return "\tText: " + this.text + "\nLikes: " + this.likes + "\t Dislikes: " + this.dislikes + "\n TimeStamp: "
        + this.timeStamp;
  }
}
