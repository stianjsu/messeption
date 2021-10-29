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

  private ForumPost post;
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
  public void drawComments(ForumPost post, int postIndex) {
    newCommentButton.setOnAction(e -> {
      publishComment(postIndex);
    });


    this.post = post;
    generatePostContent(post, postIndex);
    List<PostComment> comments = post.getComments();

    this.commentsContainer.getChildren().clear();

    for (int indexId = 0; indexId < comments.size(); indexId++) {
      PostComment comment = comments.get(indexId);

      try {
        Pane commentPane = generateCommentPane(comment, postIndex, indexId);
        commentPane.setLayoutY((MARGIN_COMMENTS + SIZE_COMMENTS) * indexId + MARGIN_COMMENTS);

        this.commentsContainer.getChildren().add(commentPane);

      } catch (Exception error) {
        UiUtils.exceptionAlert(error).showAndWait();
      }

    }
    commentsContainer.setPrefHeight(
        (MARGIN_COMMENTS + SIZE_COMMENTS) * comments.size() + MARGIN_COMMENTS);
  }

  private void generatePostContent(ForumPost post, int indexId) {
    postTitleLabel.setText(post.getTitle());
    postAuthorLabel.setText("Post by: " + post.getAuthor());

    postTextArea.setText(post.getText());
    postTextArea.setDisable(true);
    postTextArea.setStyle("-fx-opacity: 1;");

    postLikeLabel.setText(this.post.getLikes() + " likes");
    postDislikeLabel.setText(this.post.getDislikes() + " dislikes");

    postLikeButton.setOnAction(e -> {
      int prevLikes = post.getLikes();

      try {
        boardAccess.likePost(indexId);
        post.incrementLikes();
      } catch (Exception error) {
        UiUtils.exceptionAlert(error).showAndWait();
        this.post.setLikes(prevLikes);
      }

      postLikeLabel.setText(post.getLikes() + " likes");
    });

    postDislikeButton.setOnAction(e -> {
      int prevDislikes = this.post.getDislikes();

      try {
        boardAccess.dislikePost(indexId);
        post.incrementDislikes();
      } catch (Exception error) {
        UiUtils.exceptionAlert(error).showAndWait();
        this.post.setDislikes(prevDislikes);
      }

      postDislikeLabel.setText(this.post.getDislikes() + " dislikes");
    });
  }

  private Pane generateCommentPane(PostComment comment, int postIndex, int commentIndex) 
      throws IOException {
    Pane toReturn = FXMLLoader.load(getClass().getResource("CommentPaneTemplate.fxml"));
    List<Node> tempChildren = new ArrayList<>(toReturn.getChildren());
    toReturn.getChildren().clear();

    Label authorLabel = (Label) UiUtils.getNodeFromId(tempChildren, "authorLabel");
    if (authorLabel != null) {
      authorLabel.setText(comment.getAuthor());
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
          boardAccess.likeComment(postIndex, commentIndex);
          comment.incrementLikes();
        } catch (Exception error) {
          UiUtils.exceptionAlert(error).showAndWait();
          comment.setLikes(prevLikes);
        }

        likeLabel.setText(comment.getLikes() + " likes");
      });
    }

    Button dislikeButton = (Button) UiUtils.getNodeFromId(tempChildren, "dislikeCommentButton");
    if (dislikeButton != null) {
      dislikeButton.setOnAction(e -> {

        int prevDislikes = comment.getDislikes();

        try {
          boardAccess.dislikeComment(postIndex, commentIndex);
          comment.incrementDislikes();
        } catch (Exception error) {
          UiUtils.exceptionAlert(error).showAndWait();
          comment.setDislikes(prevDislikes);
        }

        dislikeLabel.setText(comment.getDislikes() + " dislikes");
      });
    }

    toReturn.getChildren().addAll(
        authorLabel, commentTextArea, likeLabel, dislikeLabel, likeButton, dislikeButton);
    return toReturn;
  }

  private void publishComment(int postIndex) {

    try {
      String text = newCommentTextArea.getText();

      if (text.length() < 3) {
        return;
      }

      PostComment comment = new PostComment(text);
      if (!anonymousAuthorCheckBox.isSelected()) {
        // String username = staticclass.getActiveUsername()
        String username = "placeholder";
        comment.setAuthor(username);
      }

      boardAccess.addComment(postIndex, comment);
      drawComments(this.post, postIndex);

    } catch (Exception e) {
      UiUtils.exceptionAlert(e).show();
    }
  }
}
