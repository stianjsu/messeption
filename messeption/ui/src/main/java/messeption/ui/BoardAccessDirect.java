package messeption.ui;

import java.io.IOException;
import java.util.List;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.UserTextSubmission;
import messeption.json.JsonReadWrite;
import messeption.ui.BoardAccessInterface;

/**
 * Class for direct access to core for when the app is run localy.
 */
public class BoardAccessDirect implements BoardAccessInterface {

  private ForumBoard board;
  private JsonReadWrite readerWriter;

  /**
   * Constructor for Direct Access that reads from file to prevent null pointer.
   */
  public BoardAccessDirect() {
    readerWriter = new JsonReadWrite(this.getClass().getResource("Board.JSON"));
    try {

      readBoard();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void updateBoardChange() throws Exception {
    readerWriter.fileWriteForumBoard(board);
  }

  public ForumBoard readBoard() throws Exception {
    this.board = readerWriter.fileReadForumBoard();
    return this.board;
  }

  /**
   * Updates the local ForumBoard state by reading from file.
   */
  public void updateLocalBoard() {
    try {
      this.board = readerWriter.fileReadForumBoard();
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
    return readerWriter.getSaveLocation();
  }

}