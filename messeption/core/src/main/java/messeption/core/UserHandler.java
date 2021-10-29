package messeption.core;

import java.util.HashMap;
import java.util.Map;

/**
 * User handler class for checking and verifying existing and new users.
 */
public class UserHandler {

  private Map<String, String> users = new HashMap<>();

  public Map<String, String> getUsers() {
    return new HashMap<>(users);
  }

  /**
   * Adds a user-password pair to users-map if valid username and password.

   * @param username the username of the user to be added
   * @param password the password of the user to be added
   * @throws Exception throws an exception if the user details is not correct
   */
  public void addUser(String username, String password) throws Exception {

    Exception usernameValidation = validateNewUsername(username);
    Exception passwordValidation = validateNewPassword(password);
    if (usernameValidation != null) {
      throw usernameValidation;
    }
    if (passwordValidation != null) {
      throw passwordValidation;
    }
    users.put(username, password);
    
  }
  
  public boolean userNameExists(String username) {
    return this.users.containsKey(username);
  }

  public boolean correctPassword(String username, String password) {
    return this.users.get(username).equals(password);
  }
  
  /**
   * Checks if given username comply with standards for username.

   * @param username the user name to be validated
   * @return Exception with descriptive message. Null if username complied with all constraints  
   */
  private Exception validateNewUsername(String username) {
    if (username.length() < 5) {
      return new IllegalArgumentException("Too short username. Must be at least 5 characters long");
    } else if (username.length() > 15) {
      return new IllegalArgumentException("Too long username. Must be less than 16 characters");
    } else if (username.contains(" ")) {
      return new IllegalArgumentException(
          "Username can not cointain spaces. Try using underscores :)");
    } else if (this.users.containsKey(username)) {
      return new IllegalStateException("The username is taken");
    }
    return null;
    
  }

  /**
   * Checks if given password comply with standards for password.

   * @param password the password to be validated
   * @return Exception with descriptive message. Null if password complied with all constraints  
   */
  private Exception validateNewPassword(String password) {
    String nums = password.replaceAll("[^0-9]", "");
    if (password.length() < 5) {
      return new IllegalArgumentException("Too short password. Must be at least 5 characters long");
    } else if (password.length() > 15) {
      return new IllegalArgumentException("Too long password. Must be less than 16 characters");
    } else if ((password.contains(" "))) {
      return new IllegalArgumentException(
          "Password can not cointain spaces. Try using underscores :)");
    } else if (password.toLowerCase().equals(password)) {
      return new IllegalArgumentException("Password does not contain uppercase");
    } else if (password.toUpperCase().equals(password)) {
      return new IllegalArgumentException("Password does not contain lowercase");
    } else if (nums.isEmpty()) {
      return new IllegalArgumentException("Password does not contain numbers");
    }
    return null;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof UserHandler) {
      UserHandler other = (UserHandler) obj;
      boolean hashCodeCheck = this.hashCode() == other.hashCode();
      boolean containsSameUsers = this.users.equals(other.users);
      return hashCodeCheck && containsSameUsers;
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    int code = 0;
    for (Map.Entry<String, String> entry : users.entrySet()) {
      code += entry.getKey().length() + entry.getValue().length();
    }
    return code;
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    for (Map.Entry<String, String> entry : users.entrySet()) {
      string.append(entry.getKey() + ": " + entry.getValue() + "\n");
    }
    return string.toString();
  }
}
