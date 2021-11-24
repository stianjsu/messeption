package messeption.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import messeption.core.ForumBoard;
import messeption.core.UserHandler;

/**
 * A class with methods for reading and writing ForumBoard objects 
 * and UserHandler objects to Json file.
 */
public class JsonReadWrite {

  private String saveLocationBoard;
  private String saveLocationUsers;

  public JsonReadWrite(URL saveLocationBoard, URL saveLocationUsers) {
    this.saveLocationBoard = saveLocationBoard.getPath();
    this.saveLocationUsers = saveLocationUsers.getPath();
  }

  public void setSaveLocationBoard(URL saveLocationBoard) {
    this.saveLocationBoard = saveLocationBoard.getPath();
  }

  public void setSaveLocationUsers(URL saveLocationUsers) {
    this.saveLocationUsers = saveLocationUsers.getPath();
  }

  /**
   * Reads from a specific file and returns a ForumBoard object.

   * @return the Board object from the read file
   * @throws IOException throws in a regualr IO expetion is thrown
   */
  public ForumBoard fileReadForumBoard() throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try (InputStreamReader reader = new InputStreamReader(
          new FileInputStream(this.saveLocationBoard), StandardCharsets.UTF_8)) {
      
      ForumBoard toReturn = gson.fromJson(reader, ForumBoard.class);
      return toReturn;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * Writes a ForumBoard object to a Json file at specified location.

   * @param board the object to be written
   * @throws IOException throws in a regualr IO expetion is thrown
   */
  public void fileWriteForumBoard(ForumBoard board) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try (OutputStreamWriter writer = new OutputStreamWriter(
          new FileOutputStream(this.saveLocationBoard), StandardCharsets.UTF_8)) {
      gson.toJson(board, writer);
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * Reads from file and returns a UserHandler object.

   * @return the UserHandler object from the read file
   * @throws IOException throws in a regualr IO expetion is thrown
   */
  public UserHandler fileReadUserHandler() throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try (InputStreamReader reader = new InputStreamReader(
          new FileInputStream(this.saveLocationUsers), StandardCharsets.UTF_8)) {
      UserHandler toReturn = gson.fromJson(reader, UserHandler.class);
      return toReturn;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * Writes a UserHandler object to a Json file at specifified location.

   * @param users the object to be written
   * @throws IOException throws in a regualr IO expetion is thrown
   */
  public void fileWriteUserHandler(UserHandler users) throws IOException {
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    try (OutputStreamWriter writer = new OutputStreamWriter(
          new FileOutputStream(this.saveLocationUsers), StandardCharsets.UTF_8)) {
      gson.toJson(users, writer);
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
  
  public String getSaveLocation() {
    return this.saveLocationBoard + " : " + this.saveLocationUsers;
  }

  /**
   * Checks if provided json contains values for all fields in provided in var-arg.

   * @param json json to validate
   * @param fieldNames field names to look for
   * @return true if json cover all fields
   */
  public static boolean checkJsonFieldCoverage(String json, String... fieldNames) {
    for (int i = 0; i < fieldNames.length; i++) {
      if (!json.contains(fieldNames[i])) {
        return false;
      }
    }
    return true;
  }
  
}
