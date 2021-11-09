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
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import java.io.IOException;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.User;
import messeption.core.UserHandler;
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


  private String saveBoardToServer() {
    try {
      boardReadWrite.fileWriteForumBoard(board);
    } catch (IOException e) {
      return "500;Server failed to save request";
    }
    return "200;Server successfully saved file";
  }

  private void readBoardFromServer() {
    try {
      this.board = boardReadWrite.fileReadForumBoard();
    } catch (IOException e) {
      e.printStackTrace();
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
   * @return returns an appropriate string response
   */
  @PUT
  @Path("/set")
  @Consumes(MediaType.APPLICATION_JSON)
  public String setForumBoard(String save) {
    LOGG.debug("setForumBoard: " + save);
    try {
      this.board = gson.fromJson(save, ForumBoard.class);
    } catch (JsonParseException e) {
      return "500;Server failed to parse request";
    }
    return saveBoardToServer();
  }

  /**
   * Adds a post to the board.

   * @param post the post to add in json string format
   * @return returns an appropriate string response
   */
  @POST
  @Path(FORUM_POST_PATH + "/addPost")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addPost(String post) {
    try {
      ForumPost postToSave = gson.fromJson(post, ForumPost.class);
      board.newPost(postToSave);
    } catch (Exception e) {
      return "406;Add post request was not processed due to bad json input";
    }

    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }

    return "200;Server successfully added post";
  }

  /**
   * Adds a comment to the a post with matching id.

   * @param id if of the post to add to
   * @param comment the comment to add in json string format
   * @return returns an appropriate string response
   */
  @POST
  @Path(POST_COMMENT_PATH + "/addComment/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String addComment(@PathParam("id") String id, String comment) {
    try {
      PostComment commentToSave = gson.fromJson(comment, PostComment.class);
      try {
        board.getPost(id).addComment(commentToSave);
      } catch (Exception e) {
          return "500;Could process json, but not add comment";
      }
    } catch (Exception e) {
      return "406;Add post request was not processed due to bad json input";
    }
    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }
    return "200;Server successfully added comment";
  }

  /**
   * Likes a post with matching id.

   * @param id id of the post to like
   * @param user the user that likes the post
   * @return returns an appropriate string response
   */
  @PUT
  @Path(FORUM_POST_PATH + "/likePost/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String likePost(@PathParam("id") String id, String user) {
    try {
      User likingUser = gson.fromJson(user, User.class);
      try {
        board.getPost(id).like(likingUser);
      } catch (Exception e) {
        return "500;Could process json, but not like post";
      }
    } catch (Exception e) {
      return "406;Add post request was not processed due to bad json input";
    }
    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }
    return "200;Server successfully updated like status";
  }

  /**
   * Dislikes a post with matching id.

   * @param id id of the post to like
   * @param user the user that dislikes the post
   * @return returns an appropriate string response
   */
  @PUT
  @Path(FORUM_POST_PATH + "/dislikePost/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String dislikePost(@PathParam("id") String id, String user) {
    try {
      User likingUser = gson.fromJson(user, User.class);
      board.getPost(id).dislike(likingUser);
    } catch (Exception e) {
      return "406;Add post request was not processed due to bad json input";
    }
    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }
    return "200;Server successfully updated like status";
  }


  /**
   * Likes a comment with matching id.

   * @param postId id of post that contains comment
   * @param commentId id of the comment to like
   * @param user the user that likes the comment
   * @return returns an appropriate string response
   */
  @PUT
  @Path(POST_COMMENT_PATH + "/likeComment/{postId}/{commentId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String likeComment(@PathParam("postId") String postId, 
        @PathParam("commentId") String commentId, String user) {
    try {
      User likingUser = gson.fromJson(user, User.class);
      board.getPost(postId).getComment(commentId).like(likingUser);
    } catch (Exception e) {
      return "406;Add post request was not processed due to bad json input";
    }
    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }
    return "200;Server successfully updated like status";
  }

  /**
   * Disikes a comment with matching id.

   * @param postId id of post that contains comment
   * @param commentId id of the comment to dislike
   * @param user the user that dislikes the comment
   * @return returns an appropriate string response
   */
  @PUT
  @Path(POST_COMMENT_PATH + "/dislikeComment/{postId}/{commentId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String dislikeComment(@PathParam("postId") String postId, 
        @PathParam("commentId") String commentId, String user) {
    try {
      User likingUser = gson.fromJson(user, User.class);
      board.getPost(postId).getComment(commentId).dislike(likingUser);
    } catch (Exception e) {
      return "406;Add post request was not processed due to bad json input";
    }
    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }
    return "200;Server successfully updated dislike status";
  }

  
  @Path(USER_RESOURCE_PATH)
  public UserHandlerResource getUserHandler() {
    LOGG.debug("Sub resource for UserHandler");
    return new UserHandlerResource(handler, handlerReadWrite);
  }

}
