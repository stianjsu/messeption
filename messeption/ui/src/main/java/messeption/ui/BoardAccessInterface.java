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

  public UserHandler readUsers() throws Exception;

  public void updateUsersChange() throws Exception;

  public User getActiveUser();

  public void setActiveUser(User user);

  public void addUser(String username, String password) throws Exception;

  public boolean userNameExists(String username);

  public boolean correctPassword(String username, String password);

}