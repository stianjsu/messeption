package messeption.restserver;

import java.net.URL;
import messeption.core.ForumBoard;
import messeption.json.JsonReadWrite;
import messeption.restapi.ForumBoardService;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;


/**
 * Config file for the rest server.
 */
public class ForumConfig extends ResourceConfig {
  private ForumBoard board;
  private JsonReadWrite boardReadWrite;

  /**
   * Constructor for Config file from a ForumBoard object.

   * @param board the board to construct from
   */
  public ForumConfig(ForumBoard board) {
    setForumBoard(board);
    boardReadWrite = new JsonReadWrite(ForumConfig.class.getResource("Board.JSON"));
    register(ForumBoardService.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(ForumConfig.this.board);
        bind(ForumConfig.this.boardReadWrite);
      }
    });
  }

  public ForumConfig() {
    this(createDefaultForumBoard());
  }

  public ForumBoard getForumBoard() {
    return board;
  }

  public void setForumBoard(ForumBoard board) {
    this.board = board;
  }

  private static ForumBoard createDefaultForumBoard() {
    URL url = ForumConfig.class.getResource("Board.JSON");
    if (url != null) {
      JsonReadWrite boardReadWrite = new JsonReadWrite(url);
      try {
        return boardReadWrite.fileReadForumBoard();
      } catch (Exception e) {
        System.out.println("Couldn't read default Board.JSON, thus created empty board ("
            + e + ")");
      }
    }
    return new ForumBoard();
  }
}
