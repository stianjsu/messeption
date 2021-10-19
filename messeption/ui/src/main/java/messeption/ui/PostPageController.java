package messeption.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.json.JsonReadWrite;

/**
 * Javafx controller for viewing individual posts.
 */
public class PostPageController {

  private static final int SIZE_COMMENTS = 130;
  private static final int MARGIN_COMMENTS = 10;

  @FXML
  Label postTitleLabel;
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

  private ForumPost post;
  private ForumBoard forumBoard;

  public void initialize() {
    newCommentButton.setOnAction(e -> {
      publishComment(); });
  }

  /**
   * Takes an input post and displayes the post and its comments.

   * @param post The input post
   */
  public void setPost(ForumPost post) {
    this.post = post;
    generatePostContent();
    drawComments();

  }

  /**
   * Sets the controlelr forumboard to a specified input.

   * @param board the new controller board
   */
  public void setForumBoard(ForumBoard board) {
    this.forumBoard = board;
  }

  /**
   * Javafx help method to display comments in the UI.
   */
  public void drawComments() {

    List<PostComment> comments = post.getComments();

    this.commentsContainer.getChildren().clear();

    for (int indexId = 0; indexId < comments.size(); indexId++) {
      PostComment comment = comments.get(indexId);

      try {
        Pane commentPane = generateCommentPane(comment, indexId);
        commentPane.setLayoutY(
              (MARGIN_COMMENTS + SIZE_COMMENTS) * indexId + MARGIN_COMMENTS);

        this.commentsContainer.getChildren().add(commentPane);

      } catch (IOException e) {
        e.printStackTrace();
      }

    }
    commentsContainer.setPrefHeight(
        (MARGIN_COMMENTS + SIZE_COMMENTS) * comments.size() + MARGIN_COMMENTS);
  }

  private void generatePostContent() {
    postTitleLabel.setText(post.getTitle());

    postTextArea.setText(post.getText());
    postTextArea.setDisable(true);
    postTextArea.setStyle("-fx-opacity: 1;");

    postLikeLabel.setText(this.post.getLikes() + " likes");
    postDislikeLabel.setText(this.post.getDislikes() + " dislikes");

    postLikeButton.setOnAction(e -> {
      int prevLikes = post.getLikes();

      try {
        this.post.incrementLikes();
        JsonReadWrite.fileWrite(forumBoard);
        
      } catch (IOException error) {
        this.post.setLikes(prevLikes);
      }

      postLikeLabel.setText(post.getLikes() + " likes");
    });

    postDislikeButton.setOnAction(e -> {
      int prevDislikes = this.post.getDislikes();

      try {
        this.post.incrementDislikes();
        JsonReadWrite.fileWrite(forumBoard);
      } catch (IOException error) {
        this.post.setDislikes(prevDislikes);
      }

      postDislikeLabel.setText(this.post.getDislikes() + " dislikes");
    });
  }

  private Pane generateCommentPane(PostComment comment, int indexId) throws IOException {
    Pane toReturn = FXMLLoader.load(getClass().getResource("CommentPaneTemplate.fxml"));
    List<Node> tempChildren = new ArrayList<>(toReturn.getChildren());
    toReturn.getChildren().clear();

    TextArea commentTextArea = (TextArea) getNodeFromId(tempChildren, "commentTextArea");
    if (commentTextArea != null) {
      commentTextArea.setFont(new Font(15));
      commentTextArea.setText(comment.getText());
    }

    Label likeLabel = (Label) getNodeFromId(tempChildren, "likeLabel");
    if (likeLabel != null) {
      likeLabel.setText(comment.getLikes() + " likes");
    }

    Label dislikeLabel = (Label) getNodeFromId(tempChildren, "dislikeLabel");
    if (dislikeLabel != null) {
      dislikeLabel.setText(comment.getDislikes() + " dislikes");
    }

    Button likeButton = (Button) getNodeFromId(tempChildren, "likeButton");
    if (likeButton != null) {
      likeButton.setOnAction(e -> {
        // PostComment postToUpdate = post.getComments(); //comment to update
        int prevLikes = comment.getLikes();

        try {
          comment.incrementLikes();
          JsonReadWrite.fileWrite(forumBoard);
        } catch (IOException error) {
          comment.setLikes(prevLikes);
        }

        likeLabel.setText(comment.getLikes() + " likes");
      });
    }

    Button dislikeButton = (Button) getNodeFromId(tempChildren, "dislikeButton");
    if (dislikeButton != null) {
      dislikeButton.setOnAction(e -> {

        int prevDislikes = comment.getDislikes();

        try {
          comment.incrementDislikes();
          JsonReadWrite.fileWrite(forumBoard);
        } catch (IOException error) {
          comment.setDislikes(prevDislikes);
        }

        dislikeLabel.setText(comment.getDislikes() + " dislikes");
      });
    }

    toReturn.getChildren().addAll(commentTextArea, likeLabel, dislikeLabel, 
        likeButton, dislikeButton);
    return toReturn;
  }

  private Node getNodeFromId(List<Node> children, String id) {
    for (Node child : children) {
      if (child.getId() != null && child.getId().equals(id)) {
        return child;
      }
    }
    return null;
  }

  private void publishComment() {

    try {
      String text = newCommentTextArea.getText();

      if (text.length() < 3) {
        // error
        return;
      }

      PostComment comment = new PostComment(text);
      post.addComment(comment);

      JsonReadWrite.fileWrite(forumBoard);
      drawComments();

    } catch (IOException e) {
      // TODO ERROR
      e.printStackTrace();
    }
  }
}
