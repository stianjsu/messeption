package messeption.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
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

  private static final String[] UserHandlerFieldNames = {"users"};

  private Gson gson;

  private UserHandler handler;
  private JsonReadWrite readWrite;

  /**
  * Instanciates UserHandlerResource for handling http-requests for users.

  * @param handler UserHandler with users
  * @param readWrite Persistance object for saving users to file 
  */
  public UserHandlerResource(UserHandler handler, JsonReadWrite readWrite) {
    this.handler = handler;
    this.readWrite = readWrite;
    gson = new GsonBuilder().create();
  }

  private String saveUsersToServer() {
    try {
      this.readWrite.fileWriteUserHandler(this.handler);
    } catch (IOException e) {
      return "500;Server failed to save users";
    }
    return "200;Server successfully saved users";
  }

  private void readUsersFromServer() {
    try {
      this.handler = this.readWrite.fileReadUserHandler();
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
    readUsersFromServer();
    LOGG.debug("getForumBoard: " + handler);
    return handler;
  }
  
  /**
   * Method for setting User handler to a scpeified state.

   * @param save the handler to save in json string format
   * @return returns an appropriate string response
   */
  @PUT
  @Path("/set")
  @Consumes(MediaType.APPLICATION_JSON)
  public String setUserHandler(String save) {
    LOGG.debug("setUserHandler: " + save);
    try {
      
      ForumBoardService.checkJsonIntegrety(save, UserHandlerFieldNames);
      
      this.handler = gson.fromJson(save, UserHandler.class);
    } catch (JsonParseException e) {
      return "406;Server failed to parse request due to bad json input";
    }
    String status = saveUsersToServer();
    if (!status.split(";")[0].equals("200")) {
      readUsersFromServer();
      return status;
    }
    return "200;Users successfully saved";
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
      this.handler.addUser(userToAdd.getUsername(), userToAdd.getPassword());
    } catch (IllegalArgumentException e) {
      return "403;" + e.getMessage();
    } catch (Exception e) {
      return "406;Add user request was not processed due to bad json input";
    }
    
    String status = saveUsersToServer();
    if (!status.split(";")[0].equals("200")) {
      readUsersFromServer();
      return status;
    }

    return "200;Server successfully added user";
  }
}
