package messeption.ui;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import messeption.core.ForumPost;
import messeption.core.PostComment;

/**
 * Javafx controller for viewing individual posts.
 */
public class PostPageController extends SceneController {

  private static final int SIZE_COMMENTS = 130;
  private static final int MARGIN_COMMENTS = 10;
  
  @FXML
  Label postTitleLabel;
  @FXML
  Label postAuthorLabel;
  @FXML
  Label postTimeStampLabel;
  @FXML
  TextArea postTextArea;
  @FXML
  Label postCommentsLabel;
  @FXML
  Label postLikeLabel;
  @FXML
  Label postDislikeLabel;
  @FXML
  Button postLikeButton;
  @FXML
  Button postDislikeButton;
  @FXML
  ScrollPane commentsScrollPane;
  @FXML
  AnchorPane commentsContainer;
  @FXML
  Button cancelButton;

  @FXML
  Label newCommentFeedbackLabel;
  @FXML
  TextArea newCommentTextArea;
  @FXML
  Button newCommentButton;
  @FXML
  CheckBox anonymousAuthorCheckBox;


  private String commentFeedback;


  /**
   * Initializes the publish comment button.
   */
  public void initialize() {
    super.init();

    this.cancelButton.setOnAction(event -> {
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (Exception e) {
        UiUtils.popupAlert(e, "Something went wrong when loading page").showAndWait();
      }
    });

    this.commentFeedback = newCommentFeedbackLabel.getText();
    newCommentTextArea.setOnKeyTyped(e -> {
      if (newCommentTextArea.getText().length() < 4) {
        newCommentFeedbackLabel.setText(commentFeedback);
      } else {        
        newCommentFeedbackLabel.setText("");
      }
      updateButtonEnabled();
    });
  }

  /**
   * Javafx help method to display comments in the UI.
   */
  public void drawComments(String postId) {

    ForumPost post = boardAccess.getPost(postId);
    
    newCommentButton.setOnAction(e -> {
      publishComment(post.getId());
    });

    generatePostContent(post);
    List<PostComment> comments = post.getComments();

    this.commentsContainer.getChildren().clear();

    for (int indexId = 0; indexId < comments.size(); indexId++) {
      PostComment comment = comments.get(indexId);

      try {
        Pane commentPane = generateCommentPane(comment, postId);
        commentPane.setLayoutY((MARGIN_COMMENTS + SIZE_COMMENTS) * indexId + MARGIN_COMMENTS);

        this.commentsContainer.getChildren().add(commentPane);

      } catch (Exception error) {
        UiUtils.popupAlert(error, "Something went wrong when loading page").showAndWait();
      }

    }
    commentsContainer.setPrefHeight(
        (MARGIN_COMMENTS + SIZE_COMMENTS) * comments.size() + MARGIN_COMMENTS);
  }


  /**
   * Displays all information about the post and relevant buttons on the page.

   * @param post post to display
   */
  private void generatePostContent(ForumPost post) {
    postTitleLabel.setText(post.getTitle());
    postAuthorLabel.setText("Post by: " + (post.isAnonymous()
        ? ForumPost.ANONYMOUS_NAME : post.getAuthor().getUsername()));

    
    postTimeStampLabel.setText(post.getTimeStamp().toString());

    postTextArea.setText(post.getText());
    postTextArea.setStyle("-fx-opacity: 1;");

    postCommentsLabel.setText(post.getComments().size() + " comments");
    postLikeLabel.setText(post.getLikes() + " likes");
    postDislikeLabel.setText(post.getDislikes() + " dislikes");

    UiUtils.setStyleOfButton(postLikeButton,
        post.getLikeUsers().contains(boardAccess.getActiveUser()));
    UiUtils.setStyleOfButton(postDislikeButton,
        post.getDislikeUsers().contains(boardAccess.getActiveUser()));

    postLikeButton.setOnAction(e -> {
      try {
        boardAccess.likePost(post.getId(), boardAccess.getActiveUser());
      } catch (Exception error) {
        UiUtils.popupAlert(error, "Something went wrong with liking a post").showAndWait();

      }
      UiUtils.setStyleOfButton(postLikeButton,
            post.getLikeUsers().contains(boardAccess.getActiveUser()));
      UiUtils.setStyleOfButton(postDislikeButton,
            post.getDislikeUsers().contains(boardAccess.getActiveUser()));

      postDislikeLabel.setText(post.getDislikes() + " dislikes");
      postLikeLabel.setText(post.getLikes() + " likes");
    });

    postDislikeButton.setOnAction(e -> {
      try {
        boardAccess.dislikePost(post.getId(), boardAccess.getActiveUser());
      } catch (Exception error) {
        UiUtils.popupAlert(error, "Something went wrong with disliking a post").showAndWait();
      }
      UiUtils.setStyleOfButton(postLikeButton,
            post.getLikeUsers().contains(boardAccess.getActiveUser()));
      UiUtils.setStyleOfButton(postDislikeButton,
            post.getDislikeUsers().contains(boardAccess.getActiveUser()));

      postDislikeLabel.setText(post.getDislikes() + " dislikes");
      postLikeLabel.setText(post.getLikes() + " likes");
    });
  }


  /**
   * Generates a pane that contains all information about the comment and relevant buttons.

   * @param comment comment to display
   * @param postId id of post that contains comment 
   * @return pane to add to scene
   * @throws IOException When failing to load CommentPaneTemplate.fxml
   */
  private Pane generateCommentPane(PostComment comment, String postId) 
      throws IOException {
    FXMLLoader commentPaneTemplateLoader = new FXMLLoader(getClass().getResource(
        "CommentPaneTemplate.fxml"));
    commentPaneTemplateLoader.load();
    CommentPaneTemplateController commentPaneTemplateController =
        commentPaneTemplateLoader.getController();
    commentPaneTemplateController.setBoardAccess(boardAccess);
    commentPaneTemplateController.setPostPageController(this);
    return commentPaneTemplateController.setFieldsComment(comment, postId);
  }

  private void publishComment(String postId) {

    try {
      String text = newCommentTextArea.getText();

      boardAccess.addComment(postId, new PostComment(text, boardAccess.getActiveUser(), 
          anonymousAuthorCheckBox.isSelected()));
      drawComments(postId);
      newCommentTextArea.clear();
      updateButtonEnabled();

    } catch (Exception e) {
      UiUtils.popupAlert(e, "Something went wrong when loading page").showAndWait();
    }
  }

  private void updateButtonEnabled() {
    newCommentButton.setDisable(newCommentTextArea.getText().length() < 4);
  }
  
  /**
   * Reload page resets the page to the normal state.
   */
  public void reloadPage() {
    commentsScrollPane.setVvalue(0);
    newCommentFeedbackLabel.setText("");
    newCommentButton.setDisable(true);
    newCommentTextArea.clear();
    anonymousAuthorCheckBox.setSelected(false);
  }
}
