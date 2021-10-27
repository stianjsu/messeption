package messeption.ui;

import java.util.List;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.UserTextSubmission;

/**
 * Interface for how the ui interacts with the core moduel.
 */
public interface AccessInterface {
  public void updateBoardChange() throws Exception;

  public ForumBoard readBoard() throws Exception;

  public ForumPost getPost(int i);

  public List<ForumPost> getPosts();

  public void addPost(String title, String text) throws Exception;

  public void removePost(ForumPost post) throws Exception;

  public void likeSubmission(UserTextSubmission submission) throws Exception;

  public void dislikeSubmission(UserTextSubmission submission) throws Exception;

  public List<PostComment> getComments(ForumPost post);

  public void addComment(ForumPost post, String text) throws Exception;

}