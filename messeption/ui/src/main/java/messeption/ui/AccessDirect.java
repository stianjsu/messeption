package messeption.ui;

import java.io.IOException;
import java.util.List;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.UserTextSubmission;
import messeption.json.JsonReadWrite;
import messeption.ui.AccessInterface;

/**
 * Class for direct access to core for when the app is run localy.
 */
public class AccessDirect implements AccessInterface {
  private ForumBoard board = new ForumBoard();
  private JsonReadWrite readWrite = new JsonReadWrite();

  public void updateBoardChange() throws Exception { 
    readWrite.fileWrite(board);
  }

  public ForumBoard readBoard() throws Exception {
    return readWrite.fileRead();
  }

  public ForumPost getPost(int i) {
    return board.getPost(i);
  }

  public List<ForumPost> getPosts() {
    return board.getPosts();
  }

  public void addPost(String title, String text) throws Exception {
    board.newPost(title, text);
    updateBoardChange();
  }

  public void removePost(ForumPost post) throws Exception {
    board.deletePost(post);
    updateBoardChange();
  }

  public void likeSubmission(UserTextSubmission submission) throws Exception {
    submission.incrementLikes();
    updateBoardChange();
  }

  public void dislikeSubmission(UserTextSubmission submission) throws Exception {
    submission.incrementDislikes();
    updateBoardChange();
  }

  public List<PostComment> getComments(ForumPost post) {
    return post.getComments();
  }

  public void addComment(ForumPost post, String text) throws Exception {
    post.addComment(new PostComment(text));
    updateBoardChange();
  }

}
