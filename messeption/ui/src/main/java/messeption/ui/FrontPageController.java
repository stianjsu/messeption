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
import messeption.json.JsonReadWrite;

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
  private ForumBoard forumBoard;

  private PostPageController postPageController;
  private Scene postPageScene;

  public void initialize() throws IOException {
    drawPosts();
  }

  public ForumBoard getBoard() {
    return this.forumBoard;
  }

  public void setBoard(ForumBoard board) {
    this.forumBoard = board;
    writeBoard();
  }

  public void setPostPageController(PostPageController controller) {
    postPageController = controller;
  }

  public void setPostCommentsScene(Scene scene) {
    postPageScene = scene;
  }

  /**
   * Writes the current forumBoard state to file. Shows an alert if IOException.
   */
  public void writeBoard() {
    try {
      JsonReadWrite.fileWrite(forumBoard);
    } catch (IOException e) {
      Alert alert = JavafxUtils.exceptionAlert(e);
      alert.show();
    }
  }

  /**
   * Draws the posts in the UI and makes them visible.

   * @throws IOException If board cannot read form file
   */
  public void drawPosts() throws IOException {

    forumBoard = JsonReadWrite.fileRead();
    List<ForumPost> posts = forumBoard.getPosts();

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

    Label titleLabel = (Label) JavafxUtils.getNodeFromId(tempChildren, "titleLabel");
    if (titleLabel != null) {
      titleLabel.setText(post.getTitle());
    }

    TextArea postTextArea = (TextArea) JavafxUtils.getNodeFromId(tempChildren, "postTextArea");
    if (postTextArea != null) {
      postTextArea.setText(post.getText());
      postTextArea.setDisable(true);
      postTextArea.setStyle("-fx-opacity: 1;");
    }

    Label replyLabel = (Label) JavafxUtils.getNodeFromId(tempChildren, "replyLabel");
    if (replyLabel != null) {
      replyLabel.setText(post.getComments().size() + " comments");
    }

    Label likeLabel = (Label) JavafxUtils.getNodeFromId(tempChildren, "likeLabel");
    if (likeLabel != null) {
      likeLabel.setText(post.getLikes() + " likes");
    }

    Label dislikeLabel = (Label) JavafxUtils.getNodeFromId(tempChildren, "dislikeLabel");
    if (dislikeLabel != null) {
      dislikeLabel.setText(post.getDislikes() + " dislikes");
    }

    Button likeButton = (Button) JavafxUtils.getNodeFromId(tempChildren, "likeButton");
    if (likeButton != null) {
      likeButton.setOnAction(e -> {
        int prevLikes = post.getLikes();

        try {
          post.incrementLikes();
          JsonReadWrite.fileWrite(forumBoard);
        } catch (IOException error) {
          post.setLikes(prevLikes);
        }

        likeLabel.setText(post.getLikes() + " likes");
      });
    }

    Button dislikeButton = (Button) JavafxUtils.getNodeFromId(tempChildren, "dislikeButton");
    if (dislikeButton != null) {
      dislikeButton.setOnAction(e -> {
        int prevDislikes = post.getDislikes();

        try {
          post.incrementDislikes();
          JsonReadWrite.fileWrite(forumBoard);
        } catch (IOException error) {
          post.setDislikes(prevDislikes);
        }

        dislikeLabel.setText(post.getDislikes() + " dislikes");
      });
    }

    Button threadButton = (Button) JavafxUtils.getNodeFromId(tempChildren, "threadButton");
    if (threadButton != null) {
      threadButton.setOnAction(e -> {
        primaryStage = (Stage) createPostButton.getScene().getWindow();
        primaryStage.setScene(postPageScene);
        postPageController.setForumBoard(this.getBoard());
        postPageController.setPost(post);
      });
    }

    Line titleLine = (Line) JavafxUtils.getNodeFromId(tempChildren, "titleLine");
    
    toReturn.getChildren().addAll(new ArrayList<Node>(Arrays.asList(
        titleLabel, titleLine, postTextArea, likeLabel, dislikeLabel, 
        replyLabel, likeButton, dislikeButton, threadButton)));
    return toReturn;
  }

}
