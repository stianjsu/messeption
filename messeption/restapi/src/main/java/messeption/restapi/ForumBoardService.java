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
 * An api for communicating with the rest server.
 */
@Path(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class ForumBoardService {

  public static final String FORUM_BOARD_SERVICE_PATH = "board";
  public static final String FORUM_POST_PATH = "/posts";
  public static final String POST_COMMENT_PATH = "/comments";
  public static final String USER_RESOURCE_PATH = "/users";

  private static final Logger LOGG = LoggerFactory.getLogger(ForumBoardService.class);

  Gson gson = new GsonBuilder().create();

  @Context
  private ForumBoard board;

  @Context
  private UserHandler handler;

  @Context
  private JsonReadWrite boardReadWrite;

  @Context
  private JsonReadWrite handlerReadWrite;


  private Response saveBoardToServer() {
    try {
      boardReadWrite.fileWriteForumBoard(board);
    } catch (IOException e) {
      return Response.serverError().entity("Server failed to save request").build();
    }
    return Response.ok("Server successfully saved file").build();
  }

  private void readBoardFromServer() {
    try {
      this.board = boardReadWrite.fileReadForumBoard();
    } catch (IOException e) {
      e.printStackTrace();  //TODO add throws
    }
  }

  @GET
  public ForumBoard getForumBoard() {
    LOGG.debug("getForumBoard: " + board);
    return board;
  }
  
  //@PUT for å overskrive
  //@POST for å legge til
  
  /**
   * Method for setting forum board to a scpeified state.

   * @param save the board to save in json string format
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
  @POST
  @Path(FORUM_POST_PATH + "/addPost")
  @Consumes(MediaType.APPLICATION_JSON)
  public Response addPost(String post) {
    try {
      ForumPost postToSave = gson.fromJson(post, ForumPost.class);
      board.newPost(postToSave);
    } catch (Exception e) {
      return Response.notAcceptable(null).entity(
          "Add post request was not processed due to bad json input").build();
    }

    Response r = saveBoardToServer();
    if (r.getStatus() != 200) {
      readBoardFromServer();
      return r;
    }

    return Response.ok("Server successfully added post").build();
  }

  /**
   * Adds a comment to the a post with matching id.

   * @param id if of the post to add to
   * @param comment the comment to add in json string format
   * @return returns an appropriate response
   */
  @POST
  @Path(POST_COMMENT_PATH + "/addComment/{id}")
  public Response addComment(@PathParam("id") String id, String comment) {
    try {
      PostComment commentToSave = gson.fromJson(comment, PostComment.class);
      try {
        board.getPost(id).addComment(commentToSave);
      } catch (Exception e) {
        return Response.serverError().entity(
          "Could process json, but not add comment").build();
      }
    } catch (Exception e) {
      return Response.notAcceptable(null).entity(
          "Add comment request was not processed due to bad json input").build();
    }
    Response r = saveBoardToServer();
    if (r.getStatus() != 200) {
      readBoardFromServer();
      return r;
    }
    return Response.ok("Server successfully added comment").build();
  }

  /**
   * Likes a post with matching id.

   * @param id id of the post to like
   * @param user the user that likes the post
   * @return returns an appropriate response
   */
  @PUT
  @Path(FORUM_POST_PATH + "/likePost/{id}")
  public Response likePost(@PathParam("id") String id, String user) {
    try {
      User likingUser = gson.fromJson(user, User.class);
      try {
        board.getPost(id).like(likingUser);
      } catch (Exception e) {
        return Response.serverError().entity(
          "Could process json, but not like post").build();
      }
    } catch (Exception e) {
      return Response.notAcceptable(null).entity(
          "Like post request was not processed due to bad json input").build();
    }
    Response r = saveBoardToServer();
    if (r.getStatus() != 200) {
      readBoardFromServer();
      return r;
    }
    return Response.ok("Server successfully updated like status").build();
  }

  /**
   * Dislikes a post with matching id.

   * @param id id of the post to like
   * @param user the user that dislikes the post
   * @return returns an appropriate response
   */
  @PUT
  @Path(FORUM_POST_PATH + "/dislikePost/{id}")
  public Response dislikePost(@PathParam("id") String id, String user) {
    try {
      User likingUser = gson.fromJson(user, User.class);
      board.getPost(id).dislike(likingUser);
    } catch (Exception e) {
      return Response.notAcceptable(null).entity(
          "Like post request was not processed due to bad json input").build();
    }
    Response r = saveBoardToServer();
    if (r.getStatus() != 200) {
      readBoardFromServer();
      return r;
    }
    return Response.ok("Server successfully updated like status").build();
  }


  /**
   * Likes a comment with matching id.

   * @param postId id of post that contains comment
   * @param commentId id of the comment to like
   * @param user the user that likes the comment
   * @return returns an appropriate response
   */
  @PUT
  @Path(POST_COMMENT_PATH + "/likeComment/{postId}/{commentId}")
  public Response likeComment(@PathParam("postId") String postId, 
        @PathParam("commentId") String commentId, String user) {
    try {
      User likingUser = gson.fromJson(user, User.class);
      board.getPost(postId).getComment(commentId).like(likingUser);
    } catch (Exception e) {
      return Response.notAcceptable(null).entity(
          "Like comment request was not processed due to bad json input").build();
    }
    Response r = saveBoardToServer();
    if (r.getStatus() != 200) {
      readBoardFromServer();
      return r;
    }
    return Response.ok("Server successfully updated like status").build();
  }

  /**
   * Disikes a comment with matching id.

   * @param postId id of post that contains comment
   * @param commentId id of the comment to dislike
   * @param user the user that dislikes the comment
   * @return returns an appropriate response
   */
  @PUT
  @Path(POST_COMMENT_PATH + "/dislikeComment/{postId}/{commentId}")
  public Response dislikeComment(@PathParam("postId") String postId, 
        @PathParam("commentId") String commentId, String user) {
    try {
      User likingUser = gson.fromJson(user, User.class);
      board.getPost(postId).getComment(commentId).dislike(likingUser);
    } catch (Exception e) {
      return Response.notAcceptable(null).entity(
          "Dislike comment request was not processed due to bad json input").build();
    }
    Response r = saveBoardToServer();
    if (r.getStatus() != 200) {
      readBoardFromServer();
      return r;
    }
    return Response.ok("Server successfully updated dislike status").build();
  }

  
  @Path(USER_RESOURCE_PATH)
  public UserHandlerResource getUserHandler() {
    LOGG.debug("Sub resource for UserHandler");
    return new UserHandlerResource(handler, handlerReadWrite);
  }

}
