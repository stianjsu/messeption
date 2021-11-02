package messeption.restserver;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import messeption.core.ForumBoard;
import messeption.core.ForumPost;
import messeption.json.JsonReadWrite;
import messeption.restapi.ForumBoardService;

public class ForumConfig extends ResourceConfig {
  private ForumBoard board;
  private JsonReadWrite boardReadWrite;

  public ForumConfig(ForumBoard board) {
    setForumBoard(board);
    System.out.println(ForumConfig.class.getResource("Board.JSON"));
    boardReadWrite = new JsonReadWrite(ForumConfig.class.getResource("Board.JSON"));
    //readWrite.setSaveFile("server-Forumlist.json");  //TODO implement new saving logic
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
      System.out.println(url.getPath());
      JsonReadWrite boardReadWrite = new JsonReadWrite(url);
      try {
        return boardReadWrite.fileReadForumBoard();
      } catch (Exception e) {
        System.out.println("Couldn't read default Board.JSON, thus created empty board ("
            + e + ")");
      }
    }
    ForumBoard ForumBoard = new ForumBoard();
    return ForumBoard;
  }
}
