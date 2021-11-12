package messeption.ui;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import messeption.core.ForumPost;

/**
 * Controller for the front page or main menu of the app.
 */
public class FrontPageController {

  private static final int SIZE_POSTS = 220;
  private static final int MARIGIN_TOP = 15;
  private static final int MARIGIN_POSTS = 30;

  @FXML
  MenuButton sortMenuButton;
  @FXML
  MenuItem menuQuit;
  @FXML
  MenuItem menuLogOut;
  @FXML
  MenuItem menuAbout;
  @FXML
  MenuItem sortTime;
  @FXML
  MenuItem sortTitle;
  @FXML
  MenuItem sortAuthor;
  @FXML
  MenuItem sortTextLength;
  @FXML
  MenuItem sortCommentCount;
  @FXML
  AnchorPane postsContainer;
  @FXML
  Button createPostButton;

  private Stage primaryStage;
  private BoardAccessInterface boardAccess;

  private PostPageController postPageController;
  private Scene postPageScene;

  /**
   * Sets the onAction events of sortingButtons.
   * 

   * @throws Exception If board cannot read form file
   */
  public void initialize() throws Exception {
    sortTime.setOnAction((e) -> {
      sortPosts(sortTime.getText());
    });
    sortTitle.setOnAction((e) -> {
      sortPosts(sortTitle.getText());
    });
    sortAuthor.setOnAction((e) -> {
      sortPosts(sortAuthor.getText());
    });
    sortTextLength.setOnAction((e) -> {
      sortPosts(sortTextLength.getText());
    });
    sortCommentCount.setOnAction((e) -> {
      sortPosts(sortCommentCount.getText());
    });
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

  private void sortPosts(String sortBy) {
    
    try {
      List<ForumPost> posts = boardAccess.getPosts();
      posts.sort(UiUtils.getPostSorting(sortBy));
      drawPosts(posts);
      sortMenuButton.setText(sortBy);
    } catch (Exception e) {
      UiUtils.exceptionAlert(e).show();
    }
    
  }

  /**
   * Draws the posts sorted by time by default in the UI and makes them visible.

   * @throws Exception If board cannot read form file
   */
  public void drawPosts() throws Exception {
    sortPosts("Time");
  }
  /**
   * Draws the posts in the UI and makes them visible.

   * @throws Exception If board cannot read form file
   */
  public void drawPosts(List<ForumPost> posts) throws Exception {

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
      authorLabel.setText("Post by: " + (post.isAnonymous()
          ? ForumPost.ANONYMOUS_NAME : post.getAuthor().getUsername()));
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

    Label timeStampLabel = (Label) UiUtils.getNodeFromId(tempChildren, "timeStampLabel");
    if (timeStampLabel != null) {
      timeStampLabel.setText(post.getTimeStamp());
    }

    toReturn.getChildren().addAll(new ArrayList<Node>(Arrays.asList(titleLabel, authorLabel, 
          titleLine, postTextArea, likeLabel, dislikeLabel, 
          replyLabel, likeButton, dislikeButton, threadButton, timeStampLabel)));

    if (boardAccess.getActiveUser() != null 
          && boardAccess.getActiveUser().equals(post.getAuthor())) {
      Button deleteButton = (Button) UiUtils.getNodeFromId(tempChildren, "deleteButton");
      if (deleteButton != null) {
        deleteButton.setOnAction(e -> {
          try {
            boolean confirmation = UiUtils.confimationAlert("Confirm deletion",
                "Delete post: " + post.getTitle(), "Are you sure you want to delete your post?");

            if (confirmation) {
              boardAccess.deletePost(post.getId());
              sortPosts(sortMenuButton.getText());
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
}
