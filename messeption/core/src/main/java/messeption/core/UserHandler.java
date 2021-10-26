package messeption.core;

import java.util.HashMap;
import java.util.Map;

public class UserHandler {

  private Map<String, String> users = new HashMap<>();

  public Map<String, String> getUsers() {
      return users;
  }

  public void addUser(String username, String password) {
    if ((validateNewUsername(username) + validateNewPassword(password)).equals("")){
      users.put(username, password);
    }
  }
  
  public boolean userNameExists(String username){
    return this.users.containsKey(username);
  }

  public boolean correctPassword(String username, String password){
    return this.users.get(username).equals(password);
  }
  
  
  public String validateNewUsername(String username) {
    if (username.length()<5){
      return "Too short username";
    }
    else if(username.length() > 15){
      return "Too long username";
    }
    else if((username.contains(" "))) {
      return "Username can not cointain spaces";
    }
    else if(this.users.containsKey(username)){
      return "Username taken";
    }
    else {
      return "";
    }
  }

  public String validateNewPassword(String password) {
    String nums= password.replaceAll("[^0-9]", "");
    if (password.length()<5){
      return "Too short password";
    }
    else if(password.length() > 15){
      return "Too long password";
    }
    else if((password.contains(" "))) {
      return "Password can not cointain spaces";
    }
    else if (password.toLowerCase().equals(password)){
      return "Password does not contain uppercase";
    }
    else if (password.toUpperCase().equals(password)){
      return "Password does not contain lowercase";
    }
    else if (nums.isEmpty())
      return "Password does not contain numbers";
    else{
      return "";
    }
  }
}
