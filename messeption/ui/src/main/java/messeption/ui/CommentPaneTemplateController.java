package messeption.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import messeption.core.PostComment;

/**
 * CommentPaneTemplateController fills the template with valid data.
 */
public class CommentPaneTemplateController {

  @FXML
  Pane commentPane;
  @FXML
  Label authorLabel;
  @FXML
  Label commentTimeStampLabel;
  @FXML
  TextArea commentTextArea;
  @FXML
  Label likeCommentLabel;
  @FXML
  Label dislikeCommentLabel;
  @FXML
  Button likeCommentButton;
  @FXML
  Button dislikeCommentButton;
  @FXML
  Button deleteButton;

  private BoardAccessInterface boardAccess;
  private PostPageController postPageController;

  public void initialize() {}

  public void setBoardAccess(BoardAccessInterface boardAccess) {
    this.boardAccess = boardAccess;
  }
  
  public void setPostPageController(PostPageController postPageController) {
    this.postPageController = postPageController;
  }
  
  /**
   * Fills the FXML nodes with valid information for the given comment.
   * Text, Author, Timestamp is shown and OnAction events are set for the buttons.

   * @param comment The PostComment to be displayed
   * @param postId the id of the post the comment "belongs to"
   */
  public Pane setFieldsComment(PostComment comment, String postId) {

    authorLabel.setText(comment.isAnonymous()
        ? PostComment.ANONYMOUS_NAME : comment.getAuthor().getUsername());
    
    commentTextArea.setFont(new Font(15));
    commentTextArea.setText(comment.getText());
    
    likeCommentLabel.setText(comment.getLikes() + " likes");
    
    dislikeCommentLabel.setText(comment.getDislikes() + " dislikes");
    
    UiUtils.setStyleOfButton(likeCommentButton,
          comment.getLikeUsers().contains(boardAccess.getActiveUser()));
    UiUtils.setStyleOfButton(dislikeCommentButton,
          comment.getDislikeUsers().contains(boardAccess.getActiveUser()));

    likeCommentButton.setOnAction(e -> {

      try {
        boardAccess.likeComment(postId, comment.getId(), boardAccess.getActiveUser());
      } catch (Exception error) {
        UiUtils.popupAlert(error, "Something went wrong when liking a comment").showAndWait();
      }
      UiUtils.setStyleOfButton(likeCommentButton,
          comment.getLikeUsers().contains(boardAccess.getActiveUser()));
      UiUtils.setStyleOfButton(dislikeCommentButton,
          comment.getDislikeUsers().contains(boardAccess.getActiveUser()));

      likeCommentLabel.setText(comment.getLikes() + " likes");
      dislikeCommentLabel.setText(comment.getDislikes() + " dislikes");
    });
  
    dislikeCommentButton.setOnAction(e -> {
      try {
        boardAccess.dislikeComment(postId, comment.getId(), boardAccess.getActiveUser());
      } catch (Exception error) {
        UiUtils.popupAlert(error, "Something went wrong when disliking a comment").showAndWait();
      }
      UiUtils.setStyleOfButton(likeCommentButton,
          comment.getLikeUsers().contains(boardAccess.getActiveUser()));
      UiUtils.setStyleOfButton(dislikeCommentButton,
          comment.getDislikeUsers().contains(boardAccess.getActiveUser()));

      likeCommentLabel.setText(comment.getLikes() + " likes");
      dislikeCommentLabel.setText(comment.getDislikes() + " dislikes");
    });
    
    commentTimeStampLabel.setText(comment.getTimeStamp().toString());
    

    if (boardAccess.getActiveUser() != null 
        && boardAccess.getActiveUser().equals(comment.getAuthor())) {
      
      deleteButton.setOnAction(e -> {
        try {
          boolean confirmation = UiUtils.confimationAlert("Confirm deletion",
              "Delete comment on post: " + boardAccess.getPost(postId).getTitle(),
              "Are you sure you want to delete your comment?");
              
          if (confirmation) {
            boardAccess.deleteComment(postId, comment.getId());
            postPageController.drawComments(postId);
          }
        } catch (Exception e1) {
          UiUtils.popupAlert(e1, "Something went wrong when deleting a comment").showAndWait();
        }
      });
      deleteButton.setVisible(true);
    }
    return this.commentPane;
  }
  
}