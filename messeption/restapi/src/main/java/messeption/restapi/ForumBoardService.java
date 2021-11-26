package messeption.restapi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
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

  private static final String[] forumBoardFieldNames = {"posts"};
  private static final String[] forumPostFieldNames = {"title", "comments", 
      "id", "text", "author", "timeStamp"};
  private static final String[] postCommentFieldNames = {"id", "text", "author", "timeStamp"};
  private static final String[] userFieldNames = {"name", "password"};

  private static final Logger LOGG = LoggerFactory.getLogger(ForumBoardService.class);

  private Gson gson = new GsonBuilder().create();

  @Context
  private ForumBoard board;

  @Context
  private UserHandler userHandler;

  @Context
  private JsonReadWrite readWrite;


  /**
   * A method for checking if the json file can be parsed.

   * @throws JsonParseException is thrown if the parsing fails
   */
  public static void checkJsonIntegrety(String json, String... fieldNames) 
        throws JsonParseException {
    if (!JsonReadWrite.checkJsonFieldCoverage(json, fieldNames)) {
      throw new JsonParseException("Failed to parse request due to bad json input");
    }
  }


  private String saveBoardToServer() {
    try {
      readWrite.fileWriteForumBoard(board);
    } catch (IOException e) {
      return "500;Server failed to save request";
    }
    return "200;Server successfully saved file";
  }

  private void readBoardFromServer() {
    try {
      this.board = readWrite.fileReadForumBoard();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @GET
  public String getForumBoard() {
    LOGG.debug("getForumBoard: posts amount:" + board.getPosts().size());
    return gson.toJson(board);
  }
  
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
      checkJsonIntegrety(save, forumBoardFieldNames);
      this.board = gson.fromJson(save, ForumBoard.class);
    } catch (JsonParseException e) {
      return "406;Server failed to parse request due to bad json input";
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
    LOGG.debug("addPost: " + post);
    try {
      checkJsonIntegrety(post, forumPostFieldNames);
      ForumPost postToSave = gson.fromJson(post, ForumPost.class);
      board.newPost(postToSave);
    } catch (JsonParseException e) {
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
    LOGG.debug("addPostComment: " + comment);
    try {
      checkJsonIntegrety(comment, postCommentFieldNames);
      PostComment commentToSave = gson.fromJson(comment, PostComment.class);
      try {
        board.getPost(id).addComment(commentToSave);
      } catch (Exception e) {
        return "500;Could process json, but not add comment";
      }
    } catch (JsonParseException e) {
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
    LOGG.debug("likePost: " + id + "\t" + user);
    try {
      checkJsonIntegrety(user, userFieldNames);
      User likingUser = gson.fromJson(user, User.class);
      try {
        board.getPost(id).like(likingUser);
      } catch (Exception e) {
        return "500;Could process json, but not like post";
      }
    } catch (JsonParseException e) {
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
    LOGG.debug("dislikePost: " + id + "\t" + user);
    try {
      checkJsonIntegrety(user, userFieldNames);
      User likingUser = gson.fromJson(user, User.class);
      board.getPost(id).dislike(likingUser);
    } catch (JsonParseException e) {
      return "406;Add post request was not processed due to bad json input";
    }
    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }
    return "200;Server successfully updated like status.";
  }

  /**
   * Deletes a post with matching id.

   * @param id id of the post to delete
   * @return returns an appropriate string response
   */
  @DELETE
  @Path(FORUM_POST_PATH + "/deletePost/{id}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String deletePost(@PathParam("id") String id) {
    LOGG.debug("deletePost: " + id);
    try {
      ForumPost postToDelete = board.getPost(id);
      board.deletePost(postToDelete);
    } catch (Exception e) {
      return "404;Post not deleted because it does not exist.";
    }
    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }
    return "200;Server successfully deleted post.";
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
    LOGG.debug("likeComment: " + postId + "\t" + commentId + "\t" + user);
    try {
      checkJsonIntegrety(user, userFieldNames);
      User likingUser = gson.fromJson(user, User.class);
      board.getPost(postId).getComment(commentId).like(likingUser);
    } catch (JsonParseException e) {
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
    LOGG.debug("dislikeComment: " + postId + "\t" + commentId + "\t" + user);
    try {
      checkJsonIntegrety(user, userFieldNames);
      User likingUser = gson.fromJson(user, User.class);
      board.getPost(postId).getComment(commentId).dislike(likingUser);
    } catch (JsonParseException e) {
      return "406;Add post request was not processed due to bad json input";
    }
    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }
    return "200;Server successfully updated dislike status";
  }

  /**
   * Method for deleting comments on the server.

   * @param postId id of the corresponding post
   * @param commentId id of the comment to be deleted
   * @return returns an appropriate string response
   */
  @DELETE
  @Path(POST_COMMENT_PATH + "/deleteComment/{postId}/{commentId}")
  @Consumes(MediaType.APPLICATION_JSON)
  public String deleteComment(@PathParam("postId") String postId, 
      @PathParam("commentId") String commentId) {
    LOGG.debug("deletePost: " + postId + "\t" + commentId);
    try {
      ForumPost relevantPost = board.getPost(postId);
      PostComment commentToDelete = relevantPost.getComment(commentId);
      relevantPost.deleteComment(commentToDelete);
    } catch (Exception e) {
      return "404;Comment was not deleted because it does not exist";
    }
    String status = saveBoardToServer();
    if (!status.split(";")[0].equals("200")) {
      readBoardFromServer();
      return status;
    }
    return "200;Server successfully deleted comment";
  }

  /**
   * Method for getting the user handler from the sub resource.
   */
  @Path(USER_RESOURCE_PATH)
  public UserHandlerResource getUserHandler() {
    LOGG.debug("Sub resource for UserHandler");
    return new UserHandlerResource(userHandler, readWrite);
  }

}
