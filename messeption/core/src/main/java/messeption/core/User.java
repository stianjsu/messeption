package messeption.core;

/**
 * A class for user credentials.
 */ 
public class User {
  private final String username;
  private final String password;

  public User(String username, String password) {
    this.username = username;
    this.password = password;
  }

  public String getPassword() {
    return this.password;
  }

  public String getUsername() {
    return this.username;
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
    if (obj instanceof User) {
      User o = (User) obj;
      return o.getUsername().equals(this.username) && o.getPassword().equals(this.password)
          && this.hashCode() == o.hashCode();
    }
    return false;
  }

  @Override
  public int hashCode() {
    return this.password.length() * this.username.length();
  }
}
