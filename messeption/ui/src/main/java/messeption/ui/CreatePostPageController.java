package messeption.ui;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import messeption.core.ForumPost;

/**
 * Controller for the create post page.
 */
public class CreatePostPageController extends SceneController {
  
  @FXML
  Button logOutButton;
  @FXML
  MenuItem menuQuit;
  @FXML
  MenuItem menuLogOut;
  @FXML
  MenuItem menuAbout;
  @FXML
  Button publishButton;
  @FXML
  Button cancelButton;
  @FXML
  TextField postTitleField;
  @FXML
  TextArea postTextArea;
  @FXML
  CheckBox anonymousAuthorCheckBox;
  @FXML
  Label titleFeedbackLabel;
  @FXML
  Label textFeedbackLabel;
  private String titleFeedback; 
  private String textFeedback; 


  /**
   * initializer for the scene.
   */
  public void initialize() {

    cancelButton.setOnAction(event -> {
      this.reloadPage();
      primaryStage.setScene(frontPageScene);
      try {
        frontPageController.drawPosts();
      } catch (Exception e) {
        UiUtils.exceptionAlert(e).show();
      }
    });

    publishButton.setOnAction(e -> {
      createPostInBoard();
    });
    postTitleField.setOnKeyTyped(e -> {
      if (postTitleField.getText().length() < 4) {
        titleFeedbackLabel.setText(titleFeedback);
      } else {
        titleFeedbackLabel.setText("");
      }
      updateButtonEnabled();
    });
    postTextArea.setOnKeyTyped(e -> {
      if (postTextArea.getText().length() < 4) {
        textFeedbackLabel.setText(textFeedback);
      } else {
        textFeedbackLabel.setText("");
      }
      updateButtonEnabled();
    });
    this.titleFeedback = titleFeedbackLabel.getText();
    this.textFeedback = textFeedbackLabel.getText();
    titleFeedbackLabel.setText("");
    textFeedbackLabel.setText("");
  }

  /**
   * Gets the text and title form the text input fields.
   * Then checks if they are long enough adds a new post object
   */
  @FXML
  public void createPostInBoard() {

    try {
      String title = postTitleField.getText();
      String text = postTextArea.getText();

      if (title.length() < 3) {
        UiUtils.popupAlert("Post title is too short").showAndWait();
        return;
      }
      if (text.length() < 3) {
        UiUtils.popupAlert("Post text is too short").showAndWait();
        return;
      }

      // updates and saves board
      boardAccess.addPost(new ForumPost(title, text, boardAccess.getActiveUser(), 
          anonymousAuthorCheckBox.isSelected()));

      // confirmCreation
      feedbackAlertPostCreation(title);
    } catch (Exception e) {
      UiUtils.exceptionAlert(e);
    }
  }

  /**
   * Creates and shows an alert on screen when the user successfully creates a
   * post.

   * @param title The title name of the alert
   */
  public void feedbackAlertPostCreation(String title) {
    Alert confirmation = new Alert(AlertType.INFORMATION);

    ButtonType quitToFrontPage = new ButtonType("Quit To Front Page", ButtonData.CANCEL_CLOSE);
    ButtonType newPost = new ButtonType("Create another post");

    confirmation.getButtonTypes().setAll(newPost, quitToFrontPage);

    confirmation.setTitle("Post Created!");
    confirmation.setHeaderText("Your post " + title + " have been created successfully.");
    confirmation.setContentText("You can either create another post or go back to the front page");

    Optional<ButtonType> result = confirmation.showAndWait();

    reloadPage();
    if (result.get() == quitToFrontPage) {
      cancelButton.fire(); // go back to main menu
    } 
  }

  private void updateButtonEnabled() {
    publishButton.setDisable(postTitleField.getText().length() < 4
        || postTextArea.getText().length() < 4);
  }

  /**
   * Method for refreshing the page with empty input fields.
   */
  @FXML
  public void reloadPage() {
    postTextArea.setText("");
    postTitleField.setText("");
    publishButton.setDisable(true);
    titleFeedbackLabel.setText("");
    textFeedbackLabel.setText("");
  }

}
