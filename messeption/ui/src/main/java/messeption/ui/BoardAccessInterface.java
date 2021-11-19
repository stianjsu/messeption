package messeption.ui;

import java.util.List;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.User;
import messeption.core.UserHandler;

/**
 * Interface for how the ui interacts with the core moduel.
 */
public interface BoardAccessInterface {
  public void updateBoardChange() throws Exception;

  public ForumBoard readBoard() throws Exception;

  public void setBoard(ForumBoard board) throws Exception;

  public ForumPost getPost(String id);

  public List<ForumPost> getPosts();

  public void addPost(ForumPost post) throws Exception;

  public void deletePost(String id) throws Exception;

  public void likePost(String id, User user) throws Exception;

  public void dislikePost(String id, User user) throws Exception;

  public void likeComment(String postId, String commentId, User user) throws Exception;

  public void dislikeComment(String postId, String commentId, User user) throws Exception;

  public void addComment(String id, PostComment comment) throws Exception;

  public void deleteComment(String postId, String commentId) throws Exception;

  public UserHandler readUsers() throws Exception;

  public User getActiveUser();

  public void setActiveUser(User user);

  public void addUser(String username, String password) throws Exception;

  public boolean userNameExists(String username);

  public boolean correctPassword(String username, String password);

  public void setUserHandler(UserHandler handler) throws Exception;

}