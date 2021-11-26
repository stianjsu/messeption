package messeption.restserver;

import java.net.URL;
import java.util.Collection;
import messeption.core.ForumBoard;
import messeption.core.User;
import messeption.core.UserHandler;
import messeption.json.JsonReadWrite;
import messeption.restapi.ForumBoardService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;


/**
 * Config file for the rest server.
 */
public class ForumConfig extends ResourceConfig {

  private ForumBoard board;
  private JsonReadWrite readWrite;
  private UserHandler userHandler;

  /**
   * Constructor for Config file from a ForumBoard object.

   * @param board the board to construct from
   */
  public ForumConfig(ForumBoard board, UserHandler userHandler) {
    setForumBoard(board);
    setUserHandler(userHandler);
    readWrite = new JsonReadWrite(ForumConfig.class.getResource("Board.JSON"), 
        ForumConfig.class.getResource("Users.JSON"));
    
    register(ForumBoardService.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(ForumConfig.this.board);
        bind(ForumConfig.this.readWrite);
        bind(ForumConfig.this.userHandler);
      }
    });
  }

  public ForumConfig() {
    this(createDefaultForumBoard(), createDefaultUserHandler());
  }

  public ForumBoard getForumBoard() {
    return board;
  }

  public void setForumBoard(ForumBoard board) {
    this.board = board;
  }

  public Collection<User> getUsers() {
    return userHandler.getUsers();
  }

  public void setUserHandler(UserHandler userHandler) {
    this.userHandler = userHandler;
  }

  private static ForumBoard createDefaultForumBoard() {
    URL urlBoard = ForumConfig.class.getResource("Board.JSON");
    URL urlUsers = ForumConfig.class.getResource("Users.JSON");
    if (urlBoard != null) {
      JsonReadWrite boardReadWrite = new JsonReadWrite(urlBoard, urlUsers);
      try {
        return boardReadWrite.fileReadForumBoard();
      } catch (Exception e) {
        System.out.println("Couldn't read default Board.JSON, thus created empty board ("
            + e + ")");
      }
    }
    return new ForumBoard();
  }

  private static UserHandler createDefaultUserHandler() {
    URL urlBoard = ForumConfig.class.getResource("Board.JSON");
    URL urlUsers = ForumConfig.class.getResource("Users.JSON");
    if (urlUsers != null) {
      JsonReadWrite userReadWrite = new JsonReadWrite(urlBoard, urlUsers);
      try {
        return userReadWrite.fileReadUserHandler();
      } catch (Exception e) {
        System.out.println("Couldn't read default Users.JSON, thus created empty user handler ("
            + e + ")");
      }
    }
    return new UserHandler();
  }
}
