package messeption.ui;

import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import messeption.core.User;

/**
 * Controller for the create post page.
 */
public class LoginPageController extends SceneController {

  
  @FXML
  private TextField loginUserTextField;
  @FXML
  private PasswordField loginPasswordField;
  @FXML
  private Button loginButton;

  @FXML
  private TextField signUpUserTextField;
  @FXML
  private PasswordField signUpPasswordField;
  @FXML
  private PasswordField signUpPasswordFieldCheck;
  @FXML
  private Button signupButton;

  /**
   * initializer for the scene.
   */
  public void initialize() {
    super.setNavBarButtons();
    loginButton.setOnAction((e) -> {
      login();

    });
    signupButton.setOnAction((e) -> {
      signUp();
    });
  }

  /**
   * Method is called when the user tries to login.
   */
  public void login() {
    String username = loginUserTextField.getText();
    if (!boardAccess.userNameExists(username)) {
      UiUtils.popupAlert("Can not find username\nDont have an account? Sign up :)").showAndWait();

    } else {
      String password = loginPasswordField.getText();
      if (!boardAccess.correctPassword(username, password)) {
        UiUtils.popupAlert("Wrong password for user " + username).showAndWait();
      } else {
        sucsessAlert(new User(username, password));
        clearAllInputFields();
      }
    }
  }

  /**
   * Method for checking valid signup.
   */
  public void signUp() {
    String username = signUpUserTextField.getText();
    String password = signUpPasswordField.getText();
    String password2 = signUpPasswordFieldCheck.getText();
    if (password.equals(password2)) {
      try {
        boardAccess.addUser(username, password);
        sucsessAlert(null);
        signUpUserTextField.clear();
        signUpPasswordField.clear();
        signUpPasswordFieldCheck.clear();
      } catch (IllegalArgumentException e) {
        String message = e.getMessage();
        String[] splitMessage = message.split(":");
        if (splitMessage.length > 1) { //removed error type from ui feedback while running remote
          message = splitMessage[1];
        }
        UiUtils.popupAlert(message).showAndWait(); 
      } catch (Exception e) {
        UiUtils.popupAlert(e, "Somthing went wrong when signing up new user").showAndWait();
      }
    } else {
      UiUtils.popupAlert("You did not type the same password twice").showAndWait();
    }
  }

  /**
  * Opens an alert when the login is successful.

  * @param user the user who logged in successfully
  */
  public void sucsessAlert(User user) {
    Alert confirmation = new Alert(AlertType.INFORMATION);

    ButtonType okayButton = new ButtonType("Okay");

    confirmation.getButtonTypes().setAll(okayButton);
    String title;
    String header;
    String text;
    if (user != null) {
      title = "Logged in";
      header = "You have succsessfully logged in to your account";
      text = "You can go the Front Page";
    } else {
      title = "Account Created";
      header = "Your account has been successfully created";
      text = "You can now log in to messeption with your account";
    }
    confirmation.setTitle(title);
    confirmation.setHeaderText(header);
    confirmation.setContentText(text);

    Scene goTo = loginButton.getScene();
    Stage primaryStage = (Stage) goTo.getWindow();

    Optional<ButtonType> result = confirmation.showAndWait();

    if (result.get() == okayButton) {
      if (user != null) {
        boardAccess.setActiveUser(user);
        goTo = frontPageScene;
        try {
          frontPageController.drawPosts();
        } catch (Exception e) {
          UiUtils.popupAlert(e, "Something went wrong when loading page").showAndWait();
          goTo = loginButton.getScene();
        }
      }
    }
    primaryStage.setScene(goTo);
  }

  private void clearAllInputFields() {
    loginPasswordField.setText("");
    loginUserTextField.setText("");
    signUpUserTextField.setText("");
    signUpPasswordField.setText("");
    signUpPasswordFieldCheck.setText("");
  }
}
