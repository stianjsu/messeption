package messeption.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.io.IOException;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.json.JsonReadWrite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * An api for communicating with the rest server.
 */
@Path(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class ForumBoardService {

  public static final String FORUM_BOARD_SERVICE_PATH = "board";
  public static final String FORUM_POST_PATH = "/posts";
  public static final String POST_COMMENT_PATH = "/comments";

  private static final Logger LOGG = LoggerFactory.getLogger(ForumBoardService.class);

  Gson gson = new GsonBuilder().create();

  @Context
  private ForumBoard board;

  @Context
  private JsonReadWrite readWrite;


  @GET
  public ForumBoard getForumBoard() {
    LOGG.debug("getForumBoard: " + board);
    return board;
  }
  
  //@PUT for å overskrive
  //@POST for å legge til

  /**
   * Method for setting forum board to a scpeified state.

   * @param save teh board to save in json string format
   * @return returns an appropriate response
   */
  @PUT
  @Path("/set")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response setForumBoard(String save) {
    LOGG.debug("setForumBoard: " + save);
    try {
      this.board = gson.fromJson(save, ForumBoard.class);
    } catch (JsonParseException e) {
      return Response.serverError().entity("Server failed to parse request").build();
    }
    return saveBoardToServer();
  }

  /**
   * Adds a post to the board.

   * @param post the post to add in json string format
   * @return returns an appropriate response
   */
  @PUT
  @Path(FORUM_POST_PATH + "/addPost")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addPost(String post) {
    try {
      ForumPost postToSave = gson.fromJson(post, ForumPost.class);
      board.newPost(postToSave);
    } catch (Exception e) {
      return Response.notAcceptable(null).entity(
          "Request was not prossesed due to bad json input").build();
    }
    Response r = saveBoardToServer();
    if (r.getStatus() != 200) {
      readBoardFromServer();
    }
    return r;
  }

  /**
   * Adds a comment to the a post with matching id.

   * @param id if of the post to add to
   * @param comment the comment to add in json string format
   * @return returns an appropriate response
   */
  @PUT
  @Path(POST_COMMENT_PATH + "/addComment/{id}")
  public Response addComment(@PathParam("id") String id, String comment) {
    String[] idd = id.split("_");
    id = idd[0] + " " + idd[1];
    try {
      PostComment commentToSave = gson.fromJson(comment, PostComment.class);
      board.getPost(id).addComment(commentToSave);
    } catch (Exception e) {
      return Response.serverError().entity("Server failed to add comment").build();
    }
    return Response.ok("Server successfully added comment").build();
  }

  private Response saveBoardToServer() {
    try {
      readWrite.fileWriteForumBoard(board);
    } catch (IOException e) {
      return Response.serverError().entity("Server failed to save request").build();
    }
    return Response.ok("Server successfully saved file").build();
  }

  private void readBoardFromServer() {
    try {
      this.board = readWrite.fileReadForumBoard();
    } catch (IOException e) {
      e.printStackTrace();  //TODO add throws
    }
  }
}
