package messeption.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import messeption.core.*;
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

  public UserHandlerResource(UserHandler handler, JsonReadWrite handlerReadWrite) {
    this.handler = handler;
    this.handlerReadWrite = handlerReadWrite;
    gson = new GsonBuilder().create();
  }

  private Response saveUsersToServer() {
    try {
      this.handlerReadWrite.fileWriteUserHandler(this.handler);
    } catch (IOException e) {
      return Response.serverError().entity("Server failed to save users").build();
    }
    return Response.ok("Server successfully saved users").build();
  }

  private void readUsersFromServer() {
    try {
      this.handler = this.handlerReadWrite.fileReadUserHandler();
    } catch (IOException e) {
      e.printStackTrace();  //TODO add throws
    }
  }

  @GET
  public UserHandler getUserHandler(){
    LOGG.debug("getForumBoard: " + handler);
    return handler;
  }


  @POST
  @Path("/addUser")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addUser(String user) {
    try{
      User userToAdd = gson.fromJson(user, User.class);
      if(!this.handler.addUser(userToAdd)){
        return Response.notAcceptable(null).entity("User already added to server").build();
      }
    } catch (Exception e) {
      return Response.notAcceptable(null).entity("Add user request was not processed due to bad json input").build();
    }
    
    Response r = saveUsersToServer();
    if (r.getStatus() != 200) {
      readUsersFromServer();
      return r;
    }

    return Response.ok("Server successfully added post").build();
  }
}
