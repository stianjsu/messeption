package messeption.ui;

import java.io.IOException;
import java.util.List;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.User;
import messeption.core.UserHandler;
import messeption.core.UserTextSubmission;
import messeption.json.JsonReadWrite;
import messeption.ui.BoardAccessInterface;

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

  public void updateBoardChange() throws Exception {
    boardReaderWriter.fileWriteForumBoard(board);
  }

  public ForumBoard readBoard() throws Exception {
    this.board = boardReaderWriter.fileReadForumBoard();
    return this.board;
  }

  /**
   * Updates the local ForumBoard state by reading from file.
   */
  public void updateLocalBoard() {
    try {
      this.board = boardReaderWriter.fileReadForumBoard();
    } catch (Exception e) {
      System.out.println(e);
    }

  }

  public void setBoard(ForumBoard board) throws Exception {
    this.board = board;
    updateBoardChange();
  }

  public ForumPost getPost(String id) {
    updateLocalBoard();
    return board.getPost(id);
  }

  public List<ForumPost> getPosts() {
    updateLocalBoard();
    return board.getPosts();
  }

  public void addPost(ForumPost post) throws Exception {
    board.newPost(post);
    updateBoardChange();
  }

  public void removePost(String id) throws Exception {
    board.deletePost(board.getPost(id));
    updateBoardChange();
  }

  public void likePost(String id, User user) throws Exception {
    board.getPost(id).like(user);
    updateBoardChange();
  }

  public void dislikePost(String id, User user) throws Exception {
    board.getPost(id).dislike(user);
    updateBoardChange();
  }

  public void likeComment(String postId, String commentId, User user) throws Exception {
    board.getPost(postId).getComment(commentId).like(user);
    updateBoardChange();
  }

  public void dislikeComment(String postId, String commentId, User user) throws Exception {
    board.getPost(postId).getComment(commentId).dislike(user);
    updateBoardChange();
  }


  public List<PostComment> getComments(ForumPost post) {
    return post.getComments();
  }

  public void addComment(String id, PostComment comment) throws Exception {
    board.getPost(id).addComment(comment);
    updateBoardChange();
  }

  public String getResourcesPath() {
    return boardReaderWriter.getSaveLocation();
  }

  public UserHandler readUsers() throws Exception {
    this.userHandler = usersReaderWriter.fileReadUserHandler();
    return this.userHandler;
  }

  public void updateUsersChange() throws Exception {
    usersReaderWriter.fileWriteUserHandler(this.userHandler);
  }

  public User getActiveUser() {
    return this.activeUser;
  }

  public void setActiveUser(User user) {
    this.activeUser = user;
  }
  
  public void addUser(String username, String password) throws Exception {
    this.userHandler.addUser(username, password);
    updateUsersChange();
  }

  public boolean userNameExists(String username) {
    return userHandler.userNameExists(username);
  }

  public boolean correctPassword(String username, String password) {
    return userHandler.correctPassword(username, password);
  }

}