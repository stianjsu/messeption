package messeption.ui;

import java.io.IOException;
import java.util.List;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import messeption.core.ForumPost;

/**
 * Controller for the front page or main menu of the app.
 */
public class FrontPageController extends SceneController {

  private static final int SIZE_POSTS = 220;
  private static final int MARIGIN_TOP = 15;
  private static final int MARIGIN_POSTS = 30;

  @FXML
  MenuButton sortMenuButton;
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
  ScrollPane scrollPane;
  @FXML
  AnchorPane postsContainer;
  @FXML
  Button createPostButton;

  private PostPageController postPageController;

  /**
   * Sets the onAction events of sortingButtons.

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
    
    this.createPostButton.setOnAction(event -> {
      primaryStage.setScene(createPostPageScene);
    });
  }

  @Override
  public void setBoardAccess(BoardAccessInterface boardAccess) throws Exception {
    this.boardAccess = boardAccess;
    drawPosts();
  }
  
  public void setPostPageController(PostPageController postPageController) {
    this.postPageController = postPageController;
  }

  public void sortPosts(String sortBy) {
    
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
    sortMenuButton.setText("Time");
  }

  /**
   * Draws the posts in the UI and makes them visible.

   * @throws Exception If board cannot read form file
   */
  public void drawPosts(List<ForumPost> posts) throws Exception {

    scrollPane.setVvalue(0);
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

    FXMLLoader postPaneTemplateLoader = new FXMLLoader(getClass().getResource(
          "PostPaneTemplate.fxml"));
      
    postPaneTemplateLoader.load();
    PostPaneTemplateController postPaneTemplateController = postPaneTemplateLoader.getController();
    postPaneTemplateController.setFields(
          this.primaryStage, this.postPageScene, this.postPageController, this, this.boardAccess);
    
    return postPaneTemplateController.setFieldsPostPane(post);

  }
}
