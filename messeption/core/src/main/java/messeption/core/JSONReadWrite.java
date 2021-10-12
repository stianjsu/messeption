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



public class JSONReadWrite {
    public static final String ROOT_PATH = Paths.get("").toAbsolutePath().toString() + "/src/main/resources/messeption/"; // ender med messeption/core/ eller messeption/ui/
    public static final String UI_PATH = "ui/";
    public static final String DEFAULT_BOARD_FILE = "Board.JSON";

    public static ForumBoard fileRead(String filePath, String fileName)
            throws JsonSyntaxException, JsonIOException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        InputStreamReader reader = new InputStreamReader(new FileInputStream(ROOT_PATH + filePath + fileName), StandardCharsets.UTF_8);    //changed from fileReader in order to not rely on default encoding
        ForumBoard toReturn = gson.fromJson(reader, ForumBoard.class);
        reader.close();
        return toReturn;
    }

    public static ForumBoard fileRead() throws JsonSyntaxException, JsonIOException, IOException {
        return fileRead(UI_PATH, DEFAULT_BOARD_FILE);
    }

    public static void fileWrite(String filePath, String fileName, ForumBoard board)
            throws JsonIOException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(ROOT_PATH + filePath + fileName), StandardCharsets.UTF_8);  //changed from fileWriter in order to not rely on default encoding
        gson.toJson(board, writer);
        writer.close();
    }

    public static void fileWrite(ForumBoard board) throws JsonIOException, IOException {
        fileWrite(UI_PATH, DEFAULT_BOARD_FILE, board);
    }
}