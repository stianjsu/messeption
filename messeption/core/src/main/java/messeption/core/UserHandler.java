package messeption.core;

import java.util.ArrayList;
import java.util.Collection;

/**
 * User handler class for containing users, and checking and verifying existing and new users.
 */
public class UserHandler {

  private Collection<User> users = new ArrayList<>();

  public Collection<User> getUsers() {
    return new ArrayList<>(users);
  }

  /**
   * Adds a user-password pair to users-map if valid username and password.

   * @param username the username of the user to be added
   * @param password the password of the user to be added
   * @throws IllegalArgumentException throws an exception if the user details is not correct
   */
  public void addUser(String username, String password) throws IllegalArgumentException {

    IllegalArgumentException usernameValidation = validateNewUsername(username);
    IllegalArgumentException passwordValidation = validateNewPassword(password);
    if (usernameValidation != null) {
      throw usernameValidation;
    }
    if (passwordValidation != null) {
      throw passwordValidation;
    }
    users.add(new User(username, password));
  }

  /**
   *  Method for adding an already existing object user.

   * @param user the user to add
   * @return ture if successful, false otherwise
   */
  public boolean addUser(User user) {
    if (!users.contains(user)) {
      return users.add(user);
    }
    return false;
  }
  
  /**
   * Checks if the inputed username alreaxy exists or not.

   * @param username the username to check
   * @return returns true if the username exists
   */
  public boolean userNameExists(String username) {
    for (User user : users) {
      if (user.getUsername().equals(username)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if the inputed username alreaxy exists or not.

   * @param username the username to check
   * @param password the username to against the username
   * @return returns true if the password is correct
   */
  public boolean correctPassword(String username, String password) {
    for (User user : this.users) {
      if (user.getUsername().equals(username)) {
        return user.getPassword().equals(password);
      }
    }
    return false;
  }
  
  /**
   * Checks if given username comply with standards for username.

   * @param username the user name to be validated
   * @return Exception with descriptive message. Null if username complied with all constraints  
   */
  private IllegalArgumentException validateNewUsername(String username) {
    if (username.length() < 5) {
      return new IllegalArgumentException("Too short username. Must be at least 5 characters long");
    } else if (username.length() > 15) {
      return new IllegalArgumentException("Too long username. Must be less than 16 characters");
    } else if (!username.matches("^\\w*$")) {
      return new IllegalArgumentException(
          "Username must be alphanumerical. Try using underscores ;)");
    } else if (userNameExists(username)) {
      return new IllegalArgumentException("The username is taken");
    }
    return null;
    
  }

  /**
   * Checks if given password comply with standards for password.

   * @param password the password to be validated
   * @return Exception with descriptive message. Null if password complied with all constraints  
   */
  private IllegalArgumentException validateNewPassword(String password) {
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

  /**
   * Custom defined equals method for use when comparing with this objects serialized
   * and deserialized clone. Follows equals contract of reflexitivity, symmetry,
   * transitivity and consitancy

   * @param obj Other object to compare
   * @return true if objects have the same properties 
   */
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (obj instanceof UserHandler) {
      UserHandler other = (UserHandler) obj;
      boolean hashCodeCheck = this.hashCode() == other.hashCode();
      boolean containsSameUsers = this.users.equals(other.getUsers());
      return hashCodeCheck && containsSameUsers;
    } 

    return false;
  }

  @Override
  public int hashCode() {
    return this.users.stream().mapToInt(e -> e.hashCode()).sum();
  }

  @Override
  public String toString() {
    StringBuilder string = new StringBuilder();
    for (User user : users) {
      string.append(user.getUsername() + ": " + user.getPassword() + "\n");
    }
    return string.toString();
  }
}
