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
import messeption.core.JsonReadWrite;

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

  private PostCommentsController postCommentsController;
  private Scene postCommentsScene;

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

  public void setPostControllerController(PostCommentsController controller) {
    postCommentsController = controller;
  }

  public void setPostCommentsScene(Scene scene) {
    postCommentsScene = scene;
  }

  /**
   * Writes the current forumBoard state to file. Shows an alert if IOException.
   */
  public void writeBoard() {
    try {
      this.forumBoard.savePosts();
    } catch (IOException e) {
      Alert alert = exceptionAlert(e);
      alert.show();
    }
  }

  /**
   * Draws the posts in the UI and makes them visible.
   * 
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

    Pane toReturn = FXMLLoader.load(getClass().getResource("PaneTemplate.fxml"));
    List<Node> tempChildren = new ArrayList<>(toReturn.getChildren());
    toReturn.getChildren().clear();

    Label titleLabel = (Label) getNodeFromId(tempChildren, "titleLabel");
    TextArea postTextArea = (TextArea) getNodeFromId(tempChildren, "postTextArea");
    Line titleLine = (Line) getNodeFromId(tempChildren, "titleLine");

    Label likeLabel = (Label) getNodeFromId(tempChildren, "likeLabel");
    Label dislikeLabel = (Label) getNodeFromId(tempChildren, "dislikeLabel");
    Label replyLabel = (Label) getNodeFromId(tempChildren, "replyLabel");

    Button likeButton = (Button) getNodeFromId(tempChildren, "likeButton");
    Button dislikeButton = (Button) getNodeFromId(tempChildren, "dislikeButton");
    Button threadButton = (Button) getNodeFromId(tempChildren, "threadButton");

    if (titleLabel != null) {
      titleLabel.setText(post.getTitle());
    }
    if (postTextArea != null) {
      postTextArea.setText(post.getText());
      postTextArea.setDisable(true);
      postTextArea.setStyle("-fx-opacity: 1;");
    }
    if (replyLabel != null) {
      replyLabel.setText(post.getComments().size() + " comments");
    }
    if (likeLabel != null) {
      likeLabel.setText(post.getLikes() + " likes");
    }
    if (dislikeLabel != null) {
      dislikeLabel.setText(post.getDislikes() + " dislikes");
    }
    if (likeButton != null) {
      likeButton.setOnAction(e -> {
        int prevLikes = post.getLikes();

        try {
          post.incrementLikes();
          forumBoard.savePosts();
        } catch (IOException error) {
          post.setLikes(prevLikes);
        }

        likeLabel.setText(post.getLikes() + " likes");
      });
    }
    if (dislikeButton != null) {
      dislikeButton.setOnAction(e -> {
        int prevDislikes = post.getDislikes();

        try {
          post.incrementDislikes();
          forumBoard.savePosts();
        } catch (IOException error) {
          post.setDislikes(prevDislikes);
        }

        dislikeLabel.setText(post.getDislikes() + " dislikes");
      });
    }

    if (threadButton != null) {
      threadButton.setOnAction(e -> {
        primaryStage = (Stage) createPostButton.getScene().getWindow();
        primaryStage.setScene(postCommentsScene);
        postCommentsController.setForumBoard(this.getBoard());
        postCommentsController.setPost(post);
      });
    }

    toReturn.getChildren().addAll(new ArrayList<Node>(Arrays.asList(titleLabel, titleLine, postTextArea, likeLabel,
        dislikeLabel, replyLabel, likeButton, dislikeButton, threadButton)));
    return toReturn;
  }

  /**
   * Finds a node in a list of nodes from an ID.
   * 
   * @param children the list of nodes to look in
   * @param id       the ID to look for
   * @return the node with the matching ID, if none return null
   */
  public Node getNodeFromId(List<Node> children, String id) {
    for (Node child : children) {
      if (child.getId() != null && child.getId().equals(id)) {
        return child;
      }
    }
    return null;
  }

  /**
   * If an exception is raised it is here processed into an alert for the UI.
   * 
   * @param e the exception to be processed
   * @return the finished Alert
   */
  public Alert exceptionAlert(Exception e) {

    Alert toReturn = new Alert(AlertType.ERROR);
    toReturn.setContentText(e.toString() + "\n" + e.getCause());
    System.err.println(e.toString() + "\n" + e.getCause());
    toReturn.setTitle("Error");
    return toReturn;
  }
}
