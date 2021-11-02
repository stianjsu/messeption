package messeption.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;

/**
 * Javafx controller for viewing individual posts.
 */
public class PostPageController {

  private static final int SIZE_COMMENTS = 130;
  private static final int MARGIN_COMMENTS = 10;

  @FXML
  Label postTitleLabel;
  @FXML
  Label postAuthorLabel;
  @FXML
  TextArea postTextArea;
  @FXML
  Label postLikeLabel;
  @FXML
  Label postDislikeLabel;
  @FXML
  Button postLikeButton;
  @FXML
  Button postDislikeButton;
  @FXML
  AnchorPane commentsContainer;
  @FXML
  Button cancelButton;

  @FXML
  TextArea newCommentTextArea;
  @FXML
  Button newCommentButton;
  @FXML
  CheckBox anonymousAuthorCheckBox;

  private BoardAccessInterface boardAccess;

  /**
   * Initializes the publish comment button.
   */
  public void initialize() {
    
  }


  /**
   * Sets the controlelr forumboard to a specified input.

   * @param boardAccess the new controller boardAccess
   */
  public void setBoardAccess(BoardAccessInterface boardAccess) {
    this.boardAccess = boardAccess;
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
        UiUtils.exceptionAlert(error).showAndWait();
      }

    }
    commentsContainer.setPrefHeight(
        (MARGIN_COMMENTS + SIZE_COMMENTS) * comments.size() + MARGIN_COMMENTS);
  }

  private void generatePostContent(ForumPost post) {
    postTitleLabel.setText(post.getTitle());
    postAuthorLabel.setText("Post by: " + post.getAuthor().getUsername());

    postTextArea.setText(post.getText());
    postTextArea.setDisable(true);
    postTextArea.setStyle("-fx-opacity: 1;");

    postLikeLabel.setText(post.getLikes() + " likes");
    postDislikeLabel.setText(post.getDislikes() + " dislikes");

    postLikeButton.setOnAction(e -> {
      int prevLikes = post.getLikes();

      try {
        boardAccess.likePost(post.getId(), boardAccess.getActiveUser());
      } catch (Exception error) {
        UiUtils.exceptionAlert(error).showAndWait();
      }

      postDislikeLabel.setText(post.getDislikes() + " dislikes");
      postLikeLabel.setText(post.getLikes() + " likes");
    });

    postDislikeButton.setOnAction(e -> {
      int prevDislikes = post.getDislikes();

      try {
        boardAccess.dislikePost(post.getId(), boardAccess.getActiveUser());
      } catch (Exception error) {
        UiUtils.exceptionAlert(error).showAndWait();
      }
      postDislikeLabel.setText(post.getDislikes() + " dislikes");
      postLikeLabel.setText(post.getLikes() + " likes");
    });
  }

  private Pane generateCommentPane(PostComment comment, String postId) 
      throws IOException {
    Pane toReturn = FXMLLoader.load(getClass().getResource("CommentPaneTemplate.fxml"));
    List<Node> tempChildren = new ArrayList<>(toReturn.getChildren());
    toReturn.getChildren().clear();

    Label authorLabel = (Label) UiUtils.getNodeFromId(tempChildren, "authorLabel");
    if (authorLabel != null) {
      authorLabel.setText(comment.getAuthor().getUsername());
    }
    TextArea commentTextArea = (TextArea) UiUtils.getNodeFromId(tempChildren, "commentTextArea");
    if (commentTextArea != null) {
      commentTextArea.setFont(new Font(15));
      commentTextArea.setText(comment.getText());
    }

    Label likeLabel = (Label) UiUtils.getNodeFromId(tempChildren, "likeCommentLabel");
    if (likeLabel != null) {
      likeLabel.setText(comment.getLikes() + " likes");
    }

    Label dislikeLabel = (Label) UiUtils.getNodeFromId(tempChildren, "dislikeCommentLabel");
    if (dislikeLabel != null) {
      dislikeLabel.setText(comment.getDislikes() + " dislikes");
    }

    Button likeButton = (Button) UiUtils.getNodeFromId(tempChildren, "likeCommentButton");
    if (likeButton != null) {
      likeButton.setOnAction(e -> {

        int prevLikes = comment.getLikes();

        try {
          boardAccess.likeComment(postId, comment.getId(), boardAccess.getActiveUser());
        } catch (Exception error) {
          UiUtils.exceptionAlert(error).showAndWait();
        }

        likeLabel.setText(comment.getLikes() + " likes");
        dislikeLabel.setText(comment.getDislikes() + " dislikes");
      });
    }

    Button dislikeButton = (Button) UiUtils.getNodeFromId(tempChildren, "dislikeCommentButton");
    if (dislikeButton != null) {
      dislikeButton.setOnAction(e -> {

        int prevDislikes = comment.getDislikes();

        try {
          boardAccess.dislikeComment(postId, comment.getId(), boardAccess.getActiveUser());
        } catch (Exception error) {
          UiUtils.exceptionAlert(error).showAndWait();
        }
        likeLabel.setText(comment.getLikes() + " likes");
        dislikeLabel.setText(comment.getDislikes() + " dislikes");
      });
    }

    toReturn.getChildren().addAll(
        authorLabel, commentTextArea, likeLabel, dislikeLabel, likeButton, dislikeButton);
    return toReturn;
  }

  private void publishComment(String postId) {

    try {
      String text = newCommentTextArea.getText();

      if (text.length() < 4) {
        UiUtils.popupAlert("Comment text must be longer than 3 characters").showAndWait();
        return;
      }

      PostComment comment;

      if (anonymousAuthorCheckBox.isSelected()) {
        comment = new PostComment(text);
      } else {
        comment = new PostComment(text, boardAccess.getActiveUser());
      }

      boardAccess.addComment(postId, comment);
      drawComments(postId);

    } catch (Exception e) {
      UiUtils.exceptionAlert(e).show();
    }
  }
}
