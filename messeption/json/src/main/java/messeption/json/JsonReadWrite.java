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
 * A class with methods for writing ForumBoard objects and UserHandler objects to Json file.
 */
public class JsonReadWrite {

  private String saveLocationRecource;

  public JsonReadWrite(URL saveLocationRecource) {
    this.saveLocationRecource = saveLocationRecource.getPath();
  }

  public void setSaveLocationRecource(URL saveLocationRecource) {
    this.saveLocationRecource = saveLocationRecource.getPath();
  }

  /**
   * Reads from a specific file and returns a ForumBoard object.

   * @return the Board object from the read file
   * @throws IOException throws in a regualr IO expetion is thrown
   */
  public ForumBoard fileReadForumBoard()
      throws IOException {
    try (InputStreamReader reader = new InputStreamReader(
          new FileInputStream(this.saveLocationRecource), StandardCharsets.UTF_8)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      ForumBoard toReturn = gson.fromJson(reader, ForumBoard.class);
      reader.close();
      return toReturn;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * Writes a ForumBoard object to a Json file at specified location.

   * @param board    the object to be written
   * @throws IOException     throws in a regualr IO expetion is thrown
   */
  public void fileWriteForumBoard(ForumBoard board) 
      throws IOException {
    try (OutputStreamWriter writer = new OutputStreamWriter(
          new FileOutputStream(this.saveLocationRecource), StandardCharsets.UTF_8)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(board, writer);
      writer.close();
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * Reads from file and returns a UserHandler object.

   * @return the UserHandler object from the read file
   * @throws IOException throws in a regualr IO expetion is thrown
   */
  public UserHandler fileReadUserHandler()
      throws IOException {
    try (InputStreamReader reader = new InputStreamReader(
          new FileInputStream(this.saveLocationRecource), StandardCharsets.UTF_8)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      UserHandler toReturn = gson.fromJson(reader, UserHandler.class);
      reader.close();
      return toReturn;
    } catch (Exception e) {
      throw new IOException(e);
    }
  }

  /**
   * Writes a UserHandler object to a Json file at specifified location.

   * @param users    the object to be written
   * @throws IOException     throws in a regualr IO expetion is thrown
   */
  public void fileWriteUserHandler(UserHandler users) 
      throws IOException {
    try (OutputStreamWriter writer = new OutputStreamWriter(
          new FileOutputStream(this.saveLocationRecource), StandardCharsets.UTF_8)) {
      Gson gson = new GsonBuilder().setPrettyPrinting().create();
      gson.toJson(users, writer);
      writer.close();
    } catch (Exception e) {
      throw new IOException(e);
    }
  }
  
  
  public String getSaveLocation() {
    return this.saveLocationRecource;
  }

}
