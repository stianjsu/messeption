package messeption.ui;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.List;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.core.PostComment;
import messeption.core.User;
import messeption.core.UserHandler;

/**
 * Interface for how the ui interacts with the core moduel.
 */
public class BoardAccessRemote implements BoardAccessInterface {

  private URI endpointUri;

  private ForumBoard board;
  private UserHandler userHandler;
  private Gson gson;
  private User activeUser;
  
  /**
   * Constructor for BoardAccessRemote using the correct endpoint.

   * @param endpointUri the endpoint the access will use
   */
  public BoardAccessRemote(URI endpointUri) throws Exception {
    this.endpointUri = endpointUri;
    this.gson = new GsonBuilder().create();
    getBoardFromEndpoint();
    readUsers();
  }
  
  @Override
  public void updateBoardChange() throws Exception {
    try {
      String json = gson.toJson(this.board);
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(endpointUri + "/set"))
          .header("Accept", "application/json")
          .PUT(BodyPublishers.ofString(json))
          .build();
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public ForumBoard readBoard() throws Exception {
    getBoardFromEndpoint();
    return this.board;
  }


  private void getBoardFromEndpoint() {
    
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + ""))
        .header("Accept", "application/json")
        .GET()
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString()); 
      this.board = gson.fromJson(response.body(), ForumBoard.class);
      
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to get board from api.\n" + e);
    }

  } 

  @Override
  public void setBoard(ForumBoard board) throws Exception {
    try {
      this.board = board;
      updateBoardChange();
    } catch (RuntimeException e) {
      getBoardFromEndpoint();
      throw new IOException(e);
    }
  }

  @Override
  public ForumPost getPost(String id) {
    return board.getPost(id);
  }

  @Override
  public List<ForumPost> getPosts() {
    return board.getPosts();
  }

  @Override
  public void addPost(ForumPost post) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/posts/addPost"))
        .header("Accept", "application/json")
        .POST(BodyPublishers.ofString(gson.toJson(post)))
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
      board.newPost(post);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deletePost(String id) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/posts/deletePost/" + id))
        .header("Accept", "application/json")
        .DELETE()
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
      board.deletePost(id);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void likePost(String id, User user) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/posts/likePost/" + id))
        .header("Accept", "application/json")
        .PUT(BodyPublishers.ofString(gson.toJson(user)))
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
      board.getPost(id).like(user);
      
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void dislikePost(String id, User user) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/posts/dislikePost/" + id))
        .header("Accept", "application/json")
        .PUT(BodyPublishers.ofString(gson.toJson(user)))
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
      board.getPost(id).dislike(user);
  
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void likeComment(String postId, String commentId, User user) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/comments/likeComment/" + postId + "/" + commentId))
        .header("Accept", "application/json")
        .PUT(BodyPublishers.ofString(gson.toJson(user)))
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
      board.getPost(postId).getComment(commentId).like(user);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void dislikeComment(String postId, String commentId, User user) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/comments/likeComment/" + postId + "/" + commentId))
        .header("Accept", "application/json")
        .PUT(BodyPublishers.ofString(gson.toJson(user)))
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
      board.getPost(postId).getComment(commentId).dislike(user);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void addComment(String id, PostComment comment) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/comments/addComment/" + id))
        .header("Accept", "application/json")
        .POST(BodyPublishers.ofString(gson.toJson(comment)))
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
      board.getPost(id).addComment(comment);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void deleteComment(String postId, String commentId) throws Exception {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/comments/deleteComment" + postId + "/" + commentId))
        .header("Accept", "application/json")
        .DELETE()
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
      board.getPost(postId).deleteComment(commentId);
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public UserHandler readUsers() {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/users"))
        .header("Accept", "application/json")
        .GET()
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString()); 
      this.userHandler = gson.fromJson(response.body(), UserHandler.class);
      return userHandler;
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to get users from api.\n" + e);
    }
  }

  @Override
  public User getActiveUser() {
    return this.activeUser;
  }

  @Override
  public void setActiveUser(User user) {
    this.activeUser = user;
  }

  @Override
  public void addUser(String username, String password) throws IllegalArgumentException, Exception {
    User newUser = new User(username, password);
    String payload = gson.toJson(newUser);
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/users/addUser"))
        .header("Accept", "application/json")
        .POST(BodyPublishers.ofString(payload))
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
      this.userHandler.addUser(newUser.getUsername(), newUser.getPassword());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e);
    }
  }

  @Override
  public boolean userNameExists(String username) {
    return this.userHandler.userNameExists(username);
  }

  @Override
  public boolean correctPassword(String username, String password) {
    return this.userHandler.correctPassword(username, password);
  }

  private void checkResponse(String responseBody) throws IOException {
    
    String[] array = responseBody.split(";");

    int responseCode = Integer.parseInt(array[0]);
    String errorMessage = array[1];
    if (responseCode >= 500) {
      throw new IOException(responseCode + ": Server error \n" + errorMessage);
    } else if (responseCode != 200) {
      throw new IOException(responseCode + ": Api input error \n" + errorMessage);
    }
  }


  /**
   * Method for telling the server that a changed has been made to the UserHandler.
   */
  public void updateUsersChange() throws Exception {
    try {
      String json = gson.toJson(this.userHandler);
      System.out.println(json);
      HttpRequest request = HttpRequest.newBuilder()
          .uri(URI.create(endpointUri + "/users/set"))
          .header("Accept", "application/json")
          .PUT(BodyPublishers.ofString(json))
          .build();
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString());
      checkResponse(response.body());
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException(e);
    }
  }

  private void getUsersFromEndpoint() {
    HttpRequest request = HttpRequest.newBuilder()
        .uri(URI.create(endpointUri + "/users"))
        .header("Accept", "application/json")
        .GET()
        .build();
    try {
      final HttpResponse<String> response =
          HttpClient.newBuilder().build().send(request, HttpResponse.BodyHandlers.ofString()); 
      this.userHandler = gson.fromJson(response.body(), UserHandler.class);
      
    } catch (IOException | InterruptedException e) {
      throw new RuntimeException("Failed to get board from api.\n" + e);
    }
  }

  @Override
  public void setUserHandler(UserHandler userHandler) throws Exception {
    try {
      this.userHandler = userHandler;
      updateUsersChange();
    } catch (RuntimeException e) {
      getUsersFromEndpoint();
      throw new IOException(e);
    }
  }
}