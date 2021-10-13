package messeption.core;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

/**
 * A class with static methods for writing ForumBoard objects to Json file.
 */
public class JsonReadWrite {
  public static final String ROOT_PATH = Paths.get("").toAbsolutePath().toString() 
      + "/src/main/resources/messeption/";
  // ender med messeption/core/ eller messeption/ui/
  public static final String UI_PATH = "ui/";
  public static final String DEFAULT_BOARD_FILE = "Board.JSON";

  /**
   * Reads from a specific file and returns a ForumBoard object.

   * @param filePath the path of the file to read from
   * @param fileName the name of the file to read from
   * @return the Board object from the read file
   * @throws JsonSyntaxException throws if the file does nnot have propoer syntax
   * @throws JsonIOException throws if the Json operation has an IO exception
   * @throws IOException throws in a regualr IO expetion is thrown
   */
  public static ForumBoard fileRead(String filePath, String fileName)
      throws JsonSyntaxException, JsonIOException, IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    InputStreamReader reader = new InputStreamReader(
        new FileInputStream(ROOT_PATH + filePath + fileName), StandardCharsets.UTF_8);
    ForumBoard toReturn = gson.fromJson(reader, ForumBoard.class);
    reader.close();
    return toReturn;
  }

  public static ForumBoard fileRead() throws JsonSyntaxException, JsonIOException, IOException {
    return fileRead(UI_PATH, DEFAULT_BOARD_FILE);
  }

  /**
   * Writes a ForumBoard object to a Json file and a specific location.

   * @param filePath the path of the file to read from
   * @param fileName the name of the file to read from
   * @param board the object to be written
   * @throws JsonIOException throws if the Json operation has an IO exception
   * @throws IOException throws in a regualr IO expetion is thrown
   */
  public static void fileWrite(String filePath, String fileName, ForumBoard board) 
      throws JsonIOException, IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    OutputStreamWriter writer = new OutputStreamWriter(
        new FileOutputStream(ROOT_PATH + filePath + fileName), StandardCharsets.UTF_8);
    gson.toJson(board, writer);
    writer.close();
  }

  public static void fileWrite(ForumBoard board) throws JsonIOException, IOException {
    fileWrite(UI_PATH, DEFAULT_BOARD_FILE, board);
  }
}
