package messeption.ui;

import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import messeption.core.UserHandler;

/**
 * Controller for the create post page.
 */
public class LoginPageController {

  private UserHandler userHandler;

  @FXML
  private Label errorLabel;
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

  private Scene frontPageScene;
  // private BoardAccessInterface boardAccess;

  /**
   * initializer for the scene.
   */
  public void initialize() {
    // userHandler= JsonReadWrite.readUserData(); noe sånnt vi vil ha

    // Midlertidig for testing
    userHandler = new UserHandler();
    try {
      userHandler.addUser("Jonah", "Hei123");
    } catch (Exception e) {
      UiUtils.exceptionAlert(e).showAndWait();
    }

    loginButton.setOnAction((e) -> {
      login();

    });
    signupButton.setOnAction((e) -> {
      signUp();
    });
  }

  // public void setBoardAccess(BoardAccessInterface boardAccess) {
  // this.boardAccess = boardAccess;
  // }
  // TODO implement this

  public void setFrontPageScene(Scene frontPageScene) {
    this.frontPageScene = frontPageScene;
  }

  /**
   * Method is called when the user tries to login.
   */
  public void login() {
    String username = loginUserTextField.getText();
    if (!userHandler.userNameExists(username)) {
      UiUtils.popupAlert("Can not find username\nDont have an account? Sign up :)").showAndWait();

    } else {
      String password = loginPasswordField.getText();
      if (!userHandler.correctPassword(username, password)) {
        UiUtils.popupAlert("Wrong password for user " + username).showAndWait();
      } else {
        sucsessAlert(true);
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
        userHandler.addUser(username, password);
        sucsessAlert(false);
        signUpUserTextField.clear();
        signUpPasswordField.clear();
        signUpPasswordFieldCheck.clear();
      } catch (Exception e) {
        UiUtils.popupAlert(e.getMessage()).showAndWait();
      }
    } else {
      UiUtils.popupAlert("You did not type the same password twice").showAndWait();
    }
  }

  /**
  * Opens an alert when the login is successful.

  * @param login is true if it is a login false if account creation
  */
  public void sucsessAlert(boolean login) {
    Alert confirmation = new Alert(AlertType.INFORMATION);

    ButtonType okayButton = new ButtonType("Okay");

    confirmation.getButtonTypes().setAll(okayButton);
    String title;
    String header;
    String text;
    if (login) {
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
      if (login) {
        goTo = frontPageScene;
      }
    }
    primaryStage.setScene(goTo);
  }
}
