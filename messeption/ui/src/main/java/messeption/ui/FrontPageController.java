package messeption.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;

/**
 * Controller for the front page or main menu of the app.
 */
public class FrontPageController {

  private static final int SIZE_POSTS = 220;
  private static final int MARIGIN_TOP = 15;
  private static final int MARIGIN_POSTS = 30;

  @FXML
  AnchorPane postsContainer;
  @FXML
  Button createPostButton;

  private Stage primaryStage;
  private BoardAccessInterface boardAccess;

  private PostPageController postPageController;
  private Scene postPageScene;

  public void initialize() throws Exception {
  }

  public void setBoardAccess(BoardAccessInterface boardAccess) throws Exception {
    this.boardAccess = boardAccess;
    drawPosts();
  }
  
  public void setPostPageController(PostPageController controller) {
    this.postPageController = controller;
  }
  
  public void setPostCommentsScene(Scene scene) {
    postPageScene = scene;
  }

  /**
   * Draws the posts in the UI and makes them visible.

   * @throws Exception If board cannot read form file
   */
  public void drawPosts() throws Exception {
    
    List<ForumPost> posts = boardAccess.getPosts();

    postsContainer.getChildren().clear();

    int indexId = 0;
    for (ForumPost post : posts) {

      Pane pane = generatePostPane(post);
      pane.setLayoutY(MARIGIN_TOP + indexId * (SIZE_POSTS + MARIGIN_POSTS));
      postsContainer.getChildren().add(pane);

      indexId++;
    }
    postsContainer.setPrefHeight(indexId * (SIZE_POSTS + MARIGIN_POSTS));
  }

  private Pane generatePostPane(ForumPost post) throws IOException {

    Pane toReturn = FXMLLoader.load(getClass().getResource("PostPaneTemplate.fxml"));
    List<Node> tempChildren = new ArrayList<>(toReturn.getChildren());
    toReturn.getChildren().clear();

    Label titleLabel = (Label) UiUtils.getNodeFromId(tempChildren, "titleLabel");
    if (titleLabel != null) {
      titleLabel.setText(post.getTitle());
    }
    Label authorLabel = (Label) UiUtils.getNodeFromId(tempChildren, "authorLabel");
    if (titleLabel != null) {
      authorLabel.setText("Post by: " + post.getAuthor().getUsername());
    }

    TextArea postTextArea = (TextArea) UiUtils.getNodeFromId(tempChildren, "postTextArea");
    if (postTextArea != null) {
      postTextArea.setText(post.getText());
      postTextArea.setDisable(true);
      postTextArea.setStyle("-fx-opacity: 1;");
    }

    Label replyLabel = (Label) UiUtils.getNodeFromId(tempChildren, "replyLabel");
    if (replyLabel != null) {
      replyLabel.setText(post.getComments().size() + " comments");
    }

    Label likeLabel = (Label) UiUtils.getNodeFromId(tempChildren, "likeLabel");
    if (likeLabel != null) {
      likeLabel.setText(post.getLikes() + " likes");
    }

    Label dislikeLabel = (Label) UiUtils.getNodeFromId(tempChildren, "dislikeLabel");
    if (dislikeLabel != null) {
      dislikeLabel.setText(post.getDislikes() + " dislikes");
    }

    Button likeButton = (Button) UiUtils.getNodeFromId(tempChildren, "likeButton");
    Button dislikeButton = (Button) UiUtils.getNodeFromId(tempChildren, "dislikeButton");
    if (likeButton != null && dislikeButton != null) {

      UiUtils.setStyleOfButton(likeButton,
          post.getLikeUsers().contains(boardAccess.getActiveUser()));
      UiUtils.setStyleOfButton(dislikeButton,
          post.getDislikeUsers().contains(boardAccess.getActiveUser()));
          
      likeButton.setOnAction(e -> {
        int prevLikes = post.getLikes();

        try {
          boardAccess.likePost(post.getId(), boardAccess.getActiveUser());
        } catch (Exception error) {
          System.out.println("Klarte ikke like");
          UiUtils.exceptionAlert(error).showAndWait();
        }
        
        UiUtils.setStyleOfButton(likeButton,
            post.getLikeUsers().contains(boardAccess.getActiveUser()));
        UiUtils.setStyleOfButton(dislikeButton,
            post.getDislikeUsers().contains(boardAccess.getActiveUser()));

        likeLabel.setText(post.getLikes() + " likes");
        dislikeLabel.setText(post.getDislikes() + " dislikes");
      });

      dislikeButton.setOnAction(e -> {
        int prevDislikes = post.getDislikes();

        try {
          boardAccess.dislikePost(post.getId(), boardAccess.getActiveUser());
        } catch (Exception error) {
          UiUtils.exceptionAlert(error).showAndWait();
        }
        UiUtils.setStyleOfButton(likeButton,
            post.getLikeUsers().contains(boardAccess.getActiveUser()));
        UiUtils.setStyleOfButton(dislikeButton,
            post.getDislikeUsers().contains(boardAccess.getActiveUser()));

        dislikeLabel.setText(post.getDislikes() + " dislikes");
        likeLabel.setText(post.getLikes() + " likes");
      });
    }

    Button threadButton = (Button) UiUtils.getNodeFromId(tempChildren, "threadButton");
    if (threadButton != null) {
      threadButton.setOnAction(e -> {
        primaryStage = (Stage) createPostButton.getScene().getWindow();
        primaryStage.setScene(postPageScene);
        postPageController.drawComments(post.getId());
      });
    }

    Line titleLine = (Line) UiUtils.getNodeFromId(tempChildren, "titleLine");

    toReturn.getChildren().addAll(new ArrayList<Node>(Arrays.asList(titleLabel, authorLabel, 
          titleLine, postTextArea, likeLabel, dislikeLabel, 
          replyLabel, likeButton, dislikeButton, threadButton)));
    return toReturn;
  }

}
