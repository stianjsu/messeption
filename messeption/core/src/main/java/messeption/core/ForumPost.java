package messeption.core;

import java.util.ArrayList;
import java.util.List;

public class ForumPost extends UserTextSubmission {
  private String title;
  private List<PostComment> comments;

  public ForumPost(String title, String text) {
    super(text);
    this.title = title;
    this.comments = new ArrayList<>();
  }

  public String getTitle() {
    return this.title;
  }

  public List<PostComment> getComments() {
    return new ArrayList<>(comments);
  }

  @Override
  public String toString() {
    return "Title: " + this.title + "\tText: " + this.text + "\nLikes: " + this.likes + "\t Dislikes: " + this.dislikes
        + "\n TimeStamp: " + this.timeStamp;
  }
}
