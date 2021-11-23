package messeption.ui;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import messeption.core.ForumPost;

/**
 * Template controller for reducingredundant construction of panes in PostPage.
 */
public class PostPaneTemplateController {

  @FXML
  Pane postPaneTemplate;
  @FXML
  Label titleLabel;
  @FXML
  Label authorLabel;
  @FXML
  Label timeStampLabel;
  @FXML 
  Line titleLine;
  @FXML
  TextArea postTextArea;
  @FXML
  Label replyLabel;
  @FXML
  Label likeLabel;
  @FXML
  Label dislikeLabel;
  @FXML
  Button likeButton;
  @FXML
  Button dislikeButton;
  @FXML
  Button threadButton;
  @FXML
  Button deleteButton;

  private BoardAccessInterface boardAccess;

  private Stage primaryStage;
  private Scene postPageScene;
  private PostPageController postPageController;
  private FrontPageController frontPageController;

  public void setPostPageScene(Scene postPageScene) {
    this.postPageScene = postPageScene;
  }

  public void setPrimaryStage(Stage primaryStage) {
    this.primaryStage = primaryStage;
  }

  public void setPostPageController(PostPageController postPageController) {
    this.postPageController = postPageController;
  }

  public void setFrontPageController(FrontPageController frontPageController) {
    this.frontPageController = frontPageController;
  }

  public void setBoardAccess(BoardAccessInterface boardAccess) {
    this.boardAccess = boardAccess;
  }

  /**
   * A method that calls the setters for multiple fields.

   * @param primaryStage the stage to set Scene to
   * @param postPageScene the PostPage Scene
   * @param postPageController the controller for PostPage
   * @param frontPageController the controller for FrontPage
   * @param boardAccess boardAccessInterface
   */
  public void setFields(Stage primaryStage, Scene postPageScene, 
      PostPageController postPageController, FrontPageController frontPageController, 
      BoardAccessInterface boardAccess) {
    setPrimaryStage(primaryStage);
    setPostPageScene(postPageScene);
    setPostPageController(postPageController);
    setFrontPageController(frontPageController);
    setBoardAccess(boardAccess);
  }
  
  public void initialize() {}

  /**
   * Fills the FXML nodes with valid information for the given post.
   * Title, Text, Author, Timestamp is shown and OnAction events are set for the buttons.

   * @param post The Forumpost to be displayed
   */
  public Pane setFieldsPostPane(ForumPost post) {

    titleLabel.setText(post.getTitle());

    authorLabel.setText("Post by: " + (post.isAnonymous()
          ? ForumPost.ANONYMOUS_NAME : post.getAuthor().getUsername()));

    postTextArea.setText(post.getText());
    postTextArea.setStyle("-fx-opacity: 1;");

    replyLabel.setText(post.getComments().size() + " comments");

    likeLabel.setText(post.getLikes() + " likes");

    dislikeLabel.setText(post.getDislikes() + " dislikes");


    UiUtils.setStyleOfButton(likeButton,
        post.getLikeUsers().contains(boardAccess.getActiveUser()));
    UiUtils.setStyleOfButton(dislikeButton,
        post.getDislikeUsers().contains(boardAccess.getActiveUser()));

    likeButton.setOnAction(e -> {
      try {
        boardAccess.likePost(post.getId(), boardAccess.getActiveUser());
      } catch (Exception error) {
        UiUtils.popupAlert(error, "Something went wrong with liking a post").showAndWait();
      }
      
      UiUtils.setStyleOfButton(likeButton,
          post.getLikeUsers().contains(boardAccess.getActiveUser()));
      UiUtils.setStyleOfButton(dislikeButton,
          post.getDislikeUsers().contains(boardAccess.getActiveUser()));

      likeLabel.setText(post.getLikes() + " likes");
      dislikeLabel.setText(post.getDislikes() + " dislikes");
    });

    dislikeButton.setOnAction(e -> {
      try {
        boardAccess.dislikePost(post.getId(), boardAccess.getActiveUser());
      } catch (Exception error) {
        UiUtils.popupAlert(error, "Something went wrong with disliking a post").showAndWait();
      }
      UiUtils.setStyleOfButton(likeButton,
          post.getLikeUsers().contains(boardAccess.getActiveUser()));
      UiUtils.setStyleOfButton(dislikeButton,
          post.getDislikeUsers().contains(boardAccess.getActiveUser()));

      dislikeLabel.setText(post.getDislikes() + " dislikes");
      likeLabel.setText(post.getLikes() + " likes");
    });

    threadButton.setOnAction(e -> {
      primaryStage.setScene(postPageScene);
      postPageController.drawComments(post.getId());
      postPageController.reloadPage();
    });

    timeStampLabel.setText(post.getTimeStamp().toString());

    if (boardAccess.getActiveUser() != null 
          && boardAccess.getActiveUser().equals(post.getAuthor())) {
      deleteButton.setOnAction(e -> {
        try {
          boolean confirmation = UiUtils.confimationAlert("Confirm deletion",
              "Delete post: " + post.getTitle(), "Are you sure you want to delete your post?");

          if (confirmation) {
            boardAccess.deletePost(post.getId());
            frontPageController.sortPosts(frontPageController.sortMenuButton.getText());
          }
        } catch (Exception e1) {
          UiUtils.popupAlert(e1, "Something went wrong with deleting a post").showAndWait();
        }
      });

      deleteButton.setDisable(false);
      deleteButton.setVisible(true);
      
    }
    return this.postPaneTemplate;
  }
}
