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
    System.out.println(usersReaderWriter.getSaveLocation());
    try {

      readBoard();
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

  public ForumPost getPost(int i) {
    updateLocalBoard();
    return board.getPosts().get(i);
  }

  public List<ForumPost> getPosts() {
    updateLocalBoard();
    return board.getPosts();
  }

  public void addPost(ForumPost post) throws Exception {
    board.newPost(post);
    updateBoardChange();
  }

  public void removePost(int index) throws Exception {
    board.deletePost(board.getPost(index));
    updateBoardChange();
  }

  public void likePost(int index) throws Exception {
    board.getPost(index).incrementLikes();
    updateBoardChange();
  }

  public void dislikePost(int index) throws Exception {
    board.getPost(index).incrementDislikes();
    updateBoardChange();
  }

  public void likeComment(int postIndex, int commentIndex) throws Exception {
    board.getPost(postIndex).getComments().get(commentIndex).incrementLikes();
    updateBoardChange();
  }

  public void dislikeComment(int postIndex, int commentIndex) throws Exception {
    board.getPost(postIndex).getComments().get(commentIndex).incrementDislikes();
    updateBoardChange();
  }


  public List<PostComment> getComments(ForumPost post) {
    return post.getComments();
  }

  public void addComment(int postIndex, PostComment comment) throws Exception {
    board.getPost(postIndex).addComment(comment);
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