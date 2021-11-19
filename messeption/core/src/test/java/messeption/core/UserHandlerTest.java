package messeption.core;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


public class UserHandlerTest {

  private UserHandler users;
  private Collection<User> usersCopy;
  private User user;

  @BeforeEach
  public void setup() {
    users = new UserHandler();
    user = new User("Username", "Password123");
    usersCopy = new ArrayList<>();
  }

  @Test
  @DisplayName("Test add user")
  public void testAddValidUser() throws Exception {
    users.addUser(user);
    usersCopy.add(user);
    assertEquals(usersCopy, users.getUsers());
    users.addUser("OkayName", "Password123");
    usersCopy.add(new User("OkayName", "Password123"));
  }

  @Test
  @DisplayName("Test add user with invalid username")
  public void testAddInvalidUsername() {
    assertThrows(Exception.class, () -> {
      users.addUser("OkayName", "");
    });
    assertThrows(Exception.class, () -> {
      users.addUser("LoooooooooooooooooooooooooongName", "");
    });
    assertThrows(Exception.class, () -> {
      users.addUser("A Name", "");
    });
    assertThrows(Exception.class, () -> {
      users.addUser("", "");
    });
    assertThrows(Exception.class, () -> {
      users.addUser("", "       ");
    });
    users.addUser(user);
    assertThrows(Exception.class, () -> {
      users.addUser(user.getUsername(), "");
    });
  }

  @Test
  @DisplayName("Test add user with validname, but invalid password")
  public void testAddInvalidPassword() {
    assertThrows(Exception.class, () -> {
      users.addUser("OkayUserName", "");
    });
    assertThrows(Exception.class, () -> {
      users.addUser("OkayUserName", "LoooooooooooooooooooongPassword123");
    });
    assertThrows(Exception.class, () -> {
      users.addUser("OkayUserName", "password");
    });
    assertThrows(Exception.class, () -> {
      users.addUser("OkayUserName", "PASSWORD");
    });
    assertThrows(Exception.class, () -> {
      users.addUser("OkayUserName", "Password");
    });
  }

  @Test
  @DisplayName("Test add user")
  public void testAddExistingUser() {
    users.addUser(user);
    usersCopy.add(user);
    assertEquals(usersCopy, users.getUsers());
    users.addUser(user);
    usersCopy.add(user);
    assertNotEquals(usersCopy, users.getUsers());
  }

  @Test
  @DisplayName("Test username exists")
  public void testUsernameExists() {
    users.addUser(user);
    assertTrue(users.userNameExists(user.getUsername()));
    assertFalse(users.userNameExists("UsernameTest"));
    assertFalse(users.userNameExists("U"));
  }

  @Test
  @DisplayName("Test correct password")
  public void testCorrectPassword() {
    users.addUser(user);
    assertTrue(users.correctPassword(user.getUsername(), user.getPassword()));
  }

  @Test
  @DisplayName("Test incorrect password")
  public void testIncorrectPassword() {
    users.addUser(user);
    assertFalse(users.correctPassword("Potet", "Sekk123"));
  }

  @Test
  @DisplayName("Test equals")
  public void testEquals() {
    assertTrue(users.equals(users));
    assertFalse(users.equals(null));
    users.addUser(user);
    UserHandler users2 = users;
    assertTrue(users.equals(users2));
    assertFalse(users.equals(new Object()));
  }

  @Test
  @DisplayName("Test toString")
  public void testToString() {
    users.addUser(user);
    UserHandler users2 = users;
    assertEquals(users.toString(), users2.toString());
    assertNotEquals(users.toString(), new Object().toString());
  }


}