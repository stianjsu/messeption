package messeption.ui;

import java.util.Comparator;
import java.util.Optional;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import messeption.core.ForumPost;

/**
 * UiUtil is a collection of methods that are used in all of the controllers.
 */
public class UiUtils {
  
  private static final String HIGHLIGHTED_STYLE =
      "-fx-border-color: DarkOrange; -fx-border-radius: 3px; -fx-border-width: 2px;";
  private static final String NORMAL_STYLE = "";

  /**
   * If an exception is raised it is here processed into an alert for the UI.

   * @param e the exception to be shown to developers
   * @param toUser the string to be shown to the user
   * @return the finished Alert
   */
  public static Alert popupAlert(Exception e, String toUser) {
    e.printStackTrace();
    return popupAlert(toUser);
  }

  /**
   * If an exception is raised it is here processed into an alert for the UI.

   * @param toUser the string to be shown to the user
   * @return the finished Alert
   */
  public static Alert popupAlert(String toUser) {

    Alert toReturn = new Alert(AlertType.ERROR);
    toReturn.setContentText(toUser);
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
   * Creates and alert to confirm choice and returns false if the user decides to cancel.

   * @param title title of Alert
   * @param header header of Alert
   * @param text text of Alert
   * @return true if the user confirms, false if cancel
   */
  public static boolean confimationAlert(String title, String header, String text) {
    Alert confirmation = new Alert(AlertType.INFORMATION);

    ButtonType cancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
    ButtonType confirm = new ButtonType("Yes");

    confirmation.getButtonTypes().setAll(confirm, cancel);

    confirmation.setTitle(title);
    confirmation.setHeaderText(header);
    confirmation.setContentText(text);

    Optional<ButtonType> result = confirmation.showAndWait();

    return result.get() == confirm;
  }

  /**
   * GetPostSorting creates a comparator based on what we want to sort the posts by.

   * @param sortBy String that defines what we will sort by
   * @return the comparator
   */
  public static Comparator<ForumPost> getPostSorting(String sortBy) {
    if (sortBy.equals("Title")) {
      return Comparator.comparing(p -> p.getTitle().toLowerCase());    
    }
    if (sortBy.equals("Author")) {
      return new Comparator<ForumPost>() {
        @Override
        public int compare(ForumPost o1, ForumPost o2) {
          if (o1.isAnonymous()) {
            return 1;
          }
          if (o2.isAnonymous()) {
            return -1;
          }
          return o1.getAuthor().getUsername().toLowerCase().compareTo(
                o2.getAuthor().getUsername().toLowerCase());
        }
      };
    }
    if (sortBy.equals("Text")) {
      return Comparator.comparing(p -> - p.getText().length());
    }
    if (sortBy.equals("Comments")) {
      return Comparator.comparing(p -> - p.getComments().size());
    }
    return null;
  }
}
