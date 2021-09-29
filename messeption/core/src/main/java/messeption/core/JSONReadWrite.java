package messeption.core;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class JSONReadWrite {
    public static final String ROOT_PATH = Paths.get("").toAbsolutePath().toString() + "/src/main/resources/messeption/"; // ender med messeption/core/ eller messeption/ui/
    public static final String UI_PATH = "ui/";
    public static final String DEFAULT_BOARD_FILE = "Board.JSON";

    public static ForumBoard fileRead(String filePath, String fileName)
            throws JsonSyntaxException, JsonIOException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileReader reader = new FileReader(ROOT_PATH + filePath + fileName);
        ForumBoard toReturn = gson.fromJson(reader, ForumBoard.class);
        reader.close();
        return toReturn;
    }

    public static void fileWrite(String filePath, String fileName, ForumBoard board)
            throws JsonIOException, IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter writer = new FileWriter(ROOT_PATH + filePath + fileName);
        gson.toJson(board, writer);
        writer.close();
    }

    public static ForumBoard fileRead() throws JsonSyntaxException, JsonIOException, IOException {
        return fileRead(UI_PATH, DEFAULT_BOARD_FILE);
    }

    public static void fileWrite(ForumBoard board) throws JsonIOException, IOException {
        fileWrite(UI_PATH, DEFAULT_BOARD_FILE, board);
    }
}