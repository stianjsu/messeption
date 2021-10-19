package messeption.ui;

import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class UiUtils {
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
}
