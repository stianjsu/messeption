package messeption.ui;

import java.util.List;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.User;
import messeption.core.UserHandler;
import messeption.json.JsonReadWrite;

/**
 * Class for direct access to core for when the app is run localy.
 */
public class BoardAccessDirect implements BoardAccessInterface {

  private User activeUser;
  private ForumBoard board;
  private UserHandler userHandler;
  private JsonReadWrite boardReaderWriter;
  private JsonReadWrite usersReaderWriter;

  /**
   * Constructor for Direct Access that reads from file to prevent null pointer.
   */
  public BoardAccessDirect() {
    boardReaderWriter = new JsonReadWrite(this.getClass().getResource("Board.JSON"));
    usersReaderWriter = new JsonReadWrite(this.getClass().getResource("Users.JSON"));
    updateLocalBoard();
    try {
      readUsers();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void updateBoardChange() throws Exception {
    boardReaderWriter.fileWriteForumBoard(board);
  }

  @Override
  public ForumBoard readBoard() throws Exception {
    this.board = boardReaderWriter.fileReadForumBoard();
    return this.board;
  }

  /**
   * Updates the local ForumBoard state by reading from file.
   */
  private void updateLocalBoard() {
    try {
      this.board = boardReaderWriter.fileReadForumBoard();
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  @Override
  public void setBoard(ForumBoard board) throws Exception {
    this.board = board;
    updateBoardChange();
  }

  @Override
  public ForumPost getPost(String id) {
    updateLocalBoard();
    return board.getPost(id);
  }

  @Override
  public List<ForumPost> getPosts() {
    updateLocalBoard();
    return board.getPosts();
  }

  @Override
  public void addPost(ForumPost post) throws Exception {
    board.newPost(post);
    updateBoardChange();
  }

  @Override
  public void deletePost(String id) throws Exception {
    board.deletePost(board.getPost(id));
    updateBoardChange();
  }

  @Override
  public void likePost(String id, User user) throws Exception {
    board.getPost(id).like(user);
    updateBoardChange();
  }

  @Override
  public void dislikePost(String id, User user) throws Exception {
    board.getPost(id).dislike(user);
    updateBoardChange();
  }

  @Override
  public void likeComment(String postId, String commentId, User user) throws Exception {
    board.getPost(postId).getComment(commentId).like(user);
    updateBoardChange();
  }

  @Override
  public void dislikeComment(String postId, String commentId, User user) throws Exception {
    board.getPost(postId).getComment(commentId).dislike(user);
    updateBoardChange();
  }

  @Override
  public void addComment(String id, PostComment comment) throws Exception {
    board.getPost(id).addComment(comment);
    updateBoardChange();
  }

  @Override
  public void deleteComment(String postId, String commentId) throws Exception {
    board.getPost(postId).deleteComment(commentId);
    updateBoardChange();
  }

  public String getResourcesPath() {
    return boardReaderWriter.getSaveLocation();
  }
  
  @Override
  public UserHandler readUsers() throws Exception {
    this.userHandler = usersReaderWriter.fileReadUserHandler();
    return this.userHandler;
  }

  private void updateUsersChange() throws Exception {
    usersReaderWriter.fileWriteUserHandler(this.userHandler);
  }

  @Override
  public User getActiveUser() {
    return this.activeUser;
  }

  @Override
  public void setActiveUser(User user) {
    this.activeUser = user;
  }

  @Override
  public void addUser(String username, String password) throws Exception {
    this.userHandler.addUser(username, password);
    updateUsersChange();
  }

  @Override
  public boolean userNameExists(String username) {
    return userHandler.userNameExists(username);
  }
  
  @Override
  public boolean correctPassword(String username, String password) {
    return userHandler.correctPassword(username, password);
  }
}