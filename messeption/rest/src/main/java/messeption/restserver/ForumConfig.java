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
  //(private JsonReadWrite readWrite;

  public ForumConfig(ForumBoard board) {
    setForumBoard(board);
    //readWrite = new JsonReadWrite();
    //readWrite.setSaveFile("server-Forumlist.json");  //TODO implement new saving logic
    register(ForumBoardService.class);
    register(JacksonFeature.class);
    register(new AbstractBinder() {
      @Override
      protected void configure() {
        bind(ForumConfig.this.board);
        //bind(ForumConfig.this.readWrite);
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
    URL url = ForumConfig.class.getResource("defaultBoard.JSON");
    if (url != null) {
      try {
        return JsonReadWrite.fileRead();// readWrite.fileRead();  //TODO implement new saving logic
      } catch (Exception e) {
        System.out.println("Couldn't read default Board.JSON, so rigging ForumBoard manually ("
            + e + ")");
      }
    }
    ForumBoard ForumBoard = new ForumBoard();
    return ForumBoard;
  }
}
