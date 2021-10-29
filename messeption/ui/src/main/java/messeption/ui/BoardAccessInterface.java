package messeption.ui;

import java.util.List;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.UserTextSubmission;

/**
 * Interface for how the ui interacts with the core moduel.
 */
public interface BoardAccessInterface {
  public void updateBoardChange() throws Exception;

  public ForumBoard readBoard() throws Exception;

  public void setBoard(ForumBoard board) throws Exception;

  public ForumPost getPost(int i);

  public List<ForumPost> getPosts();

  public void addPost(ForumPost post) throws Exception;

  public void removePost(int index) throws Exception;

  public void likePost(int index) throws Exception;

  public void dislikePost(int index) throws Exception;

  public void likeComment(int postIndex, int commentIndex) throws Exception;

  public void dislikeComment(int postIndex, int commentIndex) throws Exception;

  public List<PostComment> getComments(ForumPost post);

  public void addComment(int postIndex, PostComment comment) throws Exception;

}