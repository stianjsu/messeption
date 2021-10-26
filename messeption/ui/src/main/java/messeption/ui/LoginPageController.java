package messeption.ui;

import java.util.Optional;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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


  /**
   * initializer for the scene.
   */
  public void initialize() {
    //userHandler= JsonReadWrite.readUserData(); noe sånnt vi vil ha

    // Midlertidig for testing
    userHandler = new UserHandler();
    userHandler.addUser("Jonah", "Hei123");

    loginButton.setOnAction((e) -> {
      login();
      
    });
    signupButton.setOnAction((e) -> {
      signUp();
    });
  }

  public void setFrontPageScene(Scene frontPageScene) {
      this.frontPageScene = frontPageScene;
  }

  //Trykke på login knappen
  public void login() {
    String username = loginUserTextField.getText();
    if(! userHandler.userNameExists(username)){
      showError("Can not find username\nDont have an account? Sign up here ->");
    }
    else{
      String password = loginPasswordField.getText();
      if(! userHandler.correctPassword(username, password)){
        showError("Wrong password for user " + username);
      }
      else{
        sucsessAlert(true);
      }
    }
  }

  //Trykke på sign up knappen
  public void signUp(){
    String username = signUpUserTextField.getText();
    String validation = userHandler.validateNewUsername(username);
    if(! validation.equals("")){
      showError(validation);
    }
    else{
      String password = signUpPasswordField.getText();
      validation = userHandler.validateNewPassword(password);
      if(! validation.equals("")){
        showError(validation);
      }
      else{
        String password2 = signUpPasswordFieldCheck.getText();

        if (password.equals(password2)){
          sucsessAlert(false);
        }
        else{
          showError("You did not type the same password twice");
        }
      }
    }
  }

  public void showError(String message){
    this.errorLabel.setText("An error has occured:\n" + message);
  }

  public void sucsessAlert(boolean login) {
    Alert confirmation = new Alert(AlertType.INFORMATION);

    ButtonType okayButton = new ButtonType("Okay");

    confirmation.getButtonTypes().setAll(okayButton);
    String title;
    String header;
    String text;
    if(login){
      title = "Logged in";
      header = "You have succsessfully logged in to your account";
      text = "You can go the Front Page";
    }
    else{
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
      if(login){
        goTo = frontPageScene;
      }
    }
    primaryStage.setScene(goTo);
  }
}
