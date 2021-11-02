package messeption.core;

/**
 * A class for user credentials.
 */ 
public class User {
  private String username;
  private String password;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public static User getAnonymousUser() {
    return new User("Anonymous", "");
  }

  public String getPassword() {
    return this.password;
  }

  public String getUsername() {
    return this.username;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj instanceof User) {
      User o = (User) obj;
      return o.getUsername().equals(this.username) && o.getPassword().equals(this.password);
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.password.length() + this.username.length();
  }
}
