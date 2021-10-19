package messeption.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import messeption.core.ForumBoard;

/**
 * A class with static methods for writing ForumBoard objects to Json file.
 */
public class JsonReadWrite {
  public static final String ROOT_PATH = Paths.get("").toAbsolutePath().toString() 
      + "/src/main/resources/messeption/";  // ends with messeption/core/ or messeption/ui/
  public static final String UI_PATH = "ui/";
  public static final String DEFAULT_BOARD_FILE = "Board.JSON";

  /**
   * Reads from a specific file and returns a ForumBoard object.

   * @param filePath the path of the file to read from
   * @param fileName the name of the file to read from
   * @return the Board object from the read file
   * @throws IOException         throws in a regualr IO expetion is thrown
   */
  public static ForumBoard fileRead(String filePath, String fileName)
      throws IOException {
    try (InputStreamReader reader = new InputStreamReader(
          new FileInputStream(ROOT_PATH + filePath + fileName), StandardCharsets.UTF_8)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      ForumBoard toReturn = gson.fromJson(reader, ForumBoard.class);
      reader.close();
      return toReturn;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  public static ForumBoard fileRead() throws IOException {
    return fileRead(UI_PATH, DEFAULT_BOARD_FILE);
  }

  /**
   * Writes a ForumBoard object to a Json file and a specific location.

   * @param filePath the path of the file to read from
   * @param fileName the name of the file to read from
   * @param board    the object to be written
   * @throws IOException     throws in a regualr IO expetion is thrown
   */
  public static void fileWrite(String filePath, String fileName, ForumBoard board) 
      throws IOException {
    try (OutputStreamWriter writer = new OutputStreamWriter(
          new FileOutputStream(ROOT_PATH + filePath + fileName), StandardCharsets.UTF_8)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(board, writer);
      writer.close();
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  public static void fileWrite(ForumBoard board) throws IOException {
    fileWrite(UI_PATH, DEFAULT_BOARD_FILE, board);
  }
}
