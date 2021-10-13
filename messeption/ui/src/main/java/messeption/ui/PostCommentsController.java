package messeption.ui;

import messeption.core.ForumPost;

public class PostCommentsController {

  private ForumPost post;

  public void initialize() {

  }

  public void setPost(ForumPost post) {

    this.post = post;

  }

  public String testController() {
    return post.toString();
  }
}
