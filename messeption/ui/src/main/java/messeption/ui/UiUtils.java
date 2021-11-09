package messeption.ui;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;

/**
 * UiUtil is a collcection of methods that are used in all of the controllers.
 */
public class UiUtils {
  private static final String HIGHLIGHTED_STYLE =
      "-fx-border-color: DarkOrange; -fx-border-radius: 3px; -fx-border-width: 2px;";
  private static final String NORMAL_STYLE = "";
  /**
   * Finds a node in a list of nodes from an ID.

   * @param children the list of nodes to look in
   * @param id       the ID to look for
   * @return the node with the matching ID, if none return null
   */
  public static Node getNodeFromId(List<Node> children, String id) {
    for (Node child : children) {
      if (child.getId() != null && child.getId().equals(id)) {
        return child;
      }
    }
    return null;
  }

  /**
   * If an exception is raised it is here processed into an alert for the UI.

   * @param e the exception to be processed
   * @return the finished Alert
   */
  public static Alert exceptionAlert(Exception e) {

    Alert toReturn = new Alert(AlertType.ERROR);
    toReturn.setContentText(e.toString() + "\n" + e.getCause());
    System.err.println(e.toString() + "\n" + e.getCause());
    toReturn.setTitle("Error");
    return toReturn;
  }

  /**
   * If an exception is raised it is here processed into an alert for the UI.

   * @param e the string to be processed
   * @return the finished Alert
   */
  public static Alert popupAlert(String e) {

    Alert toReturn = new Alert(AlertType.ERROR);
    toReturn.setContentText(e);
    System.err.println(e);
    toReturn.setTitle("Error");
    return toReturn;
  }

  /**
   * If an exception is raised it is here processed into an alert for the UI.

   * @param button the button to change style of
   * @param highlighted if the button shall be highlighted or not
   */
  public static void setStyleOfButton(Button button, boolean highlighted) {
    if (highlighted) {
      button.setStyle(HIGHLIGHTED_STYLE);
    } else {
      button.setStyle(NORMAL_STYLE);
    }
  }


  
  /** 
   * This method is used to set the onAction events of the navbarbuttons on every page/scene.

   * @param menuQuit menuItem for quitting the app
   * @param menuLogOut menuItem for logging out
   * @param menuAbout menuItem for reading about info
   * @param primaryStage primaryStage of the application
   * @param loginPageScene the scene you get directed to when "logging out"
   */
  public static void setNavBarButtons(MenuItem menuQuit, MenuItem menuLogOut, MenuItem menuAbout,
      Stage primaryStage, Scene loginPageScene) {
    menuQuit.setOnAction(e -> {
      primaryStage.close();
    });
    
    menuLogOut.setOnAction(e -> {
      primaryStage.setScene(loginPageScene);
    });

    menuAbout.setOnAction(e -> {
      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("About Messeption");
      alert.setContentText("Messeption is an app developed by 4 students at NTNU "
          + "in the Course IT1901 fall 2021. The app takes inspiration from forums,"
          + " for instance reddit.");
      alert.showAndWait();
    });
    
  }
}
