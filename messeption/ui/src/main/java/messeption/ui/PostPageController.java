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
import javafx.scene.control.MenuItem;
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
  MenuItem menuQuit;
  @FXML
  MenuItem menuLogOut;
  @FXML
  MenuItem menuAbout;
  
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
    postAuthorLabel.setText("Post by: " + (post.isAnonymous()
        ? ForumPost.ANONYMOUS_NAME : post.getAuthor().getUsername()));

    
    postTimeStampLabel.setText(post.getTimeStamp());

    postTextArea.setText(post.getText());
    postTextArea.setDisable(true);
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
        UiUtils.exceptionAlert(error).showAndWait();
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
        UiUtils.exceptionAlert(error).showAndWait();
      }
      UiUtils.setStyleOfButton(postLikeButton,
            post.getLikeUsers().contains(boardAccess.getActiveUser()));
      UiUtils.setStyleOfButton(postDislikeButton,
            post.getDislikeUsers().contains(boardAccess.getActiveUser()));

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
      authorLabel.setText(comment.isAnonymous()
          ? PostComment.ANONYMOUS_NAME : comment.getAuthor().getUsername());
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
    Button dislikeButton = (Button) UiUtils.getNodeFromId(tempChildren, "dislikeCommentButton");
    if (likeButton != null && dislikeButton != null) {
      UiUtils.setStyleOfButton(likeButton,
            comment.getLikeUsers().contains(boardAccess.getActiveUser()));
      UiUtils.setStyleOfButton(dislikeButton,
            comment.getDislikeUsers().contains(boardAccess.getActiveUser()));

      likeButton.setOnAction(e -> {

        try {
          boardAccess.likeComment(postId, comment.getId(), boardAccess.getActiveUser());
        } catch (Exception error) {
          UiUtils.exceptionAlert(error).showAndWait();
        }
        UiUtils.setStyleOfButton(likeButton,
            comment.getLikeUsers().contains(boardAccess.getActiveUser()));
        UiUtils.setStyleOfButton(dislikeButton,
            comment.getDislikeUsers().contains(boardAccess.getActiveUser()));

        likeLabel.setText(comment.getLikes() + " likes");
        dislikeLabel.setText(comment.getDislikes() + " dislikes");
      });
    
      dislikeButton.setOnAction(e -> {
        try {
          boardAccess.dislikeComment(postId, comment.getId(), boardAccess.getActiveUser());
        } catch (Exception error) {
          UiUtils.exceptionAlert(error).showAndWait();
        }
        UiUtils.setStyleOfButton(likeButton,
            comment.getLikeUsers().contains(boardAccess.getActiveUser()));
        UiUtils.setStyleOfButton(dislikeButton,
            comment.getDislikeUsers().contains(boardAccess.getActiveUser()));

        likeLabel.setText(comment.getLikes() + " likes");
        dislikeLabel.setText(comment.getDislikes() + " dislikes");
      });
    }

    Label timeStampLabel = (Label) UiUtils.getNodeFromId(tempChildren, "commentTimeStampLabel");
    if (timeStampLabel != null) {
      timeStampLabel.setText(comment.getTimeStamp());
    }

    toReturn.getChildren().addAll(authorLabel, commentTextArea, 
        likeLabel, dislikeLabel, likeButton, dislikeButton, timeStampLabel);

    if (boardAccess.getActiveUser() != null 
        && boardAccess.getActiveUser().equals(comment.getAuthor())) {
      Button deleteButton = (Button) UiUtils.getNodeFromId(tempChildren, "deleteButton");
      if (deleteButton != null) {
        deleteButton.setOnAction(e -> {
          try {
            boolean confirmation = UiUtils.confimationAlert("Confirm deletion",
                "Delete comment on post: " + boardAccess.getPost(postId).getTitle(),
                "Are you sure you want to delete your comment?");
                
            if (confirmation) {
              boardAccess.deleteComment(postId, comment.getId());
              drawComments(postId);
            }
          } catch (Exception e1) {
            UiUtils.exceptionAlert(e1);
          }
        });
        toReturn.getChildren().add(deleteButton);
      }
    }
    return toReturn;
  }

  private void publishComment(String postId) {

    try {
      String text = newCommentTextArea.getText();

      if (text.length() < 4) {
        UiUtils.popupAlert("Comment text must be longer than 3 characters").showAndWait();
        return;
      }

      boardAccess.addComment(postId, new PostComment(text, boardAccess.getActiveUser(), 
          anonymousAuthorCheckBox.isSelected()));
      drawComments(postId);
      newCommentTextArea.clear();

    } catch (Exception e) {
      UiUtils.exceptionAlert(e).show();
    }
  }
}
