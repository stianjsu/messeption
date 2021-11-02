package messeption.restapi;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import messeption.core.ForumPost;
import messeption.core.ForumBoard;
import messeption.json.JsonReadWrite;

@Path(ForumBoardService.FORUM_BOARD_SERVICE_PATH)
@Produces(MediaType.APPLICATION_JSON)
public class ForumBoardService {

  public static final String FORUM_BOARD_SERVICE_PATH = "board";

  private static final Logger LOGG = LoggerFactory.getLogger(ForumBoardService.class);

  @Context
  private ForumBoard board;


  @GET
  public ForumBoard getForumBoard() {
    LOGG.debug("getForumBoard: " + board);
    return board;
  }
  
  //@POST //for å sende til server

}
