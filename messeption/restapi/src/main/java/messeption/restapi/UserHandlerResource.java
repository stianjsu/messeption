package messeption.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import messeption.core.User;
import messeption.core.UserHandler;
import messeption.json.JsonReadWrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * User hanlder resource for the rest api to support users.
 */
@Produces(MediaType.APPLICATION_JSON)
public class UserHandlerResource {

  public static final String USER_SERVICE_PATH = "/users";

  private static final Logger LOGG = LoggerFactory.getLogger(UserHandlerResource.class);

  private Gson gson;

  private UserHandler handler;
  private JsonReadWrite handlerReadWrite;

  /**
  * Instanciates UserHandlerResource for handling http-requests for users.

  * @param handler UserHandler with users
  * @param handlerReadWrite Persistance object for saving users to file 
  */
  public UserHandlerResource(UserHandler handler, JsonReadWrite handlerReadWrite) {
    this.handler = handler;
    this.handlerReadWrite = handlerReadWrite;
    gson = new GsonBuilder().create();
  }

  private String saveUsersToServer() {
    try {
      this.handlerReadWrite.fileWriteUserHandler(this.handler);
    } catch (IOException e) {
      return "500;Server failed to save users";
    }
    return "200;Server successfully saved users";
  }

  private void readUsersFromServer() {
    try {
      this.handler = this.handlerReadWrite.fileReadUserHandler();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Gets the userHandler for the server.

   * @return the UserHandler
   */
  @GET
  public UserHandler getUserHandler() {
    LOGG.debug("getForumBoard: " + handler);
    return handler;
  }

  /**
  * Adds a user to the current UserHandler.

  * @param user User to add as json-string
  * @return A appropriate string response
  */
  @POST
  @Path("/addUser")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addUser(String user) {
    try {
      User userToAdd = gson.fromJson(user, User.class);
      if (!this.handler.addUser(userToAdd)) {
        return "406;User already added to server";
      }
    } catch (Exception e) {
      return "406;Add user request was not processed due to bad json input";
    }
    
    String status = saveUsersToServer();
    if (!status.split(";")[0].equals("200")) {
      readUsersFromServer();
      return status;
    }

    return "200;Server successfully added post";
  }
}
